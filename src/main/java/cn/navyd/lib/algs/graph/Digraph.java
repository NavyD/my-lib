package cn.navyd.lib.algs.graph;

import cn.navyd.lib.algs.util.BagV;
import cn.navyd.lib.algs.util.In;
import cn.navyd.lib.algs.util.ArrayStack;

/**
 * 有向图的数据结构
 * @author Navy D
 * @date 20170916233906
 */
public class Digraph {
	// 顶点总数
	private final int v;
	// 边的总数
	private int e;
	// 有向图顶点指向的顶点集合
	private BagV<Integer>[] adj;

	// 顶点的入度
	private int[] indegree;

	/**
	 * 初始化一个顶点容量为v的空的有向图
	 * @param v
	 */
	@SuppressWarnings("unchecked")
	public Digraph(int v) {
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
	 * 使用已有的有向图初始化一个新的有向图
	 * @param g
	 */
	public Digraph(Digraph g) {
		this(g.getV());
		// 使用栈是为了与g顶点保持一样的顺序，不用栈直接添加也可以，只是遍历时顶点顺序不一样
		ArrayStack<Integer> stack = new ArrayStack<>();
		for (int i = 0; i < g.getV(); i++) {
			for (int w : g.adj(i))
				stack.push(w);
			while (!stack.isEmpty()) {
				addEdge(i, stack.pop());
			}
		}
	}

	/**
	 * 从输入流中初始化一个有向图
	 * 格式：
	 * 11		//顶点总数
	 * 23		//边的总数
	 * 0 3		//表示一条边：起点 终点
	 * ...		//更多边
	 * @param in
	 */
	public Digraph(In in) {
		this(in.readInt());
		int E = in.readInt();
		if (E < 0)
			throw new IllegalArgumentException("number of edges in a Digraph must be nonnegative");
		for (int i = 0; i < E; i++) {
			int v = in.readInt();
			int w = in.readInt();
			addEdge(v, w);
		}
	}

	/**
	 * 返回有向图的顶点总数
	 * @return
	 * @author Navy D
	 * @date 20170916234818
	 */
	public int getV() {
		return v;
	}

	/**
	 * 返回有向图的边总数
	 * @return
	 * @author Navy D
	 * @date 20170916234841
	 */
	public int getE() {
		return e;
	}

	/**
	 * 将顶点v和w连接成一条边添加到图中
	 * 当前支持自环和平行边
	 * @param v
	 * @param w
	 * @author Navy D
	 * @date 20170908205058
	 */
	public void addEdge(int v, int w) {
		validateVertex(v, w);
		//不允许自环
//		if (v == w)
//			return;
		// 不允许平行边
//		if (hasEdge(w, v))
//			return;

		// 有向图只添加一条边
		adj[v].add(w);
		e++;
		// 对应顶点的入度+1
		indegree[w]++;
	}

	/**
	 * 返回顶点v的出度顶点集合
	 * @param v
	 * @return
	 * @author Navy D
	 * @date 20170909123708
	 */
	public Iterable<Integer> adj(int v) {
		validateVertex(v);
		return adj[v];
	}

	/**
	 * 返回图g的反向图
	 * @return
	 * @author Navy D
	 * @date 20170908204936
	 */
	public Digraph reverse() {
		Digraph r = new Digraph(v);
		for (int i = 0; i < v; i++)
			// 反向添加边
			for (int w : adj(i))
				r.addEdge(w, i);
		return r;
	}

	/**
	 * 如果图中含有边v->w，就返回true
	 * @param v
	 * @param w
	 * @return
	 * @author Navy D
	 * @date 20170908204905
	 */
	public boolean hasEdge(int v, int w) {
		validateVertex(v, w);
		for (int s : adj[v])
			if (s == w)
				return true;
		return false;
	}

	/**
	 * 返回顶点的入度
	 * @param v
	 * @return
	 * @author Navy D
	 * @date 20170909122346
	 */
	public int getIndegree(int v) {
		validateVertex(v);
		return indegree[v];
	}

	/**
	 * 返回顶点的出度
	 * @param v
	 * @return
	 * @author Navy D
	 * @date 20170909122402
	 */
	public int getOutdegree(int v) {
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

	public static void main(String[] args) {
		In in = new In("./tinyDG.txt");
		Digraph g = new Digraph(in);
		System.out.println(g);
		for (int v = 0; v < g.getV(); v++) {
			System.out.format("顶点=%-3d出度=%-2d入度=%-2d\n", v, g.getOutdegree(v), g.getIndegree(v));
		}
		System.out.println(g.hasEdge(0, 0));
		Digraph gg = new Digraph(g);
		System.out.println(gg);
	}
}
