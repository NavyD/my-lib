package cn.navyd.lib.algs.graph;

import cn.navyd.lib.algs.util.In;
import cn.navyd.lib.algs.util.Queue;

/**
 * 基于队列的拓扑排序：<br>
 * 思想：
 * 在图中找顶点入度为0的顶点，不断删除顶点入度为0的有向边<br>
 * 1.从有向图中选择入度为0的顶点入队<br>
 * 2.从图中删去该顶点，并删除其发出的有向边，如果有顶点入度为0就入队<br>
 * 3.重复直至队列为空<br>
 * 注意：<br>
 * 该类提供有向图和加权有向图的拓扑排序<br>成功拓扑排序的图不含有环<br>拓扑排序的结果不唯一<br>该类当前不允许伪拓扑排序<br>
 * 复杂度：所需时间与e+v成正比
 * @author Navy D
 * @date 20170909135641
 */
public class TopologicalQueue implements Topological {
	// 起点保存队列
	private Queue<Integer> order;

	/**
	 * 确定一个有向图是否可以拓扑排序，如果是，就找到其中一个排序
	 * @param g
	 */
	public TopologicalQueue(Digraph g) {
		order = new Queue<>();

		int[] indegree = new int[g.getV()];
		for (int v = 0; v < g.getV(); v++)
			indegree[v] = g.getIndegree(v);

		Queue<Integer> queue = new Queue<>();
		// 将原图中起点入队
		for (int v = 0; v < indegree.length; v++)
			if (indegree[v] == 0)
				queue.enqueue(v);
		// 将图中入度=0的顶点删除排序
		while (!queue.isEmpty()) {
			// 删除起点
			int v = queue.dequeue();
			// 输出
			order.enqueue(v);
			//将起点指向的顶点的入度-1
			for (int w : g.adj(v)) {
				indegree[w]--;
				// 如果有顶点入度=0就入队
				if (indegree[w] == 0)
					queue.enqueue(w);
			}
		}

		// 验证排序结果和图顶点数量是否一致，如果不一致说明含有有向环没有被加入队列，拓扑排序不能含有环，排序失败
		if (order.size() != g.getV())
			order = null;
	}

	/**
	 * 确定一个加权有向图是否可以拓扑排序，如果是，就找到一个排序
	 * @param g
	 */
	public TopologicalQueue(EdgeWeightedDigraph g) {
		order = new Queue<>();
		// 获取每个顶点的入度
		int[] indegree = new int[g.getV()];
		for (int v = 0; v < g.getV(); v++)
			indegree[v] = g.getIndegree(v);
		// 对入度为0的顶点操作
		Queue<Integer> queue = new Queue<>();
		// 将原图中起点入队
		for (int v = 0; v < indegree.length; v++)
			if (indegree[v] == 0)
				queue.enqueue(v);

		while (!queue.isEmpty()) {
			int v = queue.dequeue();
			order.enqueue(v);
			// 遍历顶点v的指向边
			for (DirectedEdge e : g.adj(v)) {
				// 删除顶点v入度-1
				indegree[e.to()]--;
				if (indegree[e.to()] == 0)
					queue.enqueue(e.to());
			}
		}

		// 原图中是否含有环，如果是拓扑排序失败
		if (order.size() != g.getV())
			order = null;
	}

	/**
	 * 如果图可以拓扑排序就返回排序结果，否则就返回null
	 * @return
	 * @author Navy D
	 * @date 20170917105629
	 */
	public Iterable<Integer> order() {
		return order;
	}

	/**
	 * 如果图可以拓扑排序就返回true
	 * @return
	 * @author Navy D
	 * @date 20170917105532
	 */
	public boolean hasOrder() {
		return order != null;
	}

	public static void main(String[] args) {
		In in = new In("./tinyDAG.txt");
		Digraph g = new Digraph(in);
		TopologicalQueue tp = new TopologicalQueue(g);
		if (tp.hasOrder())
			for (int v : tp.order())
				System.out.print(v + " ");
		System.out.println();
		EdgeWeightedDigraph ewd = new EdgeWeightedDigraph(new In("./tinyEWD.txt"));
		tp = new TopologicalQueue(ewd);
		if (tp.hasOrder())
			for (int v : tp.order())
				System.out.print(v + " ");
	}


}
