package cn.navyd.lib.algs.util;

/**
 * 连通快速查找算法：</br>
 * 思想：
 * find:通过保证一个连通分量顶点p q对应值相等快速查找</br>
 * union:对于没有连接的触点，需要遍历数组将连通标识更改另一个触点的标识</br>
 * 复杂度：访问数组的次数在n+3和2n+1之间  插入n个元素需要访问n^2
 * @author Navy D
 * @date 20170902113207
 */
public class QuickFindUF implements UF {
	// 连通分量标识 如：id[i]=5,表示i属于标识5的连通分量
	private int[] id;
	// 连通分量的数量
	private int count;

	/**
	 * 初始化一个以整数标识(0~n-1)的n个触点空的uf结构
	 *
	 * @param n
	 */
	public QuickFindUF(int n) {
		if (n < 0)
			throw new IllegalArgumentException();
		id = new int[n];
		count = n;
		// 初始化所有连通分量
		for (int i = 0; i < n; i++)
			id[i] = i;
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

	/**
	 * 返回触点p所在连通分量的标识符 实现为常数级别
	 *
	 * @author Navy D
	 * @date 20170927194214
	 */
	public int find(int p) {
		validate(p);
		return id[p];
	}

	/**
	 * 如果p和q都存在一个连通分量就返回true
	 *
	 * @author Navy D
	 * @date 20170927194317
	 */
	public boolean connected(int p, int q) {
		validate(p);
		validate(q);
		return find(p) == find(q);
	}

	/**
	 * 在触点p和q之间添加一条连接 实现为平方级别
	 *
	 * @author Navy D
	 * @date 20170927194632
	 */
	public void union(int p, int q) {
		validate(p);
		validate(q);
		// 查找顶点p q的连通分量
		int rootP = find(p);
		int rootQ = find(q);
		// 如果属于同一连通分量
		if (rootP == rootQ)
			return;
		// 如果不是，则将p在数组的连通分量 全部 更改为q的连通分量
		for (int i = 0; i < id.length; i++)
			if (rootP == id[i])
				id[i] = rootQ;
		count--;
	}

	private void validate(int p) {
		int n = id.length;
		if (p < 0 || p >= n) {
			throw new IndexOutOfBoundsException("index " + p + " is not between 0 and " + (n - 1));
		}
	}

	public static void main(String[] args) {
		In in = new In("../MyAlgs/algs4-data/tinyUF.txt");
		int n = in.readInt();
		in.readLine();
		UF uf = new QuickFindUF(n);
		while (!in.isEmpty()) {
			int p = in.readInt();
			int q = in.readInt();
			in.readLine();
			uf.union(p, q);
			System.out.println(p + " " + q);
		}
		System.out.println(uf.connected(4, 3));
		System.out.println(uf.count());
	}
}
