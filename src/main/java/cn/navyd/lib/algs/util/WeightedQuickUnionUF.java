package cn.navyd.lib.algs.util;

/**
 * union-find算法：</br>
 * 通过初始化一个固定的数组分量，并对分量进行union-find操作划分等价类(两个对象连接)；</br>
 * weightedquick-union算法将连接转换为树形结构：</br>
 * 如：id[id[id[5]]]		5->2->6<->6;</br>
 * 表示将id[5]连接到id[2]再到id[6],而id[6]为根节点;</br>
 * find(p)总是返回p点的根，union(p,q)连接p q等价类</br>
 * 加权weighted表示对quick-union优化：</br>
 * union(p,q)时总是将小树连接到大树，以优化find(p)算法中查找到根的速度</br>
 *
 * 复杂度：通过union构造的节点深度最多为lgn</br>
 * @author Navy D
 * @date 20170726154407
 */
public class WeightedQuickUnionUF implements UF {
	//链接数组索引
	private int[] id;
	//每个根节点对应的分量大小
	private int[] size;
	//连同分量的数量
	private int count;
	/**
	 * 初始化0-n的链接索引
	 * @param n 存在的索引
	 */
	public WeightedQuickUnionUF(int n) {
		count = n;
		id = new int[n];
		size = new int[n];
		for (int i = 0; i < n; i++) {
			id[i] = i;
			size[i] = 1;
		}
	}

	/**
	 * 返回当前的连通分量
	 * @return 连通分量
	 * @author Navy D
	 * @date 20170726160233
	 */
	public int count() {
		return count;
	}

	/**
	 * 如果qp同一个连通分量中则返回true
	 * @param p
	 * @param q
	 * @return
	 * @author Navy D
	 * @date 20170726160308
	 */
	public boolean connected(int p, int q) {
		return find(p) == find(q);
	}

	/**
	 * 返回p点所属的连通分量的根节点
	 *
	 * @param p
	 * @return
	 * @author Navy D
	 * @date 20170726160446
	 */
	public int find(int p) {
		/*
		 * 如：5 != id[5](=2), 则2 != id[2](=6),则 6 == id[6] return 6(root);
		 */
		while (p != id[p])
			p = id[p];
		return p;
	}

	/**
	 * 连接pq使之处于同一连通分量
	 * @param p
	 * @param q
	 * @author Navy D
	 * @date 20170726160528
	 */
	public void union(int p, int q) {
		int rootP = find(p);
		int rootQ = find(q);

		if (rootP == rootQ)
			return;
		/*
		 * 未加权时的处理方式
		 * 不区分大小树，可能导致一个树过高，find时的速度过慢
		 */
//		id[i] = j;
		//将小的连通分量连接到大的底下
		if (size[rootP] < size[rootQ]) {
			id[rootP] = rootQ;
			size[rootQ] += size[rootP];
		} else {
			id[rootQ] = rootP;
			size[rootP] += size[rootQ];
		}
		count--;
	}

	public static void main(String[] args) {
		In in = new In("../MyAlgs/algs4-data/largeUF.txt");
		int n = in.readInt();
		in.readLine();
		UF uf = new WeightedQuickUnionUF(n);
		while (!in.isEmpty()) {
			int p = in.readInt();
			int q = in.readInt();
			in.readLine();
			uf.union(p, q);
//			System.out.println(p + " " + q);
		}
		System.out.println(uf.connected(4, 3));
		System.out.println(uf.count());
	}
}
