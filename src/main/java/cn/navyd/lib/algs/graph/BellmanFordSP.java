package cn.navyd.lib.algs.graph;

import cn.navyd.lib.algs.util.In;
import cn.navyd.lib.algs.util.Queue;
import cn.navyd.lib.algs.util.ArrayStack;

/**
 * 基于队列的bellman-ford最短路径算法：<br>
 * 该算法允许负权重有向环的存在，但是对于含负权重环只有起点s不可达时有效，若s存在对负权重环可达的路径，最短路径为负无穷<br>
 *
 * 思想：对给定的加权有向图起点s，s对任何负权重环不可达， 将顶点初始化为0，其他元素为 无穷大，放松所有顶点重复v轮（对于负权重环不可达）<br>
 * 使用队列保存顶点的放松的顺序，其中顺序由上一次放松成功的顶点保持，这样放松的成功率更大<br>
 * 在放松顶点时，入队使用标记保持队中不出现重复顶点，一个顶点可能被多次放松（负权重环路径上的顶点）<br>
 * 对于负权重环的队列在v次处理后，队列肯定不为空，即edgeTo中含有这个环，使用这些边构造图找出环<br>
 *<br>
 * 复杂度：最坏情况解决最短路径问题或找到从s可达的负权重环所需的时间与ev成正比，空间与v成正比<br>
 * 平均：时间与e+v成正比
 * @author Navy D
 * @date 20170916110930
 */
public class BellmanFordSP implements ShortestPath {
	// 从起点到某个顶点的路径长度
	private double[] distTo;
	// 最短路径的边
	private DirectedEdge[] edgeTo;
	// 顶点是否在队列中
	private boolean[] onQ;
	// 正在被放松的顶点
	private Queue<Integer> queue;
	// relax()调用次数，用于对负权重环出现时寻找环
	private int cost;
	// edgeTo中是否含有负权重环
	private Iterable<DirectedEdge> cycle;

	/**
	 * 在加权有向图中计算起点s可达的其他顶点的最短路径
	 * @param g
	 * @param s
	 */
	public BellmanFordSP(EdgeWeightedDigraph g, int s) {
		distTo = new double[g.getV()];
		edgeTo = new DirectedEdge[g.getV()];

		validateVertex(s);

		onQ = new boolean[g.getV()];
		queue = new Queue<>();

		for (int i = 0; i < g.getV(); i++)
			distTo[i] = Double.POSITIVE_INFINITY;
		// 初始化顶点s入队列
		distTo[s] = 0.0;
		queue.enqueue(s);
		onQ[s] = true;

		// 当执行v次后若队列不为空，表示一定有负权重环，此时需要找到环后停止循环，如果找到这个环，表示该算法路径无效
		while (!queue.isEmpty() && !hasNegativeCycle()) {
			// 将出队列并放松顶点
			int v = queue.dequeue();
			onQ[v] = false;
			relax(g, v);
		}

		assert check(g, s);
	}

	/**
	 * 放松每个顶点的同时，处理环保证队列中不出现重复顶点和下一个处理改变的w的边
	 * @param g
	 * @param v
	 * @author Navy D
	 * @date 20170916092046
	 */
	private void relax(EdgeWeightedDigraph g, int v) {
		for (DirectedEdge e : g.adj(v)) {
			int w = e.to();
			// 如果新的路径较短就更新
			if (distTo[w] > distTo[v] + e.weight()) {
				distTo[w] = distTo[v] + e.weight();
				edgeTo[w] = e;
				// 保证队列中不出现重复顶点，可能主要用于对于负权重边和环
				if (!onQ[w]) {
					// 使用上一个改变的边的顶点在下一轮处理
					queue.enqueue(w);
					onQ[w] = true;
				}
			}
			// 放松v次后，如果存在s可达的负权重环，那么队列永远不可能空
			if (cost++ % g.getV() == 0) {
				findNegativeCycle();
				// 如果找到一个负权重环就退出当前顶点的有向边遍历
				if (hasNegativeCycle())
					return;
			}
		}
	}

	/**
	 * 如果图中存在一个从起点s可达的负权重环，就返回true
	 * @return
	 * @author Navy D
	 * @date 20170917141039
	 */
	public boolean hasNegativeCycle() {
		return cycle != null;
	}

	/**
	 * 如果图中存在一个从起点s可达的负权重环，就返回这个环的路径。否则返回null
	 * @return
	 * @author Navy D
	 * @date 20170917140932
	 */
	public Iterable<DirectedEdge> negativeCycle() {
		return cycle;
	}

	/**
	 * 寻找一个负权重环，由于算法特性，只有负权重环会出现在最短路径edgeTo中，
	 * 所以使用edgeTo中的边构造一个图，在图中查找环就行了
	 * @author Navy D
	 * @date 20170917140508
	 */
	private void findNegativeCycle() {
		int v = edgeTo.length;
		EdgeWeightedDigraph spt = new EdgeWeightedDigraph(v);
		for (int i = 0; i < v; i++)
			if (edgeTo[i] != null)
				spt.addEdge(edgeTo[i]);
		EdgeWeightedDirectedCycle finder = new EdgeWeightedDirectedCycle(spt);
		cycle = finder.cycle();
	}

