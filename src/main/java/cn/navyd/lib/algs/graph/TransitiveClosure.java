package cn.navyd.lib.algs.graph;

/**
 * 有向图的可达性：是否存在顶点v到w的路径
 * 虽说理论是什么传递闭包，但是看不懂，只好自己强行解释
 * 由于无法使用无向图中cc的方式(有向图中一个顶点可能在多个连通分量中)
 * 可以简单的将每个顶点深搜，查询时只要检查这个顶点深搜的结果就行了
 * @author Navy D
 * @date 20170908182209
 */
public class TransitiveClosure {
	// 每个顶点深搜结果
	private DirectedDFS[] all;

	public TransitiveClosure(Digraph g) {
		all = new DirectedDFS[g.getV()];
		for (int i = 0; i < g.getV(); i++)
			all[i] = new DirectedDFS(g, i);// 深搜
	}

	/**
	 * 如果顶点v可达w就返回true
	 * @param v
	 * @param w
	 * @return
	 * @author Navy D
	 * @date 20170918205757
	 */
	public boolean reachable(int v, int w) {
		validateVertex(v);
        validateVertex(w);
		return all[v].marked(w);// 直接查询对应顶点的深搜结果
	}

	private void validateVertex(int v) {
        int V = all.length;
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (V-1));
    }

}
