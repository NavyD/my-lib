package cn.navyd.lib.algs.graph;

import cn.navyd.lib.algs.util.BagV;
import cn.navyd.lib.algs.util.In;
import cn.navyd.lib.algs.util.ArrayStack;

/**
 * 加权有向图的数据结构
 * @author Navy D
 * @date 20170916151937
 */
public class EdgeWeightedDigraph {
	// 图的顶点总数
	private final int v;
	// 图的边总数
	private int e;
	// 顶点的邻接表如：adj[v]=顶点邻接的所有边
	private BagV<DirectedEdge>[] adj;
	// 顶点的入度 如：indegree[v]=入度
	private int[] indegree;

	/**
	 * 初始化一个顶点容量为v的空图
	 * @param v
	 */
	@SuppressWarnings("unchecked")
	public EdgeWeightedDigraph(int v) {
		if (v < 0)
			throw new IllegalArgumentException("Number of vertices in a Digraph must be nonnegative");
		this.v = v;
		this.e = 0;
		adj = new BagV[v];
		indegree = new int[v];
		for (int i = 0; i < v; i++)
			adj[i] = new BagV<>();
	}

	/**
	 * 从输入流中读取一个图
	 * 格式：
	 * 15			// 顶点总数
	 * 22			// 边总数
	 * 0 5 0.77  	// 表示一条边分别为：起点 终点 权值
	 * ...			// 边
	 * @param in
	 */
	public EdgeWeightedDigraph(In in) {
		this(in.readInt());
		int E = in.readInt();
		if (E < 0)
			throw new IllegalArgumentException("Number of edges must be nonnegative");
		for (int i = 0; i < E; i++) {
			int v = in.readInt();
			int w = in.readInt();
			double weight = in.readDouble();
			addEdge(new DirectedEdge(v, w, weight));
		}
	}

	/**
	 * 从一个图中复制到一个新图
	 * @param g
	 */
	public EdgeWeightedDigraph(EdgeWeightedDigraph g) {
		this(g.getV());
		// 使用栈保持边添加的顺序
		ArrayStack<DirectedEdge> reverse = new ArrayStack<>();
		for (int i = 0; i < g.getV(); i++) {
			for (DirectedEdge edge : g.adj[i])
				reverse.push(edge);
			while (!reverse.isEmpty())
				this.addEdge(reverse.pop());
		}
	}

	/**
	 * 返回图的顶点总数
	 * @return
	 * @author Navy D
	 * @date 20170916155409
	 */
	public int getV() {
		return v;
	}

	/**
	 * 返回图的边的总数
	 * @return
	 * @author Navy D
	 * @date 20170916155438
	 */
	public int getE() {
		return e;
	}

	/**
	 * 在图中添加一条边
	 * @param edge
	 * @author Navy D
	 * @date 20170916155458
	 */
	public void addEdge(DirectedEdge edge) {
		adj[edge.from()].add(edge);
		// 边的终点入度+1
		indegree[edge.to()]++;
		e++;
	}

	/**
	 * 返回从顶点v发出的有向边集合
	 * @param v
	 * @return
	 * @author Navy D
	 * @date 20170916155836
	 */
	public Iterable<DirectedEdge> adj(int v) {
		if (v < 0 || v >= this.v)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (this.v-1));
		return adj[v];
	}

	/**
	 * 返回顶点v的出度
	 * @param v
	 * @return
	 * @author Navy D
	 * @date 20170916160222
	 */
	public int getOutdegree(int v) {
		if (v < 0 || v >= this.v)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (this.v-1));
		return adj[v].size();
	}

	/**
	 * 返回顶点v的入度
	 * @param v
	 * @return
	 * @author Navy D
	 * @date 20170916160235
	 */
	public int getIndegree(int v) {
		if (v < 0 || v >= this.v)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (this.v-1));
		return indegree[v];
	}

	/**
	 * 返回图的所有有向边集合
	 * @return
	 * @author Navy D
	 * @date 20170916160037
	 */
	public Iterable<DirectedEdge> edges() {
		BagV<DirectedEdge> bag = new BagV<>();
		for (int i = 0; i < v; i++)
			for (DirectedEdge edge : adj[i])
				bag.add(edge);
		return bag;
	}

	/**
	 * 返回图的字符串表示
	 * @author Navy D
	 * @date 20170916160706
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(v + " " + e + "\n");
		for (int i = 0; i < v; i++) {
			sb.append(i + ":");
			for (DirectedEdge edge : adj[i])
				sb.append(edge + " ");
			sb.append("\n");
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		In in = new In("./tinyEWD.txt");
		EdgeWeightedDigraph g = new EdgeWeightedDigraph(in);
		System.out.println(g);
		g = new EdgeWeightedDigraph(g);
		System.out.println();
		System.out.println(g);
	}

}
