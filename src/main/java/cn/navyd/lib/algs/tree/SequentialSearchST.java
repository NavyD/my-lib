package cn.navyd.lib.algs.tree;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 链表实现的无序符号表：
 *
 * 对于get()与put()的实现都是使用顺序查找遍历链表
 *
 * 复杂度：查找N次比较，插入N不同个值需要N^2/2(n..1求和公式)
 * @author Navy D
 * @date 20170817195849
 * @param <Key>
 * @param <Value>
 */
public class SequentialSearchST<Key, Value> {
	private Node first;
	private int n;

	private class Node {
		Key key;
		Value val;
		Node next;
		public Node (Key key, Value val, Node next) {
			this.key = key;
			this.val = val;
			this.next = next;
		}
	}

	public SequentialSearchST() {

	}

	/**
	 * 获取表中key对应的值val，如果key不存在则返回null
	 * @param key
	 * @return
	 * @author Navy D
	 * @date 20170817204316
	 */
	public Value get(Key key) {
		if (key == null || first == null)
			throw new IllegalArgumentException();
		for (Node cur = first; cur != null; cur = cur.next) {
			if (key.equals(cur.key))
				return cur.val;
		}
		return null;
	}

	/**
	 * 将键值对存入表中，如果val为空，则将key从表中删除
	 * @param key
	 * @param val
	 * @author Navy D
	 * @date 20170817203847
	 */
	public void put(Key key, Value val) {
		if (key == null)
			throw new NoSuchElementException();
		//没有这句就是延时性删除允许(key, null)，否则即时删除，删除键
		if (val == null) {
			delete(key);
			return;
		}

		//如果key已经存在
		for (Node cur = first; cur != null; cur = cur.next) {
			if (key.equals(cur.key)) {
				cur.val = val;
				return;
			}
		}
		//key不存在时
		first = new Node(key, val, first);
		n++;
	}



//	public void delete(Key key) {
//		if (key == null)
//			throw new IllegalArgumentException();
//		
//		if (key.equals(first.key)) {
//			first = first.next;
//			return;
//		}
//
//		for (Node cur = first; cur != null; cur = cur.next) {
//			//如果key是最后一个元素
//			if (cur.next.next == null) {
//				cur.next = null;
//			}
//			//使用当前节点的下一个节点删除，再连接当前节点与下下个节点
//			else if (key.equals(cur.next.key)) {
//				cur.next = cur.next.next;
//			}
//		}
//	}

	/**
	 * 从表中删去键key与对应的值val
	 * @param key
	 * @author Navy D
	 * @date 20170817203941
	 */
	public void delete(Key key) {
		first = delete(first, key);
	}
	/**
	 * 从节点x开始删除节点值为key的节点，如果有key节点，就删除
	 * @param x
	 * @param key
	 * @return
	 * @author Navy D
	 * @date 20170816135318
	 */
	private Node delete(Node x, Key key) {
		if (x == null)
			return null;
		if (key.equals(x.key)) {
			n--;
			return x.next;
		}
		//还是通过保存当前node的上一个节点，返回当前node的下一个节点再连接
		//当最后delete返回一个值时(null或node)，会一直按原路完成x.next的赋值，最后是first.next=second
		x.next = delete(x.next, key);
		return x;
	}

	/**
	 * 如果key在表中存在值val，则返回true
	 * @param key
	 * @return
	 * @author Navy D
	 * @date 20170817204021
	 */
	public boolean contains(Key key) {
		if (key == null)
			throw new IllegalArgumentException();
		return get(key) != null;
	}

	/**
	 * 如果表中没有元素，则返回true
	 * @return
	 * @author Navy D
	 * @date 20170817204120
	 */
	public boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * 返回表当前键值对的数量
	 * @return
	 * @author Navy D
	 * @date 20170817204146
	 */
	public int size() {
		return n;
	}

	/**
	 * 快速遍历表中keys集合，返回一个iterable对象
	 * @return
	 * @author Navy D
	 * @date 20170817204215
	 */
	public Iterable<Key> keys() {


		return new Iterable<Key> () {

			@Override
			public Iterator<Key> iterator() {
				return new Iterator<Key> () {
					Node nextNode = first;

					@Override
					public boolean hasNext() {
						return nextNode != null;
					}

					@Override
					public Key next() {
						Node next = nextNode;
						nextNode = nextNode.next;
						return next.key;
					}

				};
			}

		};
	}


	public static void main(String[] args) {
		SequentialSearchST<Integer, String> ss = new SequentialSearchST<>();
		String[] s = "a b c".split(" ");
		int i = 0;
		for (String str : s)
			ss.put(i++, str);
		for (Integer a : ss.keys()) {
			System.out.format("%d ", a);
		}
		System.out.println();
		ss.delete(0);
		for (Integer a : ss.keys()) {
			System.out.format("%d ", a);
		}
	}


}
