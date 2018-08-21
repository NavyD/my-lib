package cn.navyd.lib.algs.graph;

import cn.navyd.lib.algs.util.In;
import cn.navyd.lib.algs.util.ArrayStack;

/**
 * 无环加权有向图的最短路径算法：<br>
 * 思想：按照拓扑排序的顺序放松每个顶点。因为拓扑顺序的顶点w除了之前的顶点v是不会有顶点指向w的，
 * 顶点w除了树中的路径v指向，没有树外的顶点指向边了，一旦顶点w加入最短路径中，路径不会再变化。<br>
 * 复杂度：所需时间为e+v
 * @author Navy D
 * @date 20170912172917
 */
public class AcyclicSP implements ShortestPath {
	// 最短路径的存储数组
	private DirectedEdge[] edgeTo;
	// 最短路径权重edgeTo[v] =  s->v 最短路径权值
	private double[] distTo;

	/**
	 * 在一个无环有向图计算从起点s开始的最短路径
	 * @param g
	 * @param s
	 */
	public AcyclicSP(EdgeWeightedDigraph g, int s) {
		edgeTo = new DirectedEdge[g.getV()];
		distTo = new double[g.getV()];

		validateVertex(s);
		// 将所有顶点距离初始化最大
		for (int v = 0; v < g.getV(); v++)
			distTo[v] = Double.POSITIVE_INFINITY;
		// 初始化起点s
		distTo[s] = 0.0;
		// 使用拓扑排序的顶点顺序作为取到顶点权重最小的边
		Topological top = new TopologicalDFS(g);
		// 如果图g含有环
		if (!top.hasOrder())
			throw new IllegalArgumentException("Digraph is not acyclic.");
		for (int v : top.order())
			// 放松每个顶点
			relax(g, v);
	}

	/**
	 * 顶点的松弛：
	 * 对于给定顶点的所有边，检查每条边v-w的最短路径是否是当前路径s-v-w最短，如果是则更新路径
	 * @param g
	 * @param v
	 * @author Navy D
	 * @date 20170912172541
	 */
	private void relax(EdgeWeightedDigraph g, int v) {
		for (DirectedEdge e : g.adj(v)) {
			int w = e.to();
			// 如果发现加上当前顶点边v-w比之前树中边连接距离要短就更新
			if (distTo[w] > distTo[v] + e.weight()) {
				distTo[w] = distTo[v] + e.weight();
				edgeTo[w] = e;
			}
		}
	}

	/**
	 * 返回从起点s到顶点v的最短距离
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
	 * 如果图中存在从s到达顶点v的最短路径就返回true
	 * @param v
	 * @return
	 * @author Navy D
	 * @date 20170917113307
	 */
	public boolean hasPathTo(int v) {
		validateVertex(v);
		return distTo[v] < Double.POSITIVE_INFINITY;

	}

	/**
	 * 返回起点s到顶点v的最短路径集合，如果不存在这个路径就返回null
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
		EdgeWeightedDigraph g = new EdgeWeightedDigraph(new In("./tinyEWDAG.txt"));
		int s = 1;
		AcyclicSP sp = new AcyclicSP(g, s);
		for (int v = 0; v < g.getV(); v++) {
            if (sp.hasPathTo(v)) {
                System.out.printf("%d to %d (%.2f)  ", s, v, sp.distTo(v));
                for (DirectedEdge e : sp.pathTo(v)) {
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
