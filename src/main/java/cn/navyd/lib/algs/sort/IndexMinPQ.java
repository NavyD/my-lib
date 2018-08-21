package cn.navyd.lib.algs.sort;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>索引优先最小队列：
 * 允许用例引用优先队列中的元素， 能通过索引快速访问最优先优先元素</p>
 * <p>
 * 实现：为每个元素关联一个索引，并使用键key数组存储索引k
 * 使用pq堆数组表示堆中某个位置有一个索引k.使用一个映射数组qp表示索引k在堆中的位置
 * </p>
 * 优先队列的实现是将索引数组堆有序化，使用索引通过映射数组找到该索引在堆中的位置
 * <p>
 * 时间复杂度：与logN成正比 sink()与swim() 的复杂度是lgN
 * 空间与队列容量成正比。
 * 其中changeKey，insert，delete delmin操作均为lgN</p>
 * @author Navy D
 * @date 20170811215118
 * @param <Key>
 */
public class IndexMinPQ<Key extends Comparable<? super Key>> implements Iterable<Integer> {
	//pq堆中元素数量
	private int n;
	//最大的索引不能超过maxN-1,数组元素最多maxN，索引从0开始
	private int maxN;
	/**
	 * 结构：k为索引，n为索引k堆有序化后的下标
	 * pq[n]=k;qp[k]=n;keys[k]=item
	 */
	//保存引用索引的堆数组，代替引用作为堆
	// pq存储的是堆中的位置关联的索引值
	private int[] pq;
	//索引堆数组的映射qp[pq[k]] = pq[qp[k]] = k，用于使用原索引k取出对应的堆下标，再通过堆取出队列数组元素
	// qp映射数组存储的是索引k在堆中的位置
	private int[] qp;
	//从0开始的优先队列数组，存储键的数组
	private Key[] keys;

	/**
	 * 初始化为一个固定大小maxN的最小优先索引队列
	 * @param maxN
	 */
	@SuppressWarnings("unchecked")
	public IndexMinPQ(int maxN) {
		if (maxN < 0)
			throw new IllegalArgumentException();
		n = 0;
		//索引上限maxN-1
		this.maxN = maxN;
		pq = new int[maxN + 1];
		//只有堆才从1开始，其他的都是从0开始到maxN-1
		qp = new int[maxN];
		keys =(Key[]) new Comparable[maxN];
		//
		for (int i = 0; i < qp.length; i++)
			qp[i] = -1;
	}
	/**
	 * 插入一个元素key并与索引k关联，以后在队列可以通过k对引用操作
	 * @param k
	 * @param key
	 * @author Navy D
	 * @date 20170811211221
	 */
	public void insert(int k, Key key) {
		if (contains(k))
			throw new IllegalArgumentException("index is already in the priority queue");
		n++;
		//将原数组索引对应堆与堆映射
		pq[n] = k;
		qp[k] = n;
		keys[k] = key;
		/**
		 * 将堆上浮有序化：虽然pq[n]=不再是k，但映射数组qp[不再是k]=n在同步改变pq[不再是n]=k,qp[k]=不再是n
		 * 看上去好像k删去t[pq[qp[k]]]没用不如keys[k];但是这是优先队列使用索引来表示优先队列
		 * 只用读取pq[1]开始就是最小，是对索引代表的值进行排序索引
		 */
		swim(n);
	}

	/**
	 * 将索引为k的元素设为key
	 * @param k
	 * @param key
	 * @author Navy D
	 * @date 20170811214604
	 */
	public void changeKey(int k, Key key) {
		 if (k < 0 || k >= maxN)
			 throw new IndexOutOfBoundsException();
		if (!contains(k))
			throw new NoSuchElementException("index is not int the priority queue");
		//类似于将堆中元素替换，需要从新有序
		int index = qp[k];
		keys[k] = key;
		//如果父节点的值比当前节点大则当前节点上浮，否则下沉
//		if (greater(k/2, k))//不能使用索引k,greater是用堆中下标再联系队列值进行比较
		// 比较的是堆中index位置的新的key与父节点index/2的key的大小值
		// if (greater(index/2, index))
		swim(index);
		// else
		sink(index);
	}

	/**
	 * 返回索引k关联的键
	 * @param k
	 * @return
	 * @author Navy D
	 * @date 20171004125223
	 */
	public Key keyOf(int k) {
		return get(k);
	}

	/**
	 * 返回索引k关联的键
	 * @param k
	 * @return
	 * @author Navy D
	 * @date 20171004125323
	 */
	public Key get(int k) {
		if (k < 0 || k >= maxN)
			throw new IndexOutOfBoundsException();
		if (!contains(k))
			throw new NoSuchElementException("index is not in the priority queue");
		return keys[k];
	}

	/**
	 * 如果索引k已经存在队列中，则返回true
	 * @param k
	 * @return
	 * @author Navy D
	 * @date 20170811211355
	 */
	public boolean contains(int k) {
		//索引最大为maxN-1
		if (k < 0 || k >= maxN)
			throw new IndexOutOfBoundsException();
		// qp存储值为-1表示不在堆中
		return qp[k] != -1;
	}

	/**
	 * 删除索引k和关联的元素
	 *
	 * @param k
	 * @author Navy D
	 * @date 20170811211703
	 */
	public void delete(int k) {
		if (k < 0 || k >= maxN)
			throw new IndexOutOfBoundsException();
		if (!contains(k))
			throw new NoSuchElementException("index is not the priority queue");
		// 取出堆pq中k对应的下标
		int index = qp[k];
		// 交换堆index与n，去掉交换过去的index(无法读取n)
		exch(index, n--);
		// 将交换来的小元素下沉有序化
		sink(index);
		/*
		 * 由于堆中index(n中)任然存在，pq[index] 存在,qp[pq[index]] = index也存在，
		 * keys[pq[index]]存在，改变的是index在sink()后变成对应另外的索引值a,pq[index] = a; qp[a] =
		 * index;只有qp[k]没变，keys[k]没变
		 */
		keys[k] = null;
		qp[k] = -1;
	}

