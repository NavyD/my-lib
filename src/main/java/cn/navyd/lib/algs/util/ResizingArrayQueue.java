package cn.navyd.lib.algs.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 该类是一个先进先出的队列。
 * 使用数组实现，具有自动调整大小的功能
 * @author Navy D
 * @date 20170925013008
 * @param <Item>
 */
public class ResizingArrayQueue<Item> implements Iterable<Item> {
	// 队列元素数组
    private Item[] q;
    // 队列元素总数
    private int n;
    // 队列第一个元素下标
    private int first;
    // 队列最后一个元素下标
    private int last;
    // 队列数组的最大长度
    private static final int MAXIMUM_CAPACITY = 1 << 30;

    /**
     * 返回最接近cap大小的2的幂值
     * @param cap
     * @return
     */
    private static final int tableSizeFor(int cap) {
        int n = cap - 1;
        // 将n所有位有效位置为1
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

    /**
     * 初始化一个初始大小为2的空队列
     */
    public ResizingArrayQueue() {
		this(2);
    }

	/**
	 * 初始化一个初始大小为接近cap大小的2次幂的空队列
	 * @param cap
	 */
	@SuppressWarnings("unchecked")
    public ResizingArrayQueue(int cap) {
    	int len = tableSizeFor(cap);
    	q = (Item[]) new Object[len];
    	n = 0;
    	first = 0;
    	last = 0;

    }

	/**
	 * 如果队列中没有元素就返回true
	 * @return 是否为空
	 * @author Navy D
	 * @date 20170714161615
	 */
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
	 * 返回队列中的元素数量
	 * @return 栈的大小
	 * @author Navy D
	 * @date 20170714161535
	 */
    public int size() {
        return n;
    }

    // 调整数组大小
    private void resize(int capacity) {
        assert capacity >= n;
        @SuppressWarnings("unchecked")
		Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            temp[i] = q[(first + i) & (q.length - 1)];
        }
        q = temp;
        first = 0;
        last  = n;
    }

    /**
	 * 添加一个元素入队列
	 * @param item
	 * @author Navy D
	 * @date 20170714161454
	 */
	public void enqueue(Item item) {
		// 如果数组满了就增大一倍
		if (n == q.length)
			resize(q.length << 1);
		q[last++] = item;
		if (last == q.length)
			last = 0;
		n++;
	}

	/**
	 * 返回并移除队列第一个元素
	 * @return
	 * @author Navy D
	 * @date 20170925012747
	 */
	public Item dequeue() {
		if (isEmpty())
			throw new NoSuchElementException("Queue underflow");
		Item item = q[first];
		q[first] = null; // to avoid loitering
		n--;
		first++;
		if (first == q.length)
			first = 0; // wrap-around
		// shrink size of array if necessary
		if (n > 0 && n == q.length >>> 2)
			resize(q.length >>> 1);
		return item;
	}

   /**
    * 仅返回队列第一个元素
    * @return
    * @author Navy D
    * @date 20170925012829
    */
	public Item peek() {
		if (isEmpty())
			throw new NoSuchElementException("Queue underflow");
		return q[first];
	}

	/**
	 * 返回一个FIFO顺序的遍历对象iterator
	 * @author Navy D
	 * @date 20170925012901
	 */
	public Iterator<Item> iterator() {
		return new ArrayIterator();
	}

	// an iterator, doesn't implement remove() since it's optional
	private class ArrayIterator implements Iterator<Item> {
		private int i = 0;

		public boolean hasNext() {
			return i < n;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

		public Item next() {
			if (!hasNext())
				throw new NoSuchElementException();
			Item item = q[(i + first) % q.length];
			i++;
			return item;
		}
	}

	public static void main(String[] args) {
		ResizingArrayQueue<String> queue = new ResizingArrayQueue<String>();
		String s = "slj sdf l sdf f ljsdflajfhsdu  dsjh fkljsd sddlsjdf ljsd lf ij sdjsd  a";
		for (String str : s.split(" "))
			queue.enqueue(str);
		while (!queue.isEmpty())
			System.out.println(queue.dequeue());

		System.out.println("(" + queue.size() + " left on queue)");
	}

}