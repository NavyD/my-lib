package cn.navyd.lib.algs.string;

import java.util.NoSuchElementException;

import cn.navyd.lib.algs.util.In;
import cn.navyd.lib.algs.util.Queue;

/**
 * <p>基于单词查找树的符号表：</p>
 * <p>思想：单词查找树使用一个节点Node，节点可以存储值value，节点含R个空链接
 * 这些链接只会写入对应的字符node对象不会写入字符。使用下标表示每个字符
 *	</p><p>
 * 复杂度：
 * 树的结构形状与插入删除的顺序无关，查找和删除访问数组的次数是键的长度+1
 * 一个n个随机键的树，未命中查找平均需要logRN，一般只需要检查很少的节点</br>
 * 空间：链接总数在RN~RNw之间，w为键的平均长度，当键较长时，总数接近RNw
 * </p>
 * 不要使用该算法处理大型字母表的大量长键。unicode就不要想了，扩展ascii都有问题
 *
 * @author Navy D
 * @date 20171002200446
 * @param <V>
 */
public class TrieST<V> implements StringST<V> {
	// 字符基数表示扩展ascii
	private static int R = 1 << 8;
	// 根节点
	private Node<V> root;
	// 树中键的数量
	private int n;

	// R向树节点
	private static class Node<Value> {
		// 节点值
		private Value val;
		// R个子节点链接数组
		@SuppressWarnings("unchecked")
		private Node<Value>[] next = new Node[R];
	}

	/**
	 * 初始化一个空的String符号表
	 */
	public TrieST() {

	}

	/**
	 * 插入一个键值对到符号表中，如果键已经存在就会覆盖旧值
	 * 如果值为null，就会从符号表中删除这个键
	 * @author Navy D
	 * @date 20171003120409
	 */
	@Override
	public void put(String key, V val) {
		if (key == null)
			throw new IllegalArgumentException("key is null");
		if (val == null)
			delete(key);
		root = put(root, key, val, 0);
	}

	/**
	 * put有2中情况：
	 * 1.树中不存在key某个字符后对应的节点遇到空连接，需要创建这些节点
	 * 2.树中存在key尾字符对应的节点，直接用val覆盖
	 * @author Navy D
	 * @date 20171002151036
	 */
	private Node<V> put(Node<V> x, String key, V val, int d) {
		// 创建key中所有不存在的字符节点
		if (x == null)
			x = new Node<>();
		// 如果找到尾字符节点
		if (d == key.length()) {
			if (x.val == null)
				n++;
			x.val = val;
			return x;
		}
		char c = key.charAt(d);
		// 无法插入
//		return put(x.next[c], key, val, d+1);
		// 递归查找下一个字符
		x.next[c] = put(x.next[c], key, val, d + 1);
		return x;
	}

	/**
	 * 返回指定键关联的值。如果不存在键就返回null(键没有关联值)
	 * 如果key存在符号表中但是没有关联值表示不存在这个键
	 * 如：插入过"shell"，但是get("she")返回null
	 * @author Navy D
	 * @date 20171003115321
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
	 * get在单词查找树中共存在三种情况
	 * 1.key在查找中结束于空连接(key的某个字符后不存在树中)
	 * 2.key的尾字符对应的值为空
	 * 3.key对应的尾字符节点值非空
	 * @author Navy D
	 * @date 20171002150335
	 */
	private Node<V> get(Node<V> x, String key, int d) {
		// 树中没有这个字符串某个字符后的子字符串。在字符串查找完前就遇到空连接
		if (x == null)
			return null;
		// 树中含有这个单词的所有字符，可能val为null
		if (d == key.length())
			return x;
		char c = key.charAt(d);
		// 在树中查找字符串的下一个字符
		return get(x.next[c], key, d+1);
	}

	/**
	 * 符号表中如果存在指定的键就删除这个键
	 * @author Navy D
	 * @date 20171003121015
	 */
	@Override
	public void delete(String key) {
		root = delete(root, key, 0);
	}

