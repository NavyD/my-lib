package cn.navyd.lib.algs.tree;


import cn.navyd.lib.algs.util.In;
import cn.navyd.lib.algs.util.Queue;

/**
 * <p>基于拉链法的散列表：
 * 链表使用SequentialSearchST类实现
 * <p>复杂度：
 * 任意一条链表中的数量趋向为n/m
 * 插入和查找为n/m
 * @author Navy D
 * @date 20170831195456
 * @param <Key>
 * @param <Value>
 */
public class SeparateChainingHashST<Key, Value> implements SimpleSymbolTable<Key, Value>{
	// 初始化容量
	private final static int INIT_CAPACITY = 4;
	// 键值对总数
	private int n;
	// 散列表大小
	private int m;
	// 链表存储碰撞对象
	private SequentialSearchST<Key, Value>[] st;

	/**
	 * 初始化一个空的hash符号表。默认容量为4
	 */
	public SeparateChainingHashST() {
		this(INIT_CAPACITY);
	}

	/**
	 * 初始化一个指定大小的空的符号表
	 * @param m
	 */
	@SuppressWarnings("unchecked")
	public SeparateChainingHashST(int m) {
		if (m < 0)
			throw new IllegalArgumentException();
		this.m = m;
		st = (SequentialSearchST<Key, Value>[]) new SequentialSearchST[m];
		for (int i = 0; i < m; i++)
			st[i] = new SequentialSearchST<>();
	}

	/**
	 * 重新调整大小
	 * @param cap
	 * @author Navy D
	 * @date 20171009143541
	 */
	private void resize(int cap) {
		//创建一个新的拉链对象，新的容量，hash会重新取值
		SeparateChainingHashST<Key, Value> t =  new SeparateChainingHashST<>(cap);
		for (int i = 0; i < m; i++)
			//将每一个链表中的元素重新插入新的对象中，
			for (Key key : st[i].keys())
				t.put(key, st[i].get(key));
		this.m = cap;
		this.n = t.n;
		this.st = t.st;
	}

	/**
	 * 返回指定key在数组中的位置
	 */
	private int hash(Key key) {
		//位与&将int最高位置为0，防止负数%m返回负数
		return (key.hashCode() & 0x7fffffff) % m;
	}

	/**
	 * 返回符号表中指定键关联的值
	 * @author Navy D
	 * @date 20171009144415
	 */
	public Value get(Key key) {
		 if (key == null)
			 throw new IllegalArgumentException("argument to get() is null");
		return st[hash(key)].get(key);
	}

	/**
	 * 向符号表中插入键值对
	 * @author Navy D
	 * @date 20171009143939
	 */
	public void put(Key key, Value val) {
		if (key == null)
			throw new IllegalArgumentException("first argument to put() is null");
        if (val == null) {
            delete(key);
            return;
        }
		//链表的平均长度是10就扩大
		if (n >= 10 * m)
			resize(2 * m);

		//计算key在数组哪一个链表上
		int i = hash(key);
		//如果当前链表没有包含就是插入key
		if (!st[i].contains(key))
			n++;
		//插入或替换key
		st[hash(key)].put(key, val);

	}

	/**
	 * 在符号表中删除指定的key
	 * @author Navy D
	 * @date 20171009144001
	 */
	@Override
	public void delete(Key key) {
		 if (key == null)
			 throw new IllegalArgumentException("argument to delete() is null");
		int i = hash(key);
		if (st[i].contains(key))
			n--;
		st[i].delete(key);

		if (m > INIT_CAPACITY && n <= 2*m)
			resize(m/2);
	}

	/**
	 * 如果指定的key在符号表中就返回true
	 * @author Navy D
	 * @date 20171009143759
	 */
	@Override
	public boolean contains(Key key) {
		if (key == null)
			throw new IllegalArgumentException("argument to contains() is null");
		return get(key) != null;
	}

	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	@Override
	public int size() {
		return n;
	}

	@Override
	public Iterable<Key> keys() {
		Queue<Key> queue = new Queue<Key>();
		for (int i = 0; i < m; i++) {
			for (Key key : st[i].keys())
				queue.enqueue(key);
		}
		return queue;
	}

	public static void main(String[] args) {
		SeparateChainingHashST<String, Integer> st = new SeparateChainingHashST<>(997);
		In in = new In("../MyAlgs/algs4-data/tinyTale.txt");

		for (int i = 0; !in.isEmpty(); i++) {
			String key = in.readString();
			st.put(key, i);
		}

		for (String s : st.keys())
			System.out.print(s + " ");
		System.out.println();
	}
}
