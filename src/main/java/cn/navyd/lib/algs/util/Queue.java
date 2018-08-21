package cn.navyd.lib.algs.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 一个先入先出(FIFO)队列.
 * 使用一个静态单链表类实现
 * @author Navy D
 * @param <Item>
 */
public class Queue<Item> implements Iterable<Item> {
	// 最早添加的元素连接
	private Node<Item> first;
	// 最近添加的元素连接
	private Node<Item> last;
	// 队列的元素数量
	private int n;

	//静态单链表
	private static class Node<Item> {
		Item item;
		Node<Item> next;

		public Node(Item item, Node<Item> next) {
			this.item = item;
			this.next = next;
		}
	}

	/**
	 * 初始化一个空队列
	 */
	public Queue() {
		first = null;
		last = null;
		n = 0;
	}

	/**
	 * 如果队列为空就返回true
	 * @return
	 * @author Navy D
	 * @date 20170922211015
	 */
	public boolean isEmpty() {
		return first == null;
	}

	/**
	 * 返回队列元素数量
	 * @return
	 * @author Navy D
	 * @date 20170922211036
	 */
	public int size() {
		return n;
	}

	/**
	 * 添加一个元素到队尾
	 * @param item
	 * @author Navy D
	 * @date 20170922211048
	 */
	public void enqueue(Item item) {
		//向队尾添加元素
		Node<Item> oldLast = last;
		last = new Node<>(item, null);
		// 队列中没有元素
		if (isEmpty())
			first = last;
		else
			oldLast.next = last;
		n++;
	}

	/**
	 * 返回并移除队列头元素
	 * @return
	 * @author Navy D
	 * @date 20170922211113
	 */
	public Item dequeue() {
		if (isEmpty())
			throw new NoSuchElementException("Queue underflow");
		Item item = first.item;
		first = first.next;
		n--;
		return item;
	}

	/**
	 * 返回队列的字符串表示
	 * @author Navy D
	 * @date 20170922211745
	 */
	public String toString() {
		StringBuilder s = new StringBuilder();
		for (Item item : this) {
			s.append(item);
			s.append(' ');
		}
		return s.toString();
	}

	/**
	 * 返回一个以FIFO的顺序遍历元素的iterator对象
	 * @author Navy D
	 * @date 20170922211535
	 */
	@Override
	public Iterator<Item> iterator() {
		return new ListIterator();
	}

	private class ListIterator implements Iterator<Item> {
		private Node<Item> current = first;

		@Override
		public boolean hasNext() {
			return current != null;
		}

		@Override
		public Item next() {
			if (!hasNext())
				throw new NoSuchElementException();
			Item item = current.item;
			current = current.next;
			return item;
		}

	}

	public static void main(String[] args) {
		Queue<String> queue = new Queue<>();
		String[] str = {"a", "b", "c", "d", "e"};
		for (String s : str){
			queue.enqueue(s);
		}
		for (String s : queue)
			System.out.print(s);

		System.out.println(queue);
		int sz = queue.size();
		for (int i = 0; i < sz; i++)
			System.out.println(queue.dequeue());

	}

}