	/**
	 * 删除一个键有以下情况：
	 * 1.当删除的键的节点链接的子节点存在键值(即存在比包含删除键的字符串)，只将键节点值置为null
	 * 2.如果不存在键值且所有连接为空就向上删除这些链接为空的节点
	 * 3.当删除键的节点前的链接存在键值,终止向上删除操作
	 * @author Navy D
	 * @date 20171002180435
	 */
	private Node<V> delete(Node<V> x, String key, int d) {
		if (x == null)
			return null;
		// 找到删除键节点，值置为null
		if (d == key.length()) {
			if (x.val != null)
				n--;
			x.val = null;
		}
		// 递归向下找key节点
		else {
			char c = key.charAt(d);
			x.next[c] = delete(x.next[c], key, d+1);
		}
		// 如果删除键过程中某个节点存在值就终止删除
		if (x.val != null)
			return x;
		// 扫描节点的所有链接，如果存在链接就返回这个节点
		for (char c = 0; c < R; c++)
			if (x.next[c] != null)
				return x;
		// 如果这个节点所有链接为空就删除这个节点
		return null;
	}

	/**
	 * 如果指定键存在符号表(存在一个值)就返回true。
	 * @author Navy D
	 * @date 20171003115634
	 */
	@Override
	public boolean contains(String key) {
		if (key == null)
			throw new IllegalArgumentException("key is null");
		return get(key) != null;
	}

	/**
	 * 如果符号表为空就返回true
	 * @author Navy D
	 * @date 20171003121652
	 */
	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * 返回指定字符串在符号表中存在的最长前缀字符串。如果不存在这样的前缀就返回null
	 * 如符号表中存在she，字符串query为shell，返回字符串she
	 * @author Navy D
	 * @date 20171003122048
	 */
	@Override
	public String longestPrefixOf(String query) {
		int length = search(root, query, 0, -1);
		// 如果没有找到前缀length不变为-1
		return length != -1 ? query.substring(0, length) : null;
	}

	/**
	 * 向下搜索给定字符串的最长键前缀。
	 * 1.当字符串s刚好存在树中val!=null保存这个s的长度
	 * 2.当字符串不存在，保存当前s经过最近存在的键
	 * 	如树中she e节点val存在，字符串s为shell，而ll在she后不存在，就保存she的长度
	 * @author Navy D
	 * @date 20171002171338
	 */
	private int search(Node<V> x, String s, int d, int length) {
		System.out.println(length);
		if (x == null)
			return length;
		// 存在s部分字符串,保存最近路径上的前缀单词长度
		if (x.val != null)
			length = d;
		// 找完s
		if (d == s.length())
			return length;
		char c = s.charAt(d);
		return search(x.next[c], s, d+1, length);
	}

	/**
	 * 返回以prefix字符串开头后所有的键字符串集合
	 * @author Navy D
	 * @date 20171002164819
	 */
	@Override
	public Iterable<String> keysWithPrefix(String prefix) {
		Queue<String> results = new Queue<>();
		// 收集键pre所在节点下的所有键
		// 如果prefix="";get返回的就是root
		collect(get(root, prefix, 0), new StringBuilder(prefix), results);
		return results;
	}

	/**
	 * 维护一个StringBuilder用来保存从x节点出发的路径上的一系列字符
	 * 收集从节点x开始树中所有的键。使用stringbuilder提高性能
	 * @param x 一个字符节点
	 * @param pre 和该节点关联的字符串(从x节点到当前节点的所有字符)使用""来连接字符串
	 * @param q
	 * @author Navy D
	 * @date 20171002163808
	 */
	private void collect(Node<V> x, StringBuilder pre, Queue<String> results) {
		if (x == null)
			return;
		// 如果存在值就说明这个键存在  加入
		if (x.val != null)
			results.enqueue(pre.toString());
		// 递归从节点x开始遍历收集每一个链接上的键key
		for (char c = 0; c < R; c++) {
			pre.append(c);
			collect(x.next[c], pre, results);
			// 递归回来要删除节点左边链接字符，下一个右边连接需要左边一个链接的字符
			pre.deleteCharAt(pre.length() - 1);
		}
	}

