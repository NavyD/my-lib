package cn.navyd.lib.algs.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 一个背包类。支持插入和以特定的顺序遍历
 * 使用了一个静态单链表类实现。背包中的顺序和栈一样以LIFO的顺序存储
 * @author Navy D
 * @param <Item>
 */
public class BagV<Item> implements Iterable<Item> {
	// 最近插入的元素
	private Node<Item> first;
	// 背包的元素数量
	private int n;

	// 静态内部单链表
	private static class Node<Item> {
		Item item;
		Node<Item> next;

		public Node(Item item, Node<Item> next) {
			this.item = item;
			this.next = next;
		}

	}

	/**
	 * 初始化一个空背包
	 */
	public BagV() {
		first = null;
		n = 0;
	}

	/**
	 * 如果背包是空的就返回true
	 * @return
	 * @author Navy D
	 * @date 20170922205238
	 */
	public boolean isEmpty() {
		return first == null;
	}

	/**
	 * 返回背包的元素数量
	 * @return
	 * @author Navy D
	 * @date 20170922205319
	 */
	public int size() {
		return n;
	}

	/**
	 * 添加一个元素到背包
	 * @param item
	 * @author Navy D
	 * @date 20170922205357
	 */
	public void add(Item item) {
		//与stack的push方法一致
		first = new Node<>(item, first);
		n++;
	}

	/**
	 * 在背包中以特定的顺序返回一个iterator对象遍历所有元素
	 * @author Navy D
	 * @date 20170922205518
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

	/**
	 * 打印背包元素
	 * @author Navy D
	 * @date 20170714161818
	 */
	public String toString() {
		 StringBuilder s = new StringBuilder();
	        for (Item item : this) {
	            s.append(item);
	            s.append(' ');
	        }
	        return s.toString();
	}


	public static void main(String[] args) {
		BagV<String> bag = new BagV<>();
		String[] str = {"a", "b", "c", "d", "e"};
		for (String s : str){
			bag.add(s);
		}
		for (String s : bag)
			System.out.println(s);

		System.out.println(bag);

	}
}
