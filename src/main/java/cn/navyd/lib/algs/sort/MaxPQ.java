package cn.navyd.lib.algs.sort;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * <p>基于堆数组的最大优先队列：
 * 优先队列将当前输入数据分配优先级并总是处理下一个优先级最高的数据</p>
 * <p>
 * 实现：
 * 使用二叉堆的数据结构对元素堆有序，高效的实现了插入与删除元素操作
 * 如果大量需要插入或删除操作，建议使用无序数组或有序数组分别实现
 * </p>
 * 由数组实现的优先队列支持自动改变队列大小(大数组性能非常差)，
 * 支持自定义的排序方法comparator
 * <p>
 * 时间复杂度：插入：lgN+1    删除：2lgN
 * </p>
 * @author Navy D
 * @date 20170810155728
 * @param <Key>
 */
public class MaxPQ<Key extends Comparable<? super Key>> implements Iterable<Key> {
	//优先队列保存数组，从a[1]开始
	private Key[] pq;
	//当前队列元素数量
	private int n;
	//可选的比较器
	private Comparator<Key> comparator;

	/**
	 * 构造一个初始容量为maxN空的优先队列
	 * @param maxN 优先队列存储的元素总数
	 */
	@SuppressWarnings("unchecked")
	public MaxPQ(int initCap) {
		//队列从pq[1]开始
		pq = (Key[])new Comparable[initCap + 1];
		n = 0;
	}
	/**
	 * 构造初始容量为1的空优先队列
	 */
	public MaxPQ() {
		this(1);
	}

	/**
	 * 将一个数组初始化为一个优先队列，初始容量为数组元素数量
	 * @param keys
	 */
	@SuppressWarnings("unchecked")
	public MaxPQ(Key[] keys) {
		n = keys.length;
		pq = (Key[]) new Comparable[n+1];
		//赋值数组元素到队列
		for (int i = 0; i < keys.length; i++)
			pq[i+1] = keys[i];

		//将新数组构造成一个堆(堆排序一部分)
		for (int i = n/2; i >= 1; i--)
			sink(i);

		assert isMaxHeap();
	}

	/**
	 * 构造一个初始容量为maxN的空优先队列，使用指定的比较器排列
	 * @param maxN 队列长度
	 * @param comparator 特定的比较器
	 */
	public MaxPQ(int initCap, Comparator<Key> comparator) {
		this(initCap);
		this.comparator = comparator;
	}
	/**
	 * 将数组初始化为一个优先队列，使用特定的比较器
	 * @param keys
	 * @param comparator
	 */
	public MaxPQ(Key[] keys, Comparator<Key> comparator) {
		this(keys);
		this.comparator = comparator;
	}
	/**
	 * 构造一个初始容量为1的空优先队列，使用指定的比较器排列
	 * @param comparator
	 */
	public MaxPQ(Comparator<Key> comparator) {
		this(1);
		this.comparator = comparator;
	}

	/**
	 * 自动调整数组大小
	 * @param capacity 新的数组容量
	 * @author Navy D
	 * @date 20170811150149
	 */
	@SuppressWarnings("unchecked")
	private void resize(int capacity) {
		//capacity只有两种方式，测试能过就行
		assert capacity > n;

		Key[] temp = (Key[]) new Comparable[capacity];
//		复制到新数组
//		for (int i = 1; i <= n; i++)
//			temp[i] = pq[i];

		System.arraycopy(pq, 1, temp, 1, n);
		pq = temp;
	}

	/**
	 * 如果当前队列没有元素，就返回true
	 * @return
	 * @author Navy D
	 * @date 20170811150433
	 */
	public boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * 返回当前队列元素数量
	 * @return
	 * @author Navy D
	 * @date 20170811150512
	 */
	public int size() {
		return n;
	}

	/**
	 * 返回优先队列当前最大的元素
	 * @return
	 * @author Navy D
	 * @date 20170811150542
	 */
	public Key max() {
		if (isEmpty())
			throw new NoSuchElementException();
		//堆数据结构中根最大
		return pq[1];
	}

	/**
	 * 在队列堆底插入新的元素，并使该元素在堆中有序
	 * @param v
	 * @author Navy D
	 * @date 20170811150826
	 */
	public void insert(Key v) {
		//当前元素数量刚好等于数组长度（除a[0]） 将数组扩大一倍
		if (n == pq.length-1)
			resize(pq.length << 1);
		//以1开始存储元素
		pq[++n] = v;
		//放在最后一个，上浮序列化
		swim(n);

		assert isMaxHeap();
	}