	/**
	 * 返回图中起点s到顶点v的最短路径的权值，如果s可达一个负权重环就抛出异常
	 * @author Navy D
	 * @date 20170917142028
	 */
	@Override
	public double distTo(int v) {
		validateVertex(v);
		 if (hasNegativeCycle())
	            throw new UnsupportedOperationException("Negative cost cycle exists");
		return distTo[v];
	}

	/**
	 * 如果图中存在从s到顶点v的最短路径就返回true
	 * @author Navy D
	 * @date 20170917142156
	 */
	@Override
	public boolean hasPathTo(int v) {
		validateVertex(v);
		return distTo[v] < Double.POSITIVE_INFINITY;
	}

	/**
	 * 返回图中从起点s到顶点v的最短路径。如果s可达负权重环就抛出异常，如果s不可达顶点v就返回null
	 * @author Navy D
	 * @date 20170917142237
	 */
	@Override
	public Iterable<DirectedEdge> pathTo(int v) {
		validateVertex(v);
		if (hasNegativeCycle())
			throw new UnsupportedOperationException("Negative cost cycle exists");
		if (!hasPathTo(v))
			return null;
		ArrayStack<DirectedEdge> path = new ArrayStack<DirectedEdge>();
		for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()]) {
			path.push(e);
		}
		return path;
	}

	/**
	 * 检查条件：
	 * 1.检查负权重环
	 * 2.检查顶点的初始化
	 * 3.检查所有顶点应满足的最优性条件
	 * 4.检查最短路径权值是否一致
	 * @param g
	 * @param s
	 * @return
	 * @author Navy D
	 * @date 20170917144707
	 */
	private boolean check(EdgeWeightedDigraph g, int s) {
		// 如果存在负权重环
		if (hasNegativeCycle()) {
			double weight = .0;
			for (DirectedEdge e : negativeCycle())
				weight += e.weight();
			// 如果负权重环的总权重不小于0就是错误的
			if (weight >= 0) {
				System.err.println("error: weight of negative cycle = " + weight);
				return false;
			}
		} else {
			//检查起点初始化后是否被更改
			if (distTo[s] != 0.0 || edgeTo[s] != null) {
                System.err.println("distanceTo[s] and edgeTo[s] inconsistent");
                return false;
            }

			// 验证起点s不可达顶点初始化后是否被更改
			for (int v = 0; v < g.getV(); v++) {
				if (v == s)
					continue;
				// 正常edgeTo[v]=null表示s不可达顶点，且初始化为正无穷
				if (edgeTo[v] == null && distTo[v] != Double.POSITIVE_INFINITY) {
					System.err.println("distTo[] and edgeTo[] inconsistent");
					return false;
				}
			}

			// 检查图中所有顶点是否满足了最优条件e = v->w distTo[w] <= distTo[v] + e.weight()
			for (int v = 0; v < g.getV(); v++) {
				for (DirectedEdge e : g.adj(v)) {
					int w = e.to();
					if (distTo[v] + e.weight() < distTo[w]) {
						System.err.println("edge " + e + " not relaxed");
						return false;
					}
				}
			}

			// 检查图中最短路径顶端是否满足到distTo[w] == distTo[v] + e.weight()
			for (int w = 0; w < g.getV(); w++) {
				if (edgeTo[w] == null)
					continue;
				DirectedEdge e = edgeTo[w];
				int v = e.from();
				if (w != e.to())
					return false;
				if (Math.abs(distTo[w] - (distTo[v] + e.weight())) > 1E-12) {
					// if (distTo[v] + e.weight() != distTo[w]) {
					System.err.println("edge " + e + " on shortest path not tight");
					return false;
				}
			}
		}
		return true;
	}

	private void validateVertex(int v) {
		if (v < 0 || v >= distTo.length)
			throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (distTo.length - 1));
	}

	public static void main(String[] args) {
		int s = 1;
		EdgeWeightedDigraph G = new EdgeWeightedDigraph(new In("./tinyEWDn.txt"));

		BellmanFordSP sp = new BellmanFordSP(G, s);

		// print negative cycle
		if (sp.hasNegativeCycle()) {
			for (DirectedEdge e : sp.negativeCycle())
				System.out.println(e);
		}

		// print shortest paths
		else {
			for (int v = 0; v < G.getV(); v++) {
				if (sp.hasPathTo(v)) {
					System.out.printf("%d to %d (%5.2f)  ", s, v, sp.distTo(v));
					for (DirectedEdge e : sp.pathTo(v)) {
						System.out.print(e + "   ");
					}
					System.out.println();
				} else {
					System.out.printf("%d to %d           no path\n", s, v);
				}
			}
		}

	}

}
