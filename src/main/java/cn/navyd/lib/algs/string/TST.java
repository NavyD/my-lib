package cn.navyd.lib.algs.string;

import java.util.NoSuchElementException;

import cn.navyd.lib.algs.util.In;
import cn.navyd.lib.algs.util.Queue;


/**
 * 三向单词查找树：
 * 思想：节点Node含有一个字符，三个小等大方向的链接，一个值。每个节点显示的表示一个字符，
 * 查找一个键必须沿着中间的链接才能找到键。
 *
 * N个平均长度为w的字符串的链接总数为3N~3Nw之间
 * 未命中须比较lnN
 *
 * @author Navy D
 * @date 20171002213548
 * @param <V>
 */
public class TST<V> implements StringST<V> {
	// 符号表的根节点
	private Node<V> root;
	// 键的数量
	private int n;

	private static class Node<Value> {
		// 节点的值
		Value val;
		// 节点表示为一个字符
		char c;
		// 节点的左,中, 右三个子连接
		Node<Value> left, mid, right;

		public Node(char c) {
			this.c = c;
		}
	}

	/**
	 * 初始化一个空的String符号表
	 */
	public TST() {

	}

	/**
	 * 插入一个键值对到符号表中，如果键已经存在就会覆盖旧值
	 * 如果值为null，就会从符号表中删除这个键
	 * @author Navy D
	 * @date 20171003201231
	 */
	@Override
	public void put(String key, V val) {
		if (key == null)
            throw new IllegalArgumentException("calls put() with null key");
		root = put(root, key, val, 0);
	}

	/**
	 * put分以下几种情况：
	 * 空节点：
	 * 1.遇到空节点就构造一个与当前键字符相等的字符节点往下(正下方)不断延伸，直到构造完key的字符就插入值val
	 * 非空节点：
	 * 2.遇到相等的字符节点就用键的下一个字符与节点的中间子字符节点比较。
	 * 3.遇到不相等的字符节点就用键的当前字符与节点的小 左，大右的方式比较子节点
	 *
	 * @author Navy D
	 * @date 20171002210354
	 */
	private Node<V> put(Node<V> x, String key, V val, int d) {
		char c = key.charAt(d);
		// 当键的字符不存在树中时
		if (x == null)
			x = new Node<>(c);
		// 键的字符小于节点字符，取节点左边子节点与当前键字符比较
		if (c < x.c)
			x.left = put(x.left, key, val, d);
		// 键的字符大于节点字符，取节点右边子节点与当前键字符比较
		else if (c > x.c)
			x.right = put(x.right, key, val, d);
		// 键的字符等于节点字符
		// 如果不是尾字符就取节点的中间节点再与下一个字符比较
		else if (d != key.length() - 1)
			x.mid = put(x.mid, key, val, d + 1);

		// 如果是尾字符就找到这个节点并插入覆盖值val
		else {
			if (x.val == null)
				n++;
			// 如果不写这个else 那么所有if elseif后递归返回后都会经过这个使每个x.val赋值
			x.val = val;
		}
		return x;
	}

	/**
	 * 返回指定键关联的值。如果不存在键就返回null(键没有关联值)
	 * 如果key存在符号表中但是没有关联值表示不存在这个键
	 * 如：插入过"shell"，但是get("she")返回null
	 * @author Navy D
	 * @date 20171003163409
	 */
	@Override
	public V get(String key) {
		if (key == null)
			throw new IllegalArgumentException("key is null");
		if (key.length() == 0)
			throw new IllegalArgumentException("key must have length >= 1");
		if (isEmpty())
			throw new NoSuchElementException();
		Node<V> x = get(root, key, 0);
		return x != null ? x.val : null;
	}

	/**
	 * 查找有以下情况：
	 * 1.遇到不相等的字符节点就用键的当前字符与节点的小 左，大右的方式比较子节点
	 * 2.遇到相等的字符节点就用键的下一个字符与节点的中间子字符节点比较。
	 * 3.在查找字符中遇到空连接或，就返回null
	 * @author Navy D
	 * @date 20171002204116
	 */
	private Node<V> get(Node<V> x, String key, int d) {
		if (x == null)
			return null;
		System.out.println(key);
		System.out.println("x.c=" + x.c);
		int c = key.charAt(d);
		// 如果小于节点的字符，取左边节点字符与当前字符比较
		if (c < x.c)
			return get(x.left, key, d);
		// 反之
		else if (c > x.c)
			return get(x.right, key, d);
		// 如果键的字符与节点字符相等且键的当前字符不是尾字符，取键的下一个字符与中间节点比较
		else if (d != key.length() - 1)
			return get(x.mid, key, d + 1);
		// 是尾字符
		return x;
	}

	@Override
	public void delete(String key) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	/**
	 * 如果指定键存在符号表(存在一个值)就返回true。
	 * @author Navy D
	 * @date 20171003163153
	 */
	@Override
	public boolean contains(String key) {
		if (key == null)
			throw new IllegalArgumentException("argument to contains() is null");
		return get(key) != null;
	}

	/**
	 * 如果符号表为空就返回true
	 * @author Navy D
	 * @date 20171003163239
	 */
	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * 返回指定字符串在符号表中存在的最长前缀字符串。如果不存在这样的前缀就返回null
	 * 如符号表中存在she，字符串query为shell，返回字符串she
	 * @author Navy D
	 * @date 20171003210719
	 */
	@Override
	public String longestPrefixOf(String query) {
		if (query == null)
            throw new IllegalArgumentException("calls longestPrefixOf() with null argument");
		int length = NonRecursionSearch(root, query);
		return length != -1 ? query.substring(0, length) : null;

//		int length = search(root, query, 0, -1);
//		return length != -1 ? query.substring(0, length+1) : null;
	}

