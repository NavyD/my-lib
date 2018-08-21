package cn.navyd.lib.algs.string;

import cn.navyd.lib.algs.sort.MinPQ;

/**
 * huffman压缩算法：前缀码的单词查找树
 * <p>
 * 思想：将字符编码为特定的前缀码，每个不同的字符具有不同的前缀码，
 * 字符在文本中出现的频率越大前缀码就短，在树中的深度更低。在应用时，
 * 需要将树写入文件中然后才写入编码。以正确读取出编码构造原文件。
 * 复杂度：
 * <p>
 * 注意：该类仅提供一个模板实现，没有完美的运行价值。</br>
 * 该算法不适用于unicode，不适用小文件的压缩，可能树的大小都会超过实际文件大小
 * @author Navy D
 * @date 20171012125244
 */
public class Huffman {
	private static int R = 256;

	private static class Node implements Comparable<Node> {
		// 叶子节点需要被编码的字符。其余节点不表示编码字符
		private char ch;
		// 叶子(字符)节点出现的频率
		private int freq;
		private final Node left, right;

		Node(char ch, int freq, Node left, Node right) {
			this.ch = ch;
			this.freq = freq;
			this.left = left;
			this.right = right;
		}

		/**
		 * 如果是叶子节点就返回true
		 */
		public boolean isLeaf() {
			// 不允许树出现只有一个孩子的情况
			assert (left == null && right == null) || (left != null && right != null);
			return left == null && right == null;
		}

		@Override
		public int compareTo(Node o) {
			return this.freq -o.freq;
		}
	}

	/**
	 * 从输入中读取要压缩的字符串，并将字符构造成编码树，然后写入编码树，最后写入编码
	 */
	public static void compress() {
		// 读取输入
		String s = BinaryStdIn.readString();
		char[] input = s.toCharArray();
		// 统计要编码字符串s中每个字符出现的频率
		int[] freq = new int[R];
		for (int i = 0; i < input.length; i++)
			freq[input[i]]++;
		// 构造huffman树
		Node root = buildTrie(freq);
		// 构造编译表
		String[] st = new String[R];
		buildCode(st, root, "");
		// 写入解码用的树
		writeTrie(root);
		// 打印字符总数
		BinaryStdOut.write(input.length);
		// 编码写入
		for (int i = 0; i < input.length; i++) {
			String code = st[input[i]];
			for (int j = 0; j < code.length(); j++)
				if (code.charAt(j) == '1')
					BinaryStdOut.write(true);
				else
					BinaryStdOut.write(false);

		}
		BinaryStdOut.close();

	}

	/**
	 * 从输入中读取一个树
	 */
	public static void expand() {
		Node root = readTrie();
		int n = BinaryStdIn.readInt();
		for (int i = 0; i < n; i++) {
			Node x = root;
			while (!x.isLeaf())
				if (BinaryStdIn.readBoolean())
					x = x.right;
				else
					x = x.right;
			BinaryStdOut.write(x.ch);
		}
		BinaryStdOut.close();
	}

	/**
	 * 构造编译表。即将树中的节点转化为编码
	 */
	private static void buildCode(String[] st, Node x, String s) {
		if (x.isLeaf()) {
			st[x.ch] = s;
			return;
		}
		// 左边为0 右边为1
		buildCode(st, x.left, s + '0');
		buildCode(st, x.right, s + '1');
	}

	/**
	 * 使用要编码字符串中字符出现频率数组构造树
	 */
	private static Node buildTrie(int[] freq) {
		MinPQ<Node> pq = new MinPQ<>();
		// 将出现的编码字符构造为叶子结点   森林
		for (char c = 0; c < R; c++)
			if (freq[c] != 0)
				pq.insert(new Node(c, freq[c], null, null));
		// 只有一个字符时
		if (pq.size() == 1)
			if (freq['\0'] == 0)
				pq.insert(new Node('\0', 0, null, null));
			else
				pq.insert(new Node('\1', 0, null, null));

		// 将森林合并为一颗树
		while (pq.size() > 1) {
			// 小的为左节点
			Node childL = pq.delMin();
			// 大一点的为右节点
			Node childR= pq.delMin();
			// 将两个子树合并为一个树
			Node parent = new Node('\0', childL.freq + childR.freq, childL, childR);
			pq.insert(parent);
		}
		// 返回根节点
		return pq.delMin();
	}

	/**
	 * 以树的前序遍历输出树的字符串bit
	 */
	private static void writeTrie(Node x) {
		if (x.isLeaf()) {
			BinaryStdOut.write(true);
			BinaryStdOut.write(x.ch);
			return;
		}
		BinaryStdOut.write(false);
		writeTrie(x.left);
		writeTrie(x.right);
	}

	/**
	 * 读取树的一个节点
	 */
	private static Node readTrie() {
		if (BinaryStdIn.readBoolean())
			return new Node(BinaryStdIn.readChar(), 0, null, null);
		return new Node('\0', 0, readTrie(), readTrie());
	}


	public static void main(String[] args) {
		System.out.println("input:");
		compress();
	}



}
