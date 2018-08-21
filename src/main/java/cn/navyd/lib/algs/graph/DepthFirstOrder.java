package cn.navyd.lib.algs.graph;

import cn.navyd.lib.algs.util.In;
import cn.navyd.lib.algs.util.Queue;
import cn.navyd.lib.algs.util.ArrayStack;

/**
 * 深搜排序：
 * 提供有向图和加权有向图的排序
 * 1.前序2.后序3.逆后序
 * @author Navy D
 * @date 20170906205240
 */
public class DepthFirstOrder {
	private boolean[] marked;
	// 前序排列
	private Queue<Integer> pre;
	// 后序排列
	private Queue<Integer> post;
	// 逆后序排列
	private ArrayStack<Integer> reversePost;

	/**
	 * 确定一个有向图的深搜排序
	 * @param g
	 */
	public DepthFirstOrder(Digraph g) {
		pre = new Queue<>();
		post = new Queue<>();
		reversePost = new ArrayStack<>();
		marked = new boolean[g.getV()];

		for (int v = 0; v < g.getV(); v++)
			if (!marked[v])
				dfs(g, v);
	}

	/**
	 * 确定一个加权有向图的深搜排序
	 * @param g
	 */
	public DepthFirstOrder(EdgeWeightedDigraph g) {
		pre = new Queue<>();
		post = new Queue<>();
		reversePost = new ArrayStack<>();
		marked = new boolean[g.getV()];

		for (int v = 0; v < g.getV(); v++)
			if (!marked[v])
				dfs(g, v);
	}

	/**
	 * 对有向图一个顶点深搜得到排序
	 * @param g
	 * @param v
	 * @author Navy D
	 * @date 20170916233025
	 */
	private void dfs(Digraph g, int v) {
		// 递归之前加入队列
		pre.enqueue(v);

		marked[v] = true;
		for (int w : g.adj(v))
			if (!marked[w])
				dfs(g, w);
		// 递归之后加入队列
		post.enqueue(v);
		// 递归之后压入栈
		reversePost.push(v);
	}

	/**
	 * 对加权有向图一个顶点深搜得到排序
	 * @param g
	 * @param v
	 * @author Navy D
	 * @date 20170916233156
	 */
	private void dfs(EdgeWeightedDigraph g, int v) {
		// 先序
		pre.enqueue(v);

		marked[v] = true;
		for (DirectedEdge e : g.adj(v))
			if (!marked[e.to()])
				dfs(g, e.to());
		// 后序
		post.enqueue(v);
		//逆后序
		reversePost.push(v);
	}

	/**
	 * 返回图的先序
	 * @return
	 * @author Navy D
	 * @date 20170916233236
	 */
	public Iterable<Integer> pre() {
		return pre;
	}

	/**
	 * 返回图的后序
	 * @return
	 * @author Navy D
	 * @date 20170916233252
	 */
	public Iterable<Integer> post() {
		return post;
	}

	/**
	 * 返回图的逆后序
	 * @return
	 * @author Navy D
	 * @date 20170916233305
	 */
	public Iterable<Integer> reversePost() {
		return reversePost;
	}

	public static void main(String[] args) {
		Digraph dg = new Digraph(new In("./tinyDG.txt"));
		EdgeWeightedDigraph ewdg = new EdgeWeightedDigraph(new In("./tinyEWD.txt"));
		DepthFirstOrder order = new DepthFirstOrder(dg);
		for (int v : order.reversePost())
			System.out.print( v + " ");
		System.out.println();

		order = new DepthFirstOrder(ewdg);
		for (int v : order.reversePost())
			System.out.print( v + " ");
		System.out.println();

	}

}