	/**
	 * 返回使用指定模式字符串在符号表中匹配的字符串集合。
	 * pattern模式字符串仅支持通配符.代表未知字符，不支持正则表达式
	 * 对于大规模的符号表该方法的性能较差。
	 * @author Navy D
	 * @date 20171003123456
	 */
	@Override
	public Iterable<String> keysThatMatch(String pattern) {
		Queue<String> results = new Queue<>();
		// 使用StringBuilder空字符串加上pattern路径上存在的所有字符
		collect(root, new StringBuilder(), pattern, results);
		return results;
	}

	/**
	 * 维护一个StringBuilder用来保存从x节点出发的路径上的一系列字符
	 * 使用一个匹配模式pat通配符匹配需要的字符串，使用通配符.代替未知的字符
	 * 通配符的性能较差
	 * @param x	一个字符节点
	 * @param pre 和该节点关联的字符串(从x节点到当前节点的所有字符)
	 * @param pat 要匹配的字符串
	 * @param q
	 * @author Navy D
	 * @date 20171002160504
	 */
	private void collect(Node<V> x, StringBuilder pre, String pat, Queue<String> results) {
		// 如果树中没有这个pat中某个字符
		if (x == null)
			return;
		int d = pre.length();
		// 如果找到这个尾字符且尾节点值存在就加入队列
		if (d == pat.length() && x.val != null)
			results.enqueue(pre.toString());
		// 如果整个字符已经找完
		if (d == pat.length())
			return;

		// 递归找节点连接
		char c = pat.charAt(d);
		// 如果是通配符.就找这个节点的R个子节点
		if (c == '.') {
			for (char ch = 0; ch < R; ch++) {
				pre.append(ch);
				collect(x.next[ch], pre, pat, results);
				pre.deleteCharAt(pre.length() - 1);
			}
		} else {
			pre.append(c);
			collect(x.next[c], pre, pat, results);
			pre.deleteCharAt(pre.length() - 1);
		}
	}

	/**
	 * 返回符号表中键的数量
	 * @author Navy D
	 * @date 20171003130118
	 */
	@Override
	public int size() {
		return n;
	}

	/**
	 * 返回符号表中所有键的集合
	 * @author Navy D
	 * @date 20171003130134
	 */
	@Override
	public Iterable<String> keys() {
		return keysWithPrefix("");
	}

	public static void main(String[] args) {
		TrieST<Integer> st = new TrieST<>();
//		edu.princeton.cs.algs4.TrieST<Integer> st = new edu.princeton.cs.algs4.TrieST<>();

		In in = new In("../MyAlgs/algs4-data/shellsST.txt");
		for (int i = 0; !in.isEmpty(); i++) {
			String key = in.readString();
			st.put(key, i);
		}
		if (st.size() < 100) {
			System.out.println("keys(\"\"):");
			for (String key : st.keys()) {
				System.out.println(key + " " + st.get(key));
			}
			System.out.println();
		}

		System.out.println("longestPrefixOf(\"shellsort\"):");
		System.out.println(st.longestPrefixOf("shellsort"));
		System.out.println();

		System.out.println("longestPrefixOf(\"quicksort\"):");
		System.out.println(st.longestPrefixOf("quicksort"));
		System.out.println();

		System.out.println("keysWithPrefix(\"shor\"):");
		for (String s : st.keysWithPrefix("shor"))
			System.out.println(s);
		System.out.println();

		System.out.println("keysThatMatch(\".he.l.\"):");
		for (String s : st.keysThatMatch(".he.l."))
			System.out.println(s);
		System.out.println();

		String test = "by";
		st.delete(test);
		System.out.println("delete(by):" + !st.contains(test));
		for (String s : st.keys())
			System.out.println(s);
		System.out.println();

		for (String s : st.keys())
			st.delete(s);
		System.out.println("deleteAll():" + st.isEmpty());
		for (String s : st.keys())
			System.out.println(s);
	}


}
