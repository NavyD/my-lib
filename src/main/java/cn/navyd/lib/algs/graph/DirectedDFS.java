package cn.navyd.lib.algs.graph;

import cn.navyd.lib.algs.util.In;

/**
 * 有向图的可达性：<br>
 * 思想：在有向图中使用深搜寻找连通起点s的所有顶点<br>
 * 复杂度：深搜标记与起点s能连通的顶点的出度之和成正比<br>
 * 注意：<br>该类提供单起点可达与多起点可达的搜索
 * @author Navy D
 * @date 20170905210800
 */
public class DirectedDFS {
	// 标记已经搜索过的顶点
	private boolean[] marked;
	// 起点可达的顶点数
	private int count;

	/**
	 * 在图中找到从s可达的所有顶点
	 * @param g
	 * @param s
	 */
	public DirectedDFS(Digraph g, int s) {
		marked = new boolean[g.getV()];
		validateVertex(s);
		dfs(g, s);
	}
	/**
	 * 在图中找到从sources集合中的顶点可达的所有顶点
	 * @param g
	 * @param sources
	 */
	public DirectedDFS(Digraph g, Iterable<Integer> sources) {
		marked = new boolean[g.getV()];
		validateVertices(sources);
		for (int s : sources)
			if (!marked[s])
				dfs(g, s);
	}

	private void dfs(Digraph g, int v) {
		count++;
		marked[v] = true;
		for (int w : g.adj(v))
			if (!marked[w])
				dfs(g, w);
	}

	/**
	 * 如果任意起点能到达顶点v就返回true
	 * @param v
	 * @return
	 * @author Navy D
	 * @date 20170917224843
	 */
	public boolean marked(int v) {
		return marked[v];
	}

	/**
	 * 返回所有起点可达的顶点数
	 * @return
	 * @author Navy D
	 * @date 20170917224810
	 */
	public int count() {
		return count;
	}

	private void validateVertices(Iterable<Integer> vertices) {
		if (vertices == null) {
			throw new IllegalArgumentException("argument is null");
		}
		int V = marked.length;
		for (int v : vertices) {
			if (v < 0 || v >= V) {
				throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V - 1));
			}
		}
	}

	private void validateVertex(int v) {
		if (v < 0 || v >= marked.length)
			throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (marked.length - 1));
	}


	public static void main(String[] args) {
		Digraph g = new Digraph(new In("../Algs_4/tinyDG.txt"));
		int s = 0;
		DirectedDFS d = new DirectedDFS(g, s);

		for (int i = 0; i < g.getV(); i++)
			System.out.format("%-2d -> %2d %s\n", s, i, d.marked(i));


	}
}