	/**
	 * 删除并返回最大元素(堆顶)，并使下一个堆有序
	 * @return
	 * @author Navy D
	 * @date 20170811151102
	 */
	public Key delMax() {
		if (isEmpty())
			throw new NoSuchElementException("Priority queue underflow");
		// 第一个为根最大
		Key max = pq[1];
		// 将第一个与最后一个交换，移除第一个元素
		exch(1, n);
		pq[n--] = null;
		// 将交换上来的元素下沉序列化
		sink(1);
		//当前队列中元素数量为数组长度的1/4
		if (n > 0 && n == (pq.length - 1) >> 2)
			resize(pq.length >> 1);

		assert isMaxHeap();
		return max;
	}
	/**
	 * 如果数组pq下标i元素比j元素小，则返回true
	 * 如果比较器不为空，当使用其定义的比较顺序
	 * 如果要实用，请分离出comparator需要的less方法，不然会因为在while中经常调用浪费性能
	 * @param i
	 * @param j
	 * @return
	 * @author Navy D
	 * @date 20170811151526
	 */
	private boolean less(int i, int j) {
		// minPQ仅仅改变这里>0
//		if (comparator == null)
//			return pq[i].compareTo(pq[j]) < 0;
//		else
//			return comparator.compare(pq[i], pq[j]) < 0;
		return comparator != null ?
				comparator.compare(pq[i], pq[j]) < 0 : pq[i].compareTo(pq[j]) < 0;
	}

	/**
	 * 交换数组i和j的值
	 * @param i
	 * @param j
	 * @author Navy D
	 * @date 20170811151831
	 */
	private void exch(int i, int j) {
		Key tem = pq[i];
		pq[i] = pq[j];
		pq[j] = tem;
	}

	/**
	 * 使二叉堆由下至上有序化 如果当前节点比父节点大，则交换 否则有序化完成
	 * 复杂度：lgN比较与交换
	 * @param k
	 * @author Navy D
	 * @date 20170811151852
	 */
	private void swim(int k) {
		// 子节点k比父节点k/2大进入循环
		while (k > 1 && less(k >> 1, k)) {
			// 交换父子节点
			exch(k >> 1, k);
			// 将子节点设为父节点继续比较
			k >>= 1;
		}
	}

	/**
	 * 使二叉堆由上至下有序化，如果当前节点比子节点中较大的一个小，则交换，否则有序化完成
	 * 复杂度：2lgN比较,lgN交换
	 * @param k
	 * @author Navy D
	 * @date 20170811152052
	 */
	private void sink(int k) {
		// 2*k子节点要存在
		while (k << 1 <= n) {
			// 左边子节点
			int j = k << 1;
			// 如果右子节点存在且比左子节点大就取右节点
			if (j < n && less(j, j + 1))
				j++;
			// 当前节点比子节点大时完成退出
			if (!less(k, j))
				break;
			// 交换当前节点与子节点中较大的一个
			exch(k, j);
			// k设为交换后的子节点，再与其子节点比较
			k = j;
		}
	}

	/**
	 * 如果从根节点开始堆有序，返回true
	 * @return
	 * @author Navy D
	 * @date 20170811152215
	 */
	private boolean isMaxHeap() {
		return isMaxHeap(1);
	}

	// is subtree of pq[1..n] rooted at k a max heap?
	/**
	 * 如果从k节点开始堆有序，则返回true
	 * @param k
	 * @return
	 * @author Navy D
	 * @date 20170811152333
	 */
	private boolean isMaxHeap(int k) {
		//没有元素了
		if (k > n)
			return true;
		//左子节点
		int left = 2 * k;
		//右子节点
		int right = 2 * k + 1;
		//子节点存在并且大于父节点就说明堆无序，返回false
		if (left <= n && less(k, left))
			return false;
		if (right <= n && less(k, right))
			return false;
		//先遍历完左节点，再右子节点
		return isMaxHeap(left) && isMaxHeap(right);
	}

	/**
	 * 返回队列元素字符串输出
	 * @author Navy D
	 * @date 20170811152746
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (Key t : pq)
			sb.append(t + ", ");
		sb.delete(sb.length()-2, sb.length());
		sb.append("]");
		return sb.toString();
	}

	/**
	 * 返回优先队列元素副本的集合
	 * @author Navy D
	 * @date 20171004005050
	 */
	@Override
	public Iterator<Key> iterator() {
		return new HeapIterator();
	}

	private class HeapIterator implements Iterator<Key> {

		private MaxPQ<Key> copy;

		/**
		 * 构造一个副本maxpq
		 */
		public HeapIterator() {
			if (comparator == null)
				copy = new MaxPQ<Key>(size());
			else
				copy = new MaxPQ<Key>(size(), comparator);
			for (int i = 1; i <= n; i++)
				copy.insert(pq[i]);
		}

		public boolean hasNext() {
			return !copy.isEmpty();
		}

		public Key next() {
			if (!hasNext())
				throw new NoSuchElementException();
			return copy.delMax();
		}
	}


	/**
	 * 返回队列中最小的值
	 * @return
	 * @author Navy D
	 * @date 20170811152909
	 */
	private Key min() {
		int min = 1;
		//在二叉堆中，最小的值一定只存在于叶子节点上，其中n/2是最后一个子节点
		for (int i = n/2 + 1; i <= n; i++)
			if (!less(min, i))
				min = i;
		return pq[min];
	}

	public static void main(String[] args) {
		int n = 10;
		Random r = new Random(7);
		MaxPQ<Integer> pq = new MaxPQ<>();
		Integer[] s = new Integer[n];
		for (int i = 0; i < n; i++) {
			int a = r.nextInt(100);
			pq.insert(a);
			s[i] = a;
			System.out.format("%3s ", a);
		}
//		pq = new MaxPQ<>(s);
		System.out.println();
		System.out.println(pq);
		// System.out.println(pq.min());

		for (int i : pq) {
			System.out.println(i);
		}
	}
}
