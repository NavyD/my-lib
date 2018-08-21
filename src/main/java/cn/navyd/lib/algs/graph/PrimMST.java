package cn.navyd.lib.algs.graph;

import cn.navyd.lib.algs.sort.IndexMinPQ;
import cn.navyd.lib.algs.util.In;
import cn.navyd.lib.algs.util.Queue;
import cn.navyd.lib.algs.util.QuickUF;
import cn.navyd.lib.algs.util.UF;

/**
 * 加权无向图Prim算法的即时实现：<br>
 * 思想：
 * 对于树外的边，每个树外的顶点连接树的边只有一条权重最小的边，如果树外当前顶点
 * 由于树中新加顶点而多了一条边，需要保留小的边<br>
 *
 * 复杂度：计算所需空间与v成正比，时间与elgv成正比
 * @author Navy D
 * @date 20170910112040
 */
public class PrimMST implements MinSpanTree{
	// 距离树最近的边即edgeTo[v]=e表示v是下一个加入树中的顶点，e是树连接v的最小边
	private Edge[] edgeTo;
	// 树连接顶点v边的权值即树连接顶点v的边可能变化，需要更新保存
	private double[] distTo;
	// 如果顶点v在树中就为true
	private boolean[] marked;
	// 索引最小优先队列，使用顶点保存最小权值
	private IndexMinPQ<Double> pq;

	/**
	 * 计算加权无向图的最小生成树
	 * @param g
	 */
	public PrimMST(EdgeWeightedGraph g) {
		edgeTo = new Edge[g.getV()];
		distTo = new double[g.getV()];
		marked = new boolean[g.getV()];
		pq = new IndexMinPQ<>(g.getV());
		// 将所有顶点权值初始化最大
		for (int i = 0; i < g.getV(); i++)
			distTo[i] = Double.POSITIVE_INFINITY;

		// 对于非连通图，会生成连通树森林
		for (int v = 0; v < g.getV(); v++)
			if (!marked[v])
				prim(g, v);

		assert check(g);

	}
	/**
	 * 用prim算法寻找顶点v在图中的最小生成树
	 * @param g
	 * @param v
	 * @author Navy D
	 * @date 20170917213307
	 */
	private void prim(EdgeWeightedGraph g, int v) {
		// 初始化顶点v，这里的0仅仅只是为了找出0的边，权值不会保存在edgeTo中
		distTo[v] = .0;
		// 插入起点v 到达自身的距离为0
		pq.insert(0, .0);
		while (!pq.isEmpty())
			// 将最近的顶点添加到树中
			visit(g, pq.delMin());
	}

	/**
	 * 将顶点v添加到树中，更新数据
	 * @param g
	 * @param v
	 * @author Navy D
	 * @date 20170910111013
	 */
	private void visit(EdgeWeightedGraph g, int v) {
		// 将顶点v加入树中
		marked[v] = true;
		// 遍历顶点v的边
		for (Edge e : g.adj(v)) {
			int w = e.other(v);
			// 如果邻接顶点已经在树中就跳过
			if (marked[w])
				continue;
			// 如果连接树的边权值较小就更新数据
			if (e.weight() < distTo[w]) {
				// 更新树的连接边
				edgeTo[w] = e;
				// 更新权值
				distTo[w] = e.weight();
				// 如果树中之前有边连接到该顶点就更新
				if (pq.contains(w))
					pq.changeKey(w, distTo[w]);
				// 没有连接就插入
				else
					pq.insert(w, distTo[w]);
			}
		}
	}

	/**
	 * 返回最小生成树的路径
	 *
	 * @return
	 * @author Navy D
	 * @date 20170917204246
	 */
	public Iterable<Edge> edges() {
		Queue<Edge> mst = new Queue<Edge>();
		for (int v = 0; v < edgeTo.length; v++) {
			Edge e = edgeTo[v];
			if (e != null) {
				mst.enqueue(e);
			}
		}
		return mst;
	}

	/**
	 * 返回最小生成树的权值总和
	 * @return
	 * @author Navy D
	 * @date 20170917204313
	 */
	public double weight() {
		double weight = 0.0;
		for (Edge e : edges())
			weight += e.weight();
		return weight;
	}

	/**
	 * 主要检查条件：
	 * 1.生成树是否含环
	 * 2.生成树的边是否在图中横切边中最小
	 * @param g
	 * @return
	 * @author Navy D
	 * @date 20170917210822
	 */
	private boolean check(EdgeWeightedGraph g) {

		// 检查最小生成树的延时版和即时版的权重是否一致
		double total = 0.0;
        for (Edge e : edges()) {
            total += e.weight();
        }
        if (Math.abs(total - this.weight()) > 1E-12) {
            System.err.printf("Weight of edges does not equal weight(): %f vs. %f\n", total, weight());
            return false;
        }

        // 检查最小生成树是否含有环，树只要将顶点间任意添加一条边就会存在一个环
        UF uf = new QuickUF(g.getV());
        for (Edge e : edges()) {
            int v = e.either(), w = e.other(v);
            // 如果将生成树的顶点从0开始连接边，发现在顶点连接之前就已经存在一条边，表示存在环
            if (uf.connected(v, w)) {
                System.err.println("Not a forest");
                return false;
            }
            // 连接顶点
            uf.union(v, w);
        }

        // 检查是否是一个森林
        for (Edge e : g.edges()) {
            int v = e.either(), w = e.other(v);
            // 如果生成树未连接原图所有顶点即有一个顶点不在生成树中，生成树错误
            if (!uf.connected(v, w)) {
                System.err.println("Not a spanning forest");
                return false;
            }
        }

        // 检查给定的生成树删除任意边在原图中是否正好是顶点横切边最小的边
        for (Edge e : edges()) {

        	// 构建一个删除边e的最小生成树
			uf = new QuickUF(g.getV());
			for (Edge f : edges()) {
				int x = f.either(), y = f.other(x);
				// 不连接e
				if (f != e)
					uf.union(x, y);
			}

			// 在原图中检查顶点横切边生成树删除的边是否最小
			for (Edge f : g.edges()) {
				int x = f.either(), y = f.other(x);
				// 找到横切边顶点
				if (!uf.connected(x, y)) {
					// 如果横切边不是最小
					if (f.weight() < e.weight()) {
						System.err.println("Edge " + f + " violates cut optimality conditions");
						return false;
					}
				}
			}
        }
        return true;
    }

	public static void main(String[] args) {
		EdgeWeightedGraph g = new EdgeWeightedGraph(new In("../Algs_4/tinyEWG.txt"));
		PrimMST mst = new PrimMST(g);
		for (Edge e : mst.edges())
			System.out.println(e );
		System.out.println(mst.weight());

	}

}
