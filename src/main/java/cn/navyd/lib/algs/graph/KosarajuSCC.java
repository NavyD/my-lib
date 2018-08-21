package cn.navyd.lib.algs.graph;

import cn.navyd.lib.algs.util.In;
import cn.navyd.lib.algs.util.Queue;

/**
 * kosaraju算法：计算图g中所有的强连通分量(在一个环中)<br>
 * 步骤：<br>
 * 1.计算图g的反向图gr的逆后序排列<br>
 * 2.使用逆后序顶点顺序堆图g进行标准深搜<br>
 * 3.在g的标准深搜中dfs递归中的顶点都在同一个强连通分量<br>
 *
 * 复杂度：预处理时间和空间与v+e成正比，且支持常数时间的强连通图查询
 * @author Navy D
 * @date 20170907213441
 */
public class KosarajuSCC {
	// 访问过的顶点
	private boolean[] marked;
	// 强联通分量的标识符，用于标识是否在同一个强连通分量
	private int[] id;
	// 强连通分量的数量
	private int count;

	public KosarajuSCC(Digraph g) {
		marked = new boolean[g.getV()];
		id = new int[g.getV()];
		// 在g的反向图优先搜索取得反向图的逆后序
		DepthFirstOrder order = new DepthFirstOrder(g.reverse());
		// 反向图的逆后序的顺序进行深搜
		for (int s : order.reversePost())
			if (!marked[s]) {
				dfs(g, s);
				// 每次深搜都是不同的强连通分量
				count++;
			}

		assert check(g);
	}

	/**
	 * 使用dfs()递归调用的顶点均在同一个强连通向量
	 * @param g
	 * @param v
	 * @author Navy D
	 * @date 20170907213156
	 */
	private void dfs(Digraph g, int v) {
		marked[v] = true;
		// 标识当前顶点
		id[v] = count;
		for (int w : g.adj(v))
			if (!marked[w])
				dfs(g, w);
	}

	/**
	 * 如果顶点v和w是强联通的就返回true
	 * @param v
	 * @param w
	 * @return
	 * @author Navy D
	 * @date 20170907215201
	 */
	public boolean stronglyConnected(int v, int w) {
		validateVertex(v);
		validateVertex(w);
		return id[v] == id[w];
	}

	/**
	 * 返回顶点v所在的强连通分量的标识符
	 * @param v
	 * @return
	 * @author Navy D
	 * @date 20170907215248
	 */
	public int id(int v) {
		validateVertex(v);
		return id[v];
	}

	/**
	 * 返回图g中包含的强联通分量的数量
	 * @return
	 * @author Navy D
	 * @date 20170907215318
	 */
	public int count() {
		return count;
	}
	/**
	 * 检查条件：
	 * 顶点v与w强连通存在v->w,w->v的路径
	 * @param g
	 * @return
	 * @author Navy D
	 * @date 20170918210124
	 */
	private boolean check(Digraph g) {
		TransitiveClosure tc = new TransitiveClosure(g);
		for (int v = 0; v < g.getV(); v++)
			for (int w = 0; w < g.getV(); w++)
				if (stronglyConnected(v, w) != tc.reachable(v, w) && tc.reachable(w, v))
					return false;
		return true;
	}

	private void validateVertex(int v) {
		if (v < 0 || v >= marked.length)
			throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (marked.length-1));
	}

	public static void main(String[] args) {
		Digraph g = new Digraph(new In("../Algs_4/tinyDG.txt"));
		KosarajuSCC scc = new KosarajuSCC(g);

		int m = scc.count();
        System.out.println(m + " strong components");

        // compute list of vertices in each strong component
        @SuppressWarnings("unchecked")
		Queue<Integer>[] components = new Queue[m];
        for (int i = 0; i < m; i++) {
            components[i] = new Queue<Integer>();
        }
        for (int v = 0; v < g.getV(); v++) {
            components[scc.id(v)].enqueue(v);
        }

        // print results
        for (int i = 0; i < m; i++) {
            for (int v : components[i]) {
                System.out.print(v + " ");
            }
            System.out.println();
        }

	}

}
