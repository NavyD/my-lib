package cn.navyd.lib.algs.util;

/**
 * union-find算法的api。
 * 目前已知实现类：QuickUF, QuickFindUF, QuickUnionUF, WeightedQuickUnionPathCompressionUF, WeightedQuickUnionUF
 * @author Navy D
 * @date 20170927210245
 */
public interface UF {
	/**
	 * 返回连通分量的数量
	 *
	 * @author Navy D
	 */
	int count();

	/**
	 * 返回触点p所在连通分量的标识符
	 *
	 * @author Navy D
	 */
	int find(int p);

	/**
	 * 如果p和q都存在一个连通分量就返回true
	 *
	 * @author Navy D
	 */
	boolean connected(int p, int q);

	/**
	 * 在触点p和q之间添加一条连接
	 *
	 * @param p
	 * @param q
	 * @author Navy D
	 */
	void union(int p, int q);
}
