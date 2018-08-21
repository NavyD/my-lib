package cn.navyd.lib.algs.graph;

import cn.navyd.lib.algs.util.In;
import cn.navyd.lib.algs.util.ArrayStack;

/**
 * 无环加权有向图的单点最长路径算法：<br>
 * 与最短路径算法相似，这里只是修改了distTo的初始值为负无穷
 * 并改变relax中不等式的方向
 * @author Navy D
 * @date 20170912180300
 */
public class AcyclicLP {
	// 最长路径的边
	private DirectedEdge[] edgeTo;
	// 最长路径
	private double[] distTo;

	public AcyclicLP(EdgeWeightedDigraph g, int s) {
		edgeTo = new DirectedEdge[g.getV()];
		distTo = new double[g.getV()];

		validateVertex(s);
		// 将每个顶点初始化为负无穷
		for (int v = 0; v < g.getV(); v++)
			distTo[v] = Double.NEGATIVE_INFINITY;
		distTo[s] = 0.0;
		// 使用拓扑排序的顶点顺序作为取到顶点权重最小的边
		Topological top = new TopologicalDFS(g);
		if (!top.hasOrder())
			throw new IllegalArgumentException("Digraph is not acyclic.");
		for (int v : top.order())
			// 放松每个顶点
			relax(g, v);
	}

	private void relax(EdgeWeightedDigraph g, int v) {
		for (DirectedEdge e : g.adj(v)) {
			int w = e.to();
			// 取更大的路径长度
			if (distTo[w] < distTo[v] + e.weight()) {
				distTo[w] = distTo[v] + e.weight();
				edgeTo[w] = e;
			}
		}
	}

	/**
	 * 返回从起点s到顶点v的最长距离
	 * @param v
	 * @return
	 * @author Navy D
	 * @date 20170917113226
	 */
	public double distTo(int v) {
		validateVertex(v);
		return distTo[v];
	}

	/**
	 * 如果图中存在从s到达顶点v的最长路径就返回true
	 * @param v
	 * @return
	 * @author Navy D
	 * @date 20170917113307
	 */
	public boolean hasPathTo(int v) {
		validateVertex(v);
		return distTo[v] > Double.NEGATIVE_INFINITY;

	}

	/**
	 * 返回起点s到顶点v的最长路径集合，如果不存在这个路径就返回null
	 * @param v
	 * @return
	 * @author Navy D
	 * @date 20170917113852
	 */
	public Iterable<DirectedEdge> pathTo(int v) {
		validateVertex(v);
		if (!hasPathTo(v))
			return null;
		ArrayStack<DirectedEdge> path = new ArrayStack<>();
		for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()]) {
			path.push(e);
		}
		return path;
	}

	private void validateVertex(int v) {
		if (v < 0 || v >= distTo.length)
			throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (distTo.length-1));
	}

	public static void main(String[] args) {
		EdgeWeightedDigraph G = new EdgeWeightedDigraph(new In("../Algs_4/tinyEWDAG.txt"));
		int s = 1;
		AcyclicLP lp = new AcyclicLP(G, s);

        for (int v = 0; v < G.getV(); v++) {
            if (lp.hasPathTo(v)) {
                System.out.printf("%d to %d (%.2f)  ", s, v, lp.distTo(v));
                for (DirectedEdge e : lp.pathTo(v)) {
                    System.out.print(e + "   ");
                }
                System.out.println();
            }
            else {
                System.out.printf("%d to %d         no path\n", s, v);
            }
        }
	}
}
