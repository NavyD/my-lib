package cn.navyd.lib.algs.graph;

import cn.navyd.lib.algs.sort.MinPQ;
import cn.navyd.lib.algs.util.Queue;

/**
 * 最小生成树的prim算法的延时实现：
 * 思想：
 * prim将所有生成树中顶点连接树外的边取最小边的另一顶点加入生成树中
 * 步骤：
 * 1.将顶点标记为最下生成树的顶点，并将其所有不在生成树的边入最小优先队列
 * 2.将取出当前生成树中的优先队列的最小边，将该边加入生成树的边
 * 3.使用边的未标记顶点重复
 *
 * 复杂度：连通加权无向图的最小生成树所需的空间与e成正比，空间与elge成正比(最坏)
 * @author Navy D
 * @date 20170909215028
 */
public class LazyPrimMST {
	// 标记最小生成树的顶点
	private boolean[] marked;
	// 最小生成树的边
	private Queue<Edge> mst;
	// 所有横切边的最小优先队列
	private MinPQ<Edge> pq;


	public LazyPrimMST(EdgeWeightedGraph g) {
		marked = new boolean[g.getV()];
		mst = new Queue<>();
		pq = new MinPQ<>();
		// 以顶点0开始作为最小生成树的第一个节点
		visit(g, 0); // 假设图g是连通的否则visit可能无法将边入最小优先队列

		while (!pq.isEmpty()) {
			// 从当前生成树顶点集合的取得与外集合的权重最小的边
			Edge e = pq.delMin();
			int v = e.either(), w = e.other(v);
			// 跳过失效的边
			if (marked[v] && marked[w])
				continue;
			// 将最小边加入生成树队列
			mst.enqueue(e);

			// 将当前最小边的两边未标记的顶点加入最小优先队列比较
			if (!marked[v])
				visit(g, v);
			if (!marked[w])
				visit(g, w);
		}
	}

	/**
	 * 标记顶点v并将所有连接v和未标记顶点的边加入最小优先队列pq
	 * @param g
	 * @param v
	 * @author Navy D
	 * @date 20170909214937
	 */
	private void visit(EdgeWeightedGraph g, int v) {
		marked[v] = true;
		for (Edge e : g.adj(v))
			if (!marked[e.other(v)])
				pq.insert(e);
	}

	public Iterable<Edge> edges() {
		return mst;
	}

	public double weight() {
		return 0.0;
	}
}
