package cn.navyd.lib.algs.util;

/**
 * 路径压缩加权quick-union算法:
 * 在检查节点的时候将节点关联到根节点
 * 	union	1		 find	1	1
 * 			1	<- 2		1	1
 * 							2->	1
 * 复杂度：union find 均摊成本后接近与1
 *
 * @author Navy D
 * @date 20170902121125
 */
public class WeightedQuickUnionPathCompressionUF implements UF {
	// 链接数组索引
	private int[] id;
	// 每个根节点对应的分量大小
	private int[] size;
	// 连同分量的数量
	private int count;

	public WeightedQuickUnionPathCompressionUF(int n) {
		if (n < 0)
			throw new IllegalArgumentException();
		count = n;
		id = new int[n];
		size = new int[n];
		for (int i = 0; i < n; i++) {
			id[i] = i;
			size[i] = 1;
		}
	}

	private void validate(int p) {
		int n = id.length;
		if (p < 0 || p >= n) {
			throw new IllegalArgumentException("index " + p + " is not between 0 and " + (n - 1));
		}
	}

	public int count() {
		return count;
	}

	public int find(int p) {
		validate(p);
		int root = p;
		// 寻找p的根节点
		while (root != id[root])
			root = id[root];
		// 将同一连通分量的所有节点关联到根节点，对于两个大小类似的连通分量该操作仍然需要许多时间
		while (p != root) {
			int newP = id[p];
			id[p] = root;
			p = newP;
		}
		return root;
	}

	public boolean connected(int p, int q) {
		validate(p);
		validate(q);
		return find(p) == find(q);
	}

	public void union(int p, int q) {
		validate(p);
		validate(q);
		int rootP = find(p);
		int rootQ = find(q);

		if (rootP == rootQ)
			return;
		// 将小的连通分量连接到大的底下
		if (size[rootP] < size[rootQ]) {
			id[rootP] = rootQ;
			size[rootQ] += size[rootP];
		} else {
			id[rootQ] = rootP;
			size[rootP] += size[rootQ];
		}
		count--;
	}
}
