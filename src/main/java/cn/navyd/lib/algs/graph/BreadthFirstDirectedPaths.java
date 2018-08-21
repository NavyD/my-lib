package cn.navyd.lib.algs.graph;

import cn.navyd.lib.algs.util.In;
import cn.navyd.lib.algs.util.Queue;
import cn.navyd.lib.algs.util.ArrayStack;

/**
 * 单点最短(所含边数最少)有向路径：<br>
 * 思想：使用广搜寻找从s到给定的顶点v的距离起点s的边最少的一条(广搜的特性)<br>
 * 复杂度：最坏情况与v+e成正比
 * @author Navy D
 * @date 20170906164639
 */
public class BreadthFirstDirectedPaths {
	// 顶点是否被深搜标记
	private boolean[] marked;
	// 存储以s为起点的路径
	private int[] edgeTo;
	// 起点s
	private final int s;

	/**
	 * 在有向图中计算从起点s到其他顶点的最短(所含边数)路径
	 * @param g
	 * @param s
	 */
	public BreadthFirstDirectedPaths(Digraph g, int s) {
		marked = new boolean[g.getV()];
		edgeTo = new int[g.getV()];
		validateVertex(s);
		this.s = s;
		bfs(g, s);
	}

	private void bfs(Digraph g, int s) {
		Queue<Integer> queue = new Queue<>();
		queue.enqueue(s);
		marked[s] = true;
		while (!queue.isEmpty()) {
			int v = queue.dequeue();
			for (int w : g.adj(v))
				if (!marked[w]) {
					marked[w] = true;
					edgeTo[w] = v;
					queue.enqueue(w);
				}
		}
	}

	/**
	 * 如果存在起点s到顶点v的一条路径就返回true
	 * @param v
	 * @return
	 * @author Navy D
	 * @date 20170917234117
	 */
	public boolean hasPathTo(int v) {
		return marked[v];
	}

	/**
	 * 返回起点s到顶点v的最短路径。如果不存在这样的路径，就返回null
	 * @param v
	 * @return
	 * @author Navy D
	 * @date 20170917234015
	 */
	public Iterable<Integer> pathTo(int v) {
		if (!hasPathTo(v))
			return null;
		ArrayStack<Integer> path = new ArrayStack<>();
		for (int w = v; w != s; w = edgeTo[w])
			path.push(w);
		path.push(s);
		return path;
	}

	private void validateVertex(int v) {
		if (v < 0 || v > marked.length)
			throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (marked.length-1));
	}

	public static void main(String[] args) {
		Digraph g = new Digraph(new In("../Algs_4/tinyDG.txt"));
		for (int s = 0; s < g.getV(); s++) {
			BreadthFirstDirectedPaths path = new BreadthFirstDirectedPaths(g, s);
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
				} else
					System.out.format("%d to %2d:  not connected\n", s, i);
			}
			System.out.println();
		}

	}
}
