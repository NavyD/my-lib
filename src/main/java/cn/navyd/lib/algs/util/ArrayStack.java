package cn.navyd.lib.algs.util;

import java.util.ArrayDeque;
import java.util.Iterator;

/**
 * ArrayStack类仅封装java.util.ArrayDeque类实现栈的操作
 */
public class ArrayStack<Item> implements Stack<Item> {
	// 循环数组实现的双向队列
	private ArrayDeque<Item> stack;

	/**
	 * 创建一个空栈
	 */
	public ArrayStack() {
		stack = new ArrayDeque<>();
	}

	/**
	 * 如果栈为空就返回true
	 */
	public boolean isEmpty() {
		return stack.isEmpty();
	}

	/**
	 * 返回栈中的元素数量
	 */
	public int size() {
		return stack.size();
	}

	/**
	 * 添加一个元素入栈
	 */
	public void push(Item item) {
		stack.push(item);
	}

	/**
	 * 返回并从栈中删除最近添加的元素。
	 */
	public Item pop() {
		return stack.pop();
//		return stack.pollFirst();
	}

	/**
	 * 返回但不移除最近添加的节点元素，
	 */
	public Item peek() {
//		return stack.peekFirst();
		return stack.peek();
	}

	/**
	 * 打印栈中元素
	 */
	public String toString() {
		return stack.toString();
	}

	/**
	 * 在栈中以LIFO的顺序返回一个iterator对象遍历所有元素
	 */
	@Override
	public Iterator<Item> iterator() {
		return stack.iterator();
	}

	public static void main(String[] args) {
		ArrayStack<String> stack = new ArrayStack<>();
		// Stack<String> stack = new Stack<>();
		String[] str = { "a", "b", "c", "d", "e" };
		for (String s : str) {
			stack.push(s);
		}
		System.out.println(stack.size());
		System.out.println(stack);
		while (!stack.isEmpty())
			System.out.println(stack.pop());
	}

}
