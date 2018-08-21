package cn.navyd.lib.algs.util;

/**
 * union-find算法的最优算法：</br>
 * 思想：union中使用了路径加权的quick-union算法。</br>
 * find使用了均摊成本的算法，类似延迟更新的方法。只有在触点find(p)时，才会更新触点所属的连通分量</br>
 * 复杂度：find与union均接近常数级别</br>
 * @author Navy D
 * @date 20170927205611
 */
public class QuickUF implements UF {
	// 连通分量标识 如：id[i]=5,表示i属于标识5的连通分量
	private int[] id;
	// 触点所属连通分量的大小
	private int[] size;
	// 连通分量的数量
	private int count;

	/**
	 * 初始化一个以整数标识(0~n-1)的n个触点空的uf结构
	 *
	 * @param n
	 */
	public QuickUF(int n) {
		if (n < 0)
			throw new IllegalArgumentException();
		id = new int[n];
		size = new int[n];
		count = n;
		for (int i = 0; i < n; i++) {
			id[i] = i;
			size[i] = 1;
		}
	}

	/**
	 * 返回连通分量的数量
	 * @author Navy D
	 * @date 20170927203144
	 */
	@Override
	public int count() {
		return count;
	}

	/**
	 * 返回触点p所在连通分量的标识符 实现为常数级别(均摊成本)
	 * @author Navy D
	 * @date 20170927204145
	 */
	@Override
	public int find(int p) {
		validate(p);
		while (p != id[p]) {
			// 将触点p的根节点id[p]读取找到根节点连接的连通分量,并更新到这个触点到新的连通分量上
			id[p] = id[id[p]];
			// 找到p更新后的节点(新的根节点)
			p = id[p];
		}
		return p;
	}

	/**
	 * 如果p和q都存在一个连通分量就返回true
	 * @author Navy D
	 * @date 20170927204241
	 */
	@Override
	public boolean connected(int p, int q) {
		validate(p);
		validate(q);
		return find(p) == find(q);
	}

	/**
	 * 在触点p和q之间添加一条连接
	 * 实现接近常数级别
	 * @author Navy D
	 * @date 20170927205302
	 */
	@Override
	public void union(int p, int q) {
		validate(p);
		validate(q);
		int rootP = find(p);
		int rootQ = find(q);
		if (rootP == rootQ)
			return;
		// 将小的根节点连接到大的根节点上
		if (size[rootP] < size[rootQ]) {
			id[rootP] = rootQ;
			size[rootQ] += size[rootP];
		} else {
			id[rootQ] = rootP;
			size[rootP] += size[rootQ];
		}
		count--;
	}

	private void validate(int p) {
		if (p < 0 || p >= id.length)
			throw new IndexOutOfBoundsException("index " + p + " is not between 0 and " + (id.length - 1));

	}

	public static void main(String[] args) {
		In in = new In("../MyAlgs/algs4-data/tinyUF.txt");
		int n = in.readInt();
		in.readLine();
		UF uf = new QuickUF(n);
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
