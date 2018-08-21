package cn.navyd.lib.algs.util;

/**
 * 图快速联合算法：
 * 通过find找到顶点所在的连通分量的根，union只需要连接这两个根节点就可以连通为一个分量
 * 复杂度：find访问数组次数为1+2*顶点深度，union，connect需要两次find
 * 最坏情况为n^2
 * @author Navy D
 * @date 20170902115355
 */
public class QuickUnionUF {
	// 连通分量标识 如：id[i]=5,表示i属于标识5的连通分量
	private int[] id;
	// 连通分量的数量
	private int count;

	/**
	 * 初始化一个以整数标识(0~n-1)的n个触点空的uf结构
	 *
	 * @param n
	 */
	public QuickUnionUF(int n) {
		id = new int[n];
		count = n;

		for (int i = 0; i < id.length; i++)
			id[i] = i;
	}

	/**
	 * 找到顶点p的连通分量根节点标识符
	 * 查找一个p是线性级别
	 * @param p
	 * @return
	 * @author Navy D
	 * @date 20170902120017
	 */
	public int find(int p) {
		// 如果顶点的索引不是本身，就使用索引寻找
		while (p != id[p])
			p = id[p]; // 将顶点值更新，寻找下一个索引
		return p;
	}

	/**
	 * 返回连通分量的数量
	 *
	 * @author Navy D
	 * @date 20170927193944
	 */
	public int count() {
		return count;
	}

	public boolean connected(int p, int q) {
		return find(p) == find(q);
	}

	/**
	 * 快速连接
	 * 如果使用0-1,0-2,0-3这样的有序输入，会使连通分量形成一个直线
	 * @param p
	 * @param q
	 * @author Navy D
	 * @date 20170927201403
	 */
	public void union(int p, int q) {
		int rootP = find(p);
		int rootQ = find(q);

		if (rootP == rootQ)
			return ;
		id[rootP] = rootQ;
		count--;
	}

}
