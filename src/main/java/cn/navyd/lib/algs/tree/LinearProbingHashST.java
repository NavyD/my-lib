package cn.navyd.lib.algs.tree;

import cn.navyd.lib.algs.util.In;
import cn.navyd.lib.algs.util.Queue;

/**
 * 线性探测的散列表：使用数组中的空位解决碰撞(碰撞时+1)
 * 性能：键簇的长度决定性能
 * 当元素占数组一半时，命中约1.5次，未命中2.5次
 * @author Navy D
 * @date 20170830205829
 * @param <Key>
 * @param <Value>
 */
public class LinearProbingHashST<Key, Value> implements SimpleSymbolTable<Key, Value> {
	private int n;//散列表中的元素数量
	private int m;//散列表大小
	private Key[] keys;//键
	private Value[] vals;//值

	public LinearProbingHashST() {
		this(16);
	}

	@SuppressWarnings("unchecked")
	public LinearProbingHashST(int capacity) {
		if (capacity <= 0)
			throw new IllegalArgumentException();
		this.m = capacity;
		this.keys = (Key[]) new Object[m];
		this.vals = (Value[]) new Object[m];
	}

	private int hash(Key key) {
		//将符号最高位置为0，散列到数组中
		return (key.hashCode() & 0x7fffffff) % m;
	}

	private void resize(int capacity) {
		LinearProbingHashST<Key, Value> t = new LinearProbingHashST<>(capacity);
		for (int i = 0; i < m; i++) {
			if (keys[i] != null)
				t.put(keys[i], vals[i]);
		}
		keys = t.keys;
		vals = t.vals;
		m = t.m;
	}

	public void put(Key key, Value val) {
		//如果元素超过数组一半就扩大数组
		if (n >= m/2)
			resize(m*2);
		int i = 0;
		//探测key的散列值是否被占用，是就寻找下一个直到找到替换或 插入键簇最后
		for (i = hash(key); keys[i] != null; i = (i+1)%m)
			if (keys[i].equals(key)) {
				vals[i] = val;
				return;
			}
		keys[i] = key;
		vals[i] = val;
		n++;
	}

	public Value get(Key key) {
		//在key的键簇中查找
		for (int i = hash(key); keys[i] != null; i = (i+1)%m)
			if (keys[i].equals(key))
				return vals[i];
		return null;
	}

	/**
	 * delete不能简单的将key所在的位置置为null，由于get等都是依照键簇来寻找的
	 * 一旦将key置为null后，一个簇将变为两个，关键是key后面的元素的hash可能就是key的
	 * 位置，必须将后面的元素变成原来的hash位置:
	 * 1.将key元素置为null
	 * 2.将key所在的簇后面的元素重新插入散列表
	 * @author Navy D
	 * @date 20170830211846
	 */
	@Override
	public void delete(Key key) {
		if (!contains(key))
			return;
		int i = hash(key);
		//找到key对应的下标
		while (!key.equals(keys[i]))
			i = (i + 1) % m;
		keys[i] = null;
		vals[i] = null;
		//key的下一个开始
		i = (i + 1) % m;
		//将key对应的簇重新插入hash对应的位置
		while (keys[i] != null) {
			Key keyToRedo = keys[i];
			Value valToRedo = vals[i];
			keys[i] = null;
			vals[i] = null;
			n--;
			put(keyToRedo, valToRedo);
			i = (i + 1) %m;
		}
		//删除key成功数量减1
		n--;
		//当数组大小在1/8~1/2时使用率较好
		if (n > 0 && n <= m/8)
			resize(m/2);

	}

	@Override
	public boolean contains(Key key) {
		if (key == null)
			throw new IllegalArgumentException();
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
		for (int i = 0; i < m; i++)
			if (keys[i] != null)
				queue.enqueue(keys[i]);
		return queue;
	}

	public static void main(String[] args) {
		LinearProbingHashST<String, Integer> st = new LinearProbingHashST<>();
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
