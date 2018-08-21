package cn.navyd.lib.algs.graph;

import cn.navyd.lib.algs.util.In;
import cn.navyd.lib.algs.util.ArrayStack;

/**
 * 单点有向路径：<br>
 * 思想：使用深搜找出起点s到v的一条路径<br>
 * 复杂度：与连通起点s到v中所有顶点的出度之和成正比<br>
 * @author Navy D
 * @date 20170906162436
 */
public class DepthFirstDirectedPaths {
	// 顶点是否被深搜标记
	private boolean[] marked;
	// 存储以s为起点的路径
	private int[] edgeTo;
	// 起点s
	private final int s;

	/**
	 * 在有向图中计算起点s到其他顶点的一条有向路径
	 * @param g
	 * @param s
	 */
	public DepthFirstDirectedPaths(Digraph g, int s) {
		marked = new boolean[g.getV()];
		edgeTo = new int[g.getV()];
		validateVertex(s);
		this.s = s;
		dfs(g, s);
	}

	private void dfs(Digraph g, int v) {
		marked[v] = true;
		for (int w : g.adj(v))
			if (!marked[w]) {
				edgeTo[w] = v;
				dfs(g, w);
			}
	}

	/**
	 * 如果图中存在起点s到顶点v的一条路径就返回true
	 * @param v
	 * @return
	 * @author Navy D
	 * @date 20170917231243
	 */
	public boolean hasPathTo(int v) {
		validateVertex(v);
		return marked[v];
	}

	/**
	 * 返回起点s到顶点v的路径.如果不存在这个路径就返回null
	 * @param v
	 * @return
	 * @author Navy D
	 * @date 20170917231335
	 */
	public Iterable<Integer> pathTo(int v) {
		validateVertex(s);
		if (!hasPathTo(v))
			return null;
		ArrayStack<Integer> path = new ArrayStack<>();
		for (int x = v; x != s; x = edgeTo[x])
			path.push(x);
		path.push(s);
		return path;

	}

	private void validateVertex(int v) {
		if (v < 0 || v >= marked.length)
			throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (marked.length - 1));
	}

	public static void main(String[] args) {
		Digraph g = new Digraph(new In("../Algs_4/tinyDG.txt"));
		int s = 0;
		DepthFirstDirectedPaths path = new DepthFirstDirectedPaths(g, s);
		for (int i = 0; i < g.getV(); i++) {
			if (path.hasPathTo(i)) {
				System.out.format("%d to %2d:  ", s, i);
				for (int v : path.pathTo(i)) {
					if (v == s)
						System.out.print(v);
					else
						System.out.print("-" + v);
				}
				System.out.println();
			}
			else
				System.out.format("%d to %2d:  not connected\n", s, i);
		}




	}
}
