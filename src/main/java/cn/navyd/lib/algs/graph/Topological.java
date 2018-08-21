package cn.navyd.lib.algs.graph;

/**
 * 拓扑排序的api
 * 已知实现：TopologicalDFS 和 TopologicalQueue
 * @author Navy D
 * @date 20170918201211
 */
public interface Topological {
	boolean hasOrder();
	Iterable<Integer> order();
}
