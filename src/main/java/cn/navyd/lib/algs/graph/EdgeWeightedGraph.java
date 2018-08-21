package cn.navyd.lib.algs.graph;

import cn.navyd.lib.algs.util.BagV;
import cn.navyd.lib.algs.util.In;
import cn.navyd.lib.algs.util.ArrayStack;

/**
 * 加权无向图的数据类型
 * @author Navy D
 * @date 20170917200140
 */
public class EdgeWeightedGraph {
	// 顶点总数
	private final int v;
	// 边的总数
	private int e;
	// 邻接表
	private BagV<Edge>[] adj;

	/**
	 * 初始化一个顶点容量为v边为0的空图
	 * @param v
	 */
	@SuppressWarnings("unchecked")
	public EdgeWeightedGraph(int v) {
		if (v < 0)
			throw new IllegalArgumentException("Number of vertices must be nonnegative");
		this.v = v;
		this.e = 0;
		this.adj = new BagV[v];
		for (int i = 0; i < v; i++)
			adj[i] = new BagV<>();
	}

	/**
	 * 从输入流中读取一个图
	 * 格式：
	 * 15			// 顶点总数
	 * 22			// 边总数
	 * 0 5 		  	// 表示一条边分别为：起点 终点
	 * ...			// 边
	 * @param in
	 */
	public EdgeWeightedGraph(In in) {
		this(in.readInt());
		int E = in.readInt();
		if (E < 0)
			throw new IllegalArgumentException("Number of edges must be nonnegative");
		for (int i = 0; i < E; i++) {
			int v = in.readInt();
			int w = in.readInt();
			validateVertex(v, w);
			double weight = in.readDouble();
			Edge e = new Edge(v, w, weight);
			addEdge(e);
		}
	}

	/**
	 * 从一个加权无向图复制初始化一个新图
	 * @param g
	 */
	public EdgeWeightedGraph(EdgeWeightedGraph g) {
		this(g.getV());
		ArrayStack<Edge> stack = new ArrayStack<>();
		for (int i = 0; i < g.getV(); i++) {
			for (Edge e : g.adj(i))
				stack.push(e);
			while (!stack.isEmpty())
				addEdge(stack.pop());
		}
	}

	/**
	 * 返回图的顶点总数
	 * @return
	 * @author Navy D
	 * @date 20170917202240
	 */
	public int getV() {
		return v;
	}

	/**
	 * 返回图的边总数
	 * @return
	 * @author Navy D
	 * @date 20170917202253
	 */
	public int getE() {
		return e;
	}

	/**
	 * 添加一条边
	 * @param edge
	 * @author Navy D
	 * @date 20170917202306
	 */
	public void addEdge(Edge edge) {
		int v = edge.either(), w = edge.other(v);
		adj[v].add(edge);
		adj[w].add(edge);
		e++;
	}

	/**
	 * 返回顶点v的邻接表
	 * @param v
	 * @return
	 * @author Navy D
	 * @date 20170917202323
	 */
	public Iterable<Edge> adj(int v) {
		validateVertex(v);
		return adj[v];
	}

	/**
	 * 返回顶点v的度数
	 * @param v
	 * @return
	 * @author Navy D
	 * @date 20170917202355
	 */
	public int getDegree(int v) {
		validateVertex(v);
		return adj[v].size();
	}

	/**
	 * 返回加权无向图中的所有边 支持环
	 *
	 * @return
	 * @author Navy D
	 * @date 20170909182916
	 */
	public Iterable<Edge> edges() {
		BagV<Edge> bag = new BagV<>();
		for (int i = 0; i < v; i++) {
			int selfLoops = 0;
			for (Edge edge : adj[i]) {
				// 由于无向图一条边会添加到两个顶点上，用边上的两个顶点作比较，一条边就只会添加一次
				if (edge.other(i) > i)
					bag.add(edge);
				// 允许自环
				else if (edge.other(i) == i) {
					// addedge操作中添加一个环会使一个bag钟加入两个v，这样只选择一个v添加
					if ((selfLoops & 1) == 0)
						bag.add(edge);
					selfLoops++;
				}
			}
		}
		return bag;
	}

	/**
	 * 返回图的字符串表示
	 * @author Navy D
	 * @date 20170917202955
	 */
	public String toString() {
		  StringBuilder s = new StringBuilder();
	        s.append(v + " " + e + "\n");
	        for (int i = 0; i < v; i++) {
	            s.append(i + ": ");
	            for (Edge e : adj[i]) {
	                s.append(e + "  ");
	            }
	            s.append("\n");
	        }
	        return s.toString();
	}

	private void  validateVertex(int... args) {
		for (int i : args)
			if (i < 0 || i >= v)
				throw new IllegalArgumentException("vertex " + i + " is not between 0 and " + (v-1));
	}

	public static void main(String[] args) {
		EdgeWeightedGraph g = new EdgeWeightedGraph(new In("../Algs_4/tinyEWG.txt"));
		System.out.println(g);

	}



}
