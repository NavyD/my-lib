package cn.navyd.lib.algs.graph;

import cn.navyd.lib.algs.util.In;
import cn.navyd.lib.algs.util.ArrayStack;

/**
 * 加权有向图是否含有环：<br>
 * 思想：
 * 与无权有向图查找环算法类似，对于环的存在，深搜在栈中维护一个有向路径
 * 一旦在栈中找到一个边的终点w也在栈中时，就表示找到一个环<br>
 *
 * 该类只寻找一个有向环，对于含多个环的图是能找到任意的一个环
 * @author Navy D
 * @date 20170916221224
 */
public class EdgeWeightedDirectedCycle {
	// 将搜索过的顶点标记
	private boolean[] marked;
	// 保存每条边所属的路径终点索引 如：e = v->w  edgeTo[e.to()]=e
	private DirectedEdge[] edgeTo;
	// 标记递归栈上的顶点如：onStack[e.to()]=true表示e.to这个顶点在深搜中没有退出
	private boolean[] onStack;
	// 保存环的路径 如果没有环就为null
	private ArrayStack<DirectedEdge> cycle;

	/**
	 * 确定一个加权有向图是否含有环，如果是，就找到一个环
	 * @param g
	 */
	public EdgeWeightedDirectedCycle(EdgeWeightedDigraph g) {
		marked = new boolean[g.getV()];
		onStack = new boolean[g.getV()];
		edgeTo = new DirectedEdge[g.getV()];
		// 要深搜所有顶点
		for (int v = 0; v < g.getV(); v++)
			if (!marked[v])
				dfs(g, v);

		assert check();
	}

	/**
	 * 使用深搜和栈寻找一个环，找到就完成
	 * @param g
	 * @param v
	 * @author Navy D
	 * @date 20170916211956
	 */
	private void dfs(EdgeWeightedDigraph g, int v) {
		// 将当前栈中顶点标记
		onStack[v] = true;
		marked[v] = true;
		// 遍历顶点v的邻接表边
		for (DirectedEdge edge : g.adj(v)) {
			int w = edge.to();
			// 如果已经找到有向环就退出
			if (hasCycle())
				return;
			// 如果没有标记搜索就递归
			if (!marked[w]) {
				edgeTo[w] = edge;
				dfs(g, w);
			}
			// 如果已经搜索过且顶点还在栈中表示遇到一个有向环
			else if (onStack[w]) {
				cycle = new ArrayStack<>();

//				DirectedEdge f = edge;
//				while (f.from() != w) {
//					cycle.push(f);
//					f = edgeTo[f.from()];
//				}
//				cycle.push(f);

				// 从后往前遍历这个有向环
				DirectedEdge de;
				for (de = edge; de.from() != w; de = edgeTo[de.from()])
					cycle.push(de);
				// 保存起点是w开始的边(de.from不保存最开始的环边)
				cycle.push(de);
				return;
			}
		}
		// 顶点完成遍历后退出栈
		onStack[v] = false;

	}

	/**
	 * 如果加权有向图含有一个环就返回true
	 * @return
	 * @author Navy D
	 * @date 20170916212135
	 */
	public boolean hasCycle() {
		return cycle != null;
	}

	/**
	 * 如果图含有环就返回环的路径否则返回null
	 * @return
	 * @author Navy D
	 * @date 20170916212243
	 */
	public Iterable<DirectedEdge> cycle() {
		return cycle;
	}

	/**
	 * 验证当加权有向图中含有环时环的存在是否合理
	 * @return
	 * @author Navy D
	 * @date 20170916213420
	 */
	private boolean check() {
		// 如果含有环能才验证
		if (hasCycle()) {
			// 环开始的边
			DirectedEdge first = null,
					// 环中每一条边
					last = null;
			// 遍历环
			for (DirectedEdge e : cycle()) {
				if (first == null)
					first = e;
				// 上一个边last的终点与当前边e的起点是否相等
				if (last != null) {
					if (last.to() != e.from()) {
						System.err.printf("cycle edges %s and %s not incident\n", last, e);
						return false;
					}
				}
				// 保存上一条边
				last = e;
			}
			// 比较环中最后一条边的终点与第一条边的起点是否相等
			if (last.to() != first.from()) {
				System.err.printf("cycle edges %s and %s not incident\n", last, first);
				return false;
			}
		}
		return true;
	}


	public static void main(String[] args) {
		In in = new In("./tinyEWD.txt");
		EdgeWeightedDigraph g = new EdgeWeightedDigraph(in);
		EdgeWeightedDirectedCycle cycle = new EdgeWeightedDirectedCycle(g);
		System.out.println(g);
		if (cycle.hasCycle()) {
			for (DirectedEdge e : cycle.cycle())
				System.out.print(e + " ");
			System.out.println();
		} else
			System.out.println("No directed cycle");
	}
}