	/**
	 * 返回最小元素
	 * @return
	 * @author Navy D
	 * @date 20170811211824
	 */
	public Key min() {
		 if (isEmpty())
			 throw new NoSuchElementException("Priority queue underflow");
		return keys[minIndex()];
	}
	/**
	 * 返回最小元素的索引
	 * @return
	 * @author Navy D
	 * @date 20170811211846
	 */
	public int minIndex() {
		 if (isEmpty())
			 throw new NoSuchElementException("Priority queue underflow");
		return pq[1];
	}

	/**
	 * 删除队列最小元素并返回其索引
	 * @return
	 * @author Navy D
	 * @date 20170811212016
	 */
	public int delMin() {
		if (isEmpty())
			throw new NoSuchElementException();
		//取出最小值key的索引
		int min = pq[1];
		//交换
		exch(1, n--);
		//堆下沉有序化
		sink(1);
		//序列映射qp[k] = N pq[n] = k;表示这个索引不存在了
		qp[min] = -1;
		//删除元素
		keys[min] = null;
		//置索引为-1，不再需要这个keys[pq[k]]即keys[-1]
		pq[n+1] = -1;
		return min;
	}

	/**
	 * 如果队列为空，则返回true
	 * @return
	 * @author Navy D
	 * @date 20170811212106
	 */
	public boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * 返回队列元素数量
	 * @return
	 * @author Navy D
	 * @date 20170811212128
	 */
	public int size() {
		return n;
	}


	/******************************	私有方法	********************************************/


	/**
	 * 对堆中节点上浮有序化
	 * @param k
	 * @author Navy D
	 * @date 20170811212147
	 */
	private void swim(int k) {
		//如果父节点k/2比子节点k大就交换
		while (k > 1 && greater(k >>> 1, k)) {
			exch(k >>> 1, k);
			k >>>= 1;
		}
	}


	/**
	 * 对堆中节点下沉有序化
	 * @param k
	 * @author Navy D
	 * @date 20170811212219
	 */
	private void sink(int k) {
		while (k << 1 <= n) {
			int j = k << 1;
			//如果左子节点更大，则取小的右子节点
			if (j < n && greater(j, j+1))
				j++;
			//如果父节点k较小，堆有序完成
			if (!greater(k, j))
				break;
			exch(k, j);
			k = j;
		}
	}
	/**
	 * 如果堆中i对应的索引数组值比j大则返回true
	 * @param i
	 * @param j
	 * @return
	 * @author Navy D
	 * @date 20170811201656
	 */
	private boolean greater(int i, int j) {
		return keys[pq[i]].compareTo(keys[pq[j]]) > 0;
	}
	/**
	 * 交换堆pq中i和j对应的索引，并改变映射qp[]中下标与值
	 * i与j是堆中的位置
	 * @param i
	 * @param j
	 * @author Navy D
	 * @date 20170811201917
	 */
	private void exch(int i, int j) {
		// 改变索引
		int temp = pq[i];
		pq[i] = pq[j];
		pq[j] = temp;
		qp[pq[i]] = i;
		qp[pq[j]] = j;
	}

	/**
	 * 队列的字符串表示，顺序不是优先顺序排列，而是堆数组中顺序表示，只能作参考
	 * @author Navy D
	 * @date 20171004123412
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 1; i <= n; i++) {

			sb.append(pq[i] + "-" + keys[pq[i]] + ", ");
		}
		sb.delete(sb.length()-2, sb.length());
		sb.append("]");
		return sb.toString();
	}

	/**
	 * 返回优先队列元素副本的集合
	 * @author Navy D
	 * @date 20171004123347
	 */
	@Override
	public Iterator<Integer> iterator() {
		return new HeapIterator();
	}

	private class HeapIterator implements Iterator<Integer> {
		// create a new pq
		private IndexMinPQ<Key> copy;

		// add all elements to copy of heap
		// takes linear time since already in heap order so no keys move
		public HeapIterator() {
			copy = new IndexMinPQ<Key>(pq.length - 1);
			for (int i = 1; i <= n; i++)
				copy.insert(pq[i], keys[pq[i]]);
		}

		public boolean hasNext() {
			return !copy.isEmpty();
		}

		public Integer next() {
			if (!hasNext())
				throw new NoSuchElementException();
			return copy.delMin();
		}
	}

	public static void main(String[] args) {
		String[] strings = { "it", "was", "the", "best", "of", "times", "it", "was", "the", "worst" };

		IndexMinPQ<String> pq = new IndexMinPQ<String>(strings.length);
		for (int i = 0; i < strings.length; i++) {
			pq.insert(i, strings[i]);
		}

		// delete and print each key
		while (!pq.isEmpty()) {
			int i = pq.delMin();
			System.out.println(i + " " + strings[i]);
		}
		System.out.println();

		// reinsert the same strings
		for (int i = 0; i < strings.length; i++) {
			pq.insert(i, strings[i]);
		}

		pq.changeKey(3, "xxx");
		// print each key using the iterator
		for (int i : pq) {
			System.out.println(i + " " + strings[i]);
		}
		System.out.println(pq);

		while (!pq.isEmpty()) {
			pq.delMin();
		}
	}



}
