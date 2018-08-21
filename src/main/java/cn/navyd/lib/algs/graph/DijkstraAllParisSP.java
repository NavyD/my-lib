package cn.navyd.lib.algs.graph;

/**
 * 任意顶点对之间的最短路径：<br>
 * 使用dijkstra算法实现对每个图中的起点做一个最短路径(如果两个顶点没有路径就为无限大)<br>
 * 复杂度：空间与时间都是evlogv成正比
 * @author Navy D
 * @date 20170911210944
 */
public class DijkstraAllParisSP {
	private DijkstraSP[] all;

	/**
	 * 在加权有向图中计算任意顶点到其他任意顶点的最短路径
	 * @param g
	 */
	public DijkstraAllParisSP(EdgeWeightedDigraph g) {
		all = new DijkstraSP[g.getV()];
		for (int v = 0; v < g.getV(); v++)
			all[v] = new DijkstraSP(g, v);
	}
	/**
	 * 返回图中从s到t的最短路径，如果不存在就返回null
	 * @param s
	 * @param t
	 * @return
	 * @author Navy D
	 * @date 20170917121734
	 */
	public Iterable<DirectedEdge> path(int s, int t) {
		validateVertex(s, t);
		return all[s].pathTo(t);
	}

	/**
	 * 如果图中存在s到t的路径就返回true
	 * @param s
	 * @param t
	 * @return
	 * @author Navy D
	 * @date 20170917121935
	 */
	public boolean hasPath(int s, int t) {
		validateVertex(s, t);
		return dist(s, t) < Double.POSITIVE_INFINITY;
	}

	/**
	 * 返回从s到t的最短路径权重
	 * @param s
	 * @param t
	 * @return
	 * @author Navy D
	 * @date 20170917122002
	 */
	public double dist(int s, int t) {
		validateVertex(s, t);
		return all[s].distTo(t);
	}

	private void validateVertex(int... args) {
		for (int i : args)
			if (i < 0 || i >= all.length)
				throw new IllegalArgumentException("vertex " + i + " is not between 0 and " + (all.length-1));
	}


}
