package cn.navyd.lib.algs.graph;

import java.util.Iterator;

import cn.navyd.lib.algs.util.BagV;
import cn.navyd.lib.algs.util.In;
import cn.navyd.lib.algs.util.ArrayStack;

/**
 * 无向图的数据结构
 * @author Navy D
 * @date 20170919094745
 */
public class Graph {
	// 顶点数目
	private final int v;
	//边的数目
	private int e;
	// 顶点对应的邻接表
	private BagV<Integer>[] adj;


	/**
	 * 构造一个顶点容量为v的空表
	 * @param v
	 */
	@SuppressWarnings("unchecked")
	public Graph(int v) {
		if (v < 0)
			throw new IllegalArgumentException("Number of vertices must be nonnegative");
		this.v = v;
		this.e = 0;
		adj = new BagV[v];
		for (int i = 0; i < v; i++)
			adj[i] = new BagV<>();
	}

	/**
	 * 从输入流中读取一个无向图
	 * 格式：
	 * 11		//顶点总数
	 * 20		//边总数
	 * 0 3		//边的两个顶点
	 * ...
	 * @param in
	 */
	public Graph(In in) {
		this(in.readInt());
		int E = in.readInt();
		if (E < 0)
			throw new IllegalArgumentException("number of edges in a Graph must be nonnegative");
		for (int i = 0; i < E; i++) {
			int v = in.readInt();
			int w = in.readInt();
			addEdge(v, w);
		}
	}

	/**
	 * 从一个无向图中复制为一个新的无向图
	 * @param g
	 */
	public Graph(Graph g) {
		v = g.getV();
		e = g.getE();
		// 由于图的bag是后进先出，需要使用栈转换
		ArrayStack<Integer> stack = new ArrayStack<>();
		for (int i = 0; i < v; i++) {
			for (int w : g.adj(i))
				stack.push(w);
			while (!stack.isEmpty())
				adj[i].add(stack.pop());
		}
	}

	/**
	 * 返回图的字符串表示
	 * @author Navy D
	 * @date 20170919100328
	 */
	public String toString() {
		String s = v + " vertices, " + e + " edges\n";
		for (int i = 0; i < v; i++) {
			s += i + ": ";
			for (int w : this.adj(i))
				s += w + " ";
			s += "\n";
		}
		return s;
	}

	/**
	 * 返回图的顶点总数
	 * @return
	 * @author Navy D
	 * @date 20170919100409
	 */
	public int getV() {
		return v;
	}

	/**
	 * 返回图的边总数
	 * @return
	 * @author Navy D
	 * @date 20170919100427
	 */
	public int getE() {
		return e;
	}

	//向图中添加一条边v-w
	/**
	 * 添加一条边
	 * @param v
	 * @param w
	 * @author Navy D
	 * @date 20170919100441
	 */
	public void addEdge(int v, int w) {
		validateVertex(v, w);
		// 不允许自环
//		if (v == w)
//			return ;
//		// 不允许平行边
//		if (hasEdge(w, v))
//			return ;
		//注意：bag采用后进先出栈的方式
		adj[v].add(w);// 将w添加到v的链表中
		adj[w].add(v);// 将v添加到w的链表中
		e++;
	}

	//和v相邻的所有顶点
	public Iterable<Integer> adj(int v) {
		validateVertex(v);
		return adj[v];
	}

	/**
	 * 如果顶点v的邻接点包含w就返回true
	 * @author Navy D
	 * @date 20170903201301
	 */
	public boolean hasEdge(int v, int w) {
		validateVertex(v, w);
		for (int i : adj[v])
			if (i == w)
				return true;
		return false;
	}

	/**
	 * 返回顶点v的度数
	 * @param v
	 * @return
	 * @author Navy D
	 * @date 20170919100459
	 */
	public int degree(int v) {
		validateVertex(v);
		return adj[v].size();
	}

	/**
	 * 检查顶点是否越界
	 * @param args
	 * @author Navy D
	 * @date 20170908210520
	 */
	private void validateVertex(int... args) {
		for (int i : args)
			if (i < 0 || i >= v)
				throw new IllegalArgumentException();
	}




	public static int degree(Graph g, int v) {
		int degree = 0;
		Iterator<Integer> it = g.adj(v).iterator();
		while (it.hasNext()) {
			degree++;
			it.next();
		}
		return degree;
	}

	public static int maxDegree(Graph g) {
		int max = 0;
		for (int getV = 0; getV < g.getV(); getV++) {
			if (degree(g, getV) > max)
				max = degree(g, getV);
		}
		return max;
	}

	public static double avgDegree(Graph g) {
		return 2.0 * g.getE() / g.getV();
	}

	public static int numberOfSelfLoops(Graph g) {
		int count = 0;
		for (int getV= 0; getV < g.getV(); getV++)
			for (int w : g.adj(getV))
				if (getV == w)
					count++;
		return count/2;
	}

	public static void main(String[] args) {
		In in = new In("./tinyG.txt");
		Graph g = new Graph(in);
		System.out.println(g);
//		DepthFirstPaths dfp = new DepthFirstPaths(g, 0);
//		BreadthFirstPaths bfp = new BreadthFirstPaths(g, 0);
//		for (int i : dfp.pathTo(5))
//			System.out.print(i + " ");
//		System.out.println();
//		for (int i : bfp.pathTo(5))
//			System.out.print(i + " ");
	}

}
