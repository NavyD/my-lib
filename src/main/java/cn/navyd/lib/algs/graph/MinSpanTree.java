package cn.navyd.lib.algs.graph;

/**
 * 加权图的最小生成树的api
 * @author Navy D
 * @date 20170917214225
 */
public interface MinSpanTree {
	/**
	 * 最小生成树的所有边
	 * @return
	 * @author Navy D
	 * @date 20170917214715
	 */
	Iterable<Edge> edges();
	/**
	 * 最小生成树的权值总和
	 * @return
	 * @author Navy D
	 * @date 20170917214732
	 */
	double weight();
}
