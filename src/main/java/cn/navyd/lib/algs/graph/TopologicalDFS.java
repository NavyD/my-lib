package cn.navyd.lib.algs.graph;

import cn.navyd.lib.algs.util.In;

/**
 * 基于深搜的拓扑排序：<br>
 * 思想：使用深搜的逆后序排列的拓扑排序<br>
 * 注意：<br>
 * 该类提供有向图和加权有向图的拓扑排序<br>成功拓扑排序的图不含有环<br>拓扑排序的结果不唯一<br>该类当前不允许伪拓扑排序<br>
 * 如果需要伪拓扑排序，可以使用DepthFirstOrder类<br>
 * 复杂度：拓扑排序所需的时间和v+e成正比(两次深搜都访问了所有的顶点和边)
 * @author Navy D
 * @date 20170906210655
 */
public class TopologicalDFS implements Topological {
	// 顶点的拓扑排序
	private Iterable<Integer> order;

	public TopologicalDFS(Digraph g) {
		DirectedCycle cycleFinder = new DirectedCycle(g);
		// 判断是否是无环图
		if (!cycleFinder.hasCycle())
			// 使用逆后序
			order = new DepthFirstOrder(g).reversePost();
	}

	public TopologicalDFS(EdgeWeightedDigraph g) {
		EdgeWeightedDirectedCycle cycleFinder = new EdgeWeightedDirectedCycle(g);
		// 判断是否是无环图
		if (!cycleFinder.hasCycle())
			// 使用逆后序
			order = new DepthFirstOrder(g).reversePost();
	}

	/**
	 * 如果图是无环有向图就返回逆后序遍历，否则返回null
	 * @return
	 * @author Navy D
	 * @date 20170906210956
	 */
	public Iterable<Integer> order() {
		return order;
	}

	/**
	 * 如果是无环有向图就返回true
	 * @return
	 * @author Navy D
	 * @date 20170917000050
	 */
	public boolean hasOrder() {
		return order != null;
	}

	public static void main(String[] args) {
		In in = new In("./tinyDAG.txt");
		Digraph g = new Digraph(in);
		Topological tp = new TopologicalDFS(g);
		if (tp.hasOrder())
			for (int v : tp.order())
				System.out.print(v + " ");
		System.out.println();
		EdgeWeightedDigraph ewd = new EdgeWeightedDigraph(new In("./tinyEWD.txt"));
		tp = new TopologicalDFS(ewd);
		if (tp.hasOrder())
			for (int v : tp.order())
				System.out.print(v + " ");

	}
}