	/**
	 *  向下搜索给定字符串的最长键前缀。
	 * 1.当字符串s刚好存在树中val!=null保存这个s的长度
	 * 2.当字符串不存在，保存当前s经过最近存在的键
	 * 	如树中she e节点val存在，字符串s为shell，而ll在she后不存在，就保存she的长度
	 * @date 20171003213104
	 */
	private int NonRecursionSearch(Node<V> node, String query) {
		if (query.length() == 0)
			return -1;
		int length = -1;
		Node<V> x = node;
		int i = 0;
		while (x != null && i != query.length()) {
			// 先判断这个值在那边，中间相等时再++
			char c = query.charAt(i);
			if (c < x.c)
				x = x.left;
			else if (c > x.c)
				x = x.right;
			else {
				i++;
				if (x.val != null)
					length = i;
				x = x.mid;
			}
		}
		return length;
	}

	/**
	 * TST与TrieST的区别：
	 * 两者的根节点不同，前者根节点就代表一个字符和值，后者的根节点值跟前者一样
	 * 但是要到节点的next[c]中找到这个字符。即前者比后者少一个层次
	 *
	 * 相对于TrieST:如存在she这个字符串，用shr匹配,结果应为null，如果d==query.length在后面
	 * 若she是正下方连接，则h下一个为x.mid就会扫描到e存在值，此时d=3就返回了length=d=3
	 * 当遇到c=r正下方却是mid=e，需要添加判断条件，c==x.c
	 *
	 * @author Navy D
	 * @date 20171003210125
	 */
//	private int search(Node<V> x, String query, int d, int length) {
//		if (x == null)
//			return length;
//		System.out.format("x.c=%2s x.val=%4s d=%2s length=%2s\n", x.c, x.val, d, length);
//		// 找完query字符
//		if (d == query.length())
//			return length;
//		char c = query.charAt(d);
//		// 存在s部分字符串,保存最近路径上的前缀单词长度
//		if (x.val != null && c == x.c)
//			length = d;
//		System.out.format("c=%2s\n", c);
//		if (c < x.c)
//			return search(x.left, query, d, length);
//		else if (c > x.c)
//			return search(x.right, query, d, length);
//		else
//			return search(x.mid, query, d + 1, length);
//	}

	@Override
	public Iterable<String> keysWithPrefix(String prefix) {
		if (prefix == null) {
			throw new IllegalArgumentException("calls keysWithPrefix() with null argument");
		}
		Queue<String> queue = new Queue<String>();
		Node<V> x = get(root, prefix, 0);
		if (x == null)
			return queue;
		if (x.val != null)
			queue.enqueue(prefix);
		collect(x.mid, new StringBuilder(prefix), queue);
		return queue;
	}

	@Override
	public Iterable<String> keysThatMatch(String pattern) {
		Queue<String> queue = new Queue<String>();
        collect(root, new StringBuilder(), 0, pattern, queue);
        return queue;
	}

	private void collect(Node<V> x, StringBuilder prefix, int i, String pattern, Queue<String> queue) {
		if (x == null)
			return;
		char c = pattern.charAt(i);
		if (c == '.' || c < x.c)
			collect(x.left, prefix, i, pattern, queue);
		if (c == '.' || c == x.c) {
			if (i == pattern.length() - 1 && x.val != null)
				queue.enqueue(prefix.toString() + x.c);
			if (i < pattern.length() - 1) {
				collect(x.mid, prefix.append(x.c), i + 1, pattern, queue);
				prefix.deleteCharAt(prefix.length() - 1);
			}
		}
		if (c == '.' || c > x.c)
			collect(x.right, prefix, i, pattern, queue);
	}

	/**
	 * 返回符号表中键的数量
	 * @author Navy D
	 * @date 20171003163256
	 */
	@Override
	public int size() {
		return n;
	}

	/**
	 * 返回符号表中所有键的集合
	 * @author Navy D
	 * @date 20171003222847
	 */
	@Override
	public Iterable<String> keys() {
		Queue<String> queue = new Queue<String>();
		collect(root, new StringBuilder(), queue);
		return queue;
	}

	/**
	 * 维护一个StringBuilder用来保存从x节点出发的路径上的一系列字符
	 * 收集从节点x开始树中所有的键。使用stringbuilder提高性能
	 *
	 * 前序遍历？
	 * @author Navy D
	 * @date 20171003222306
	 */
	private void collect(Node<V> x, StringBuilder prefix, Queue<String> queue) {
		if (x == null)
			return;
		collect(x.left, prefix, queue);
		if (x.val != null)
			queue.enqueue(prefix.toString() + x.c);
		collect(x.mid, prefix.append(x.c), queue);
		prefix.deleteCharAt(prefix.length() - 1);
		collect(x.right, prefix, queue);
	}

	public static void main(String[] args) {
		TST<Integer> st = new TST<>();
		In in = new In("../MyAlgs/algs4-data/shellsST.txt");
		for (int i = 0; !in.isEmpty(); i++) {
			String key = in.readString();
			st.put(key, i);
		}
		System.out.println(st.size());
		for (String s : st.keys())
			System.out.println(s);
		System.out.println();

		String s = "byss";
		System.out.println("longestPrefixOf(\"shellsort\"):");
		System.out.println(st.longestPrefixOf(s));
		System.out.println();
	}

}
