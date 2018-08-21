package cn.navyd.lib.algs.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 该类是一个后进先出(LIFO)的栈。
 * 使用数组实现，具有自动数组调整大小的功能。
 * @author Navy D
 * @date 20170922232113
 * @param <Item>
 */
public class ResizingArrayStack<Item> implements Iterable<Item> {
	// 栈元素
	private Item[] a;
	// 元素数量
	private int n;

	/**
	 * 初始化一个初始大小为2的空栈
	 */
	public ResizingArrayStack() {
		this(2);
	}

	/**
	 * 初始化一个初始大小为cap的空栈
	 * @param cap
	 */
	@SuppressWarnings("unchecked")
	public ResizingArrayStack(int cap) {
		a = (Item[]) new Object[cap];
		n = 0;
	}


	/**
	 * 如果栈中没有元素就返回true
	 * @return 是否为空
	 * @author Navy D
	 * @date 20170714161615
	 */
	public boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * 返回栈中的元素数量
	 * @return 栈的大小
	 * @author Navy D
	 * @date 20170714161535
	 */
	public int size() {
		return n;
	}

	//调整数组大小，使用arrays类代替循环提高性能
	private void resize(int capacity) {
		assert capacity >= n;
		// 将数组a中的元素从0开始复制到capacity为止，如果capacity大于a.length的数组部分会填充为null或原始类型的初始值
		Item[] temp = Arrays.copyOfRange(a, 0, capacity);
		a = temp;
	}

	/**
	 * 添加一个元素入栈
	 * @param item
	 * @author Navy D
	 * @date 20170714161454
	 */
	public void push(Item item) {
		if (n == a.length)
			resize(a.length << 1);
		a[n++] = item;
	}

	/**
	 * 返回并从栈中删除最近添加的元素。
	 * @return 最近添加的元素
	 * @author Navy D
	 * @date 20170714161305
	 */
	public Item pop() {
		if (isEmpty())
			throw new NoSuchElementException("Stack underflow");
		Item item = a[n-1];
		a[n-1] = null;
		n--;
		// 数组太大检测是否小于数组长度1/4，最佳性能
		if (n > 0 && a.length >>> 2 == n)
			resize(a.length >>> 1);
		return item;
	}

	/**
	 * 返回(不删除)最近添加的元素。
	 * @return
	 * @author Navy D
	 * @date 20170922232649
	 */
	public Item peek() {
		if (isEmpty())
			throw new NoSuchElementException("Stack underflow");
		return a[n - 1];
	}

	/**
	 * 在栈中以LIFO的顺序返回一个iterator对象遍历所有元素
	 * @author Navy D
	 * @date 20170922205518
	 */
	@Override
	public Iterator<Item> iterator() {
		return new ReverseArrayIterator();
	}

	private class ReverseArrayIterator implements Iterator<Item> {
		private int i = n - 1;

		@Override
		public boolean hasNext() {
			return i >= 0;
		}

		@Override
		public Item next() {
			if (!hasNext())
				throw new NoSuchElementException();
			return a[i--];
		}
	}

	public static void main(String[] args) {
		String[] str = {"a", "b", "c", "d", "e"};
		ResizingArrayStack<String> stack = new ResizingArrayStack<>(1);
		for (String s : str){
			stack.push(s);
			//System.out.println(Arrays.toString(stack.a));
		}
		for (String s : stack) {
			System.out.print(s+" ");
		}
		System.out.println();
		int sz = stack.size();
		System.out.println(stack.peek());
		for (int i = 0; i < sz; i++)
			System.out.print(stack.pop() + " ");
	}

//	static void test() {
//		int[] aa = new int[2];
//		aa[1] = 1;
//		int capacity = 4;
//		int[] temp = Arrays.copyOfRange(aa, 0, capacity);
//		System.out.println(Arrays.toString(temp));
//	}
}
