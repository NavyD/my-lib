package cn.navyd.lib.algs.graph;

/**
 * 加权图的最短路径api
 * @author Navy D
 * @date 20170917213822
 */
public interface ShortestPath {
	/**
	 * 给从起点s到顶点v的最短路径的权值
	 * @param v
	 * @return
	 * @author Navy D
	 * @date 20170917214526
	 */
	double distTo(int v);

	/**
	 * 起点s到顶点v是否有最短路径
	 * @param v
	 * @return
	 * @author Navy D
	 * @date 20170917214631
	 */
	boolean hasPathTo(int v);

	/**
	 * 返回最短路径
	 * @param v
	 * @return
	 * @author Navy D
	 * @date 20170917214655
	 */
	Iterable<DirectedEdge> pathTo(int v);
}
