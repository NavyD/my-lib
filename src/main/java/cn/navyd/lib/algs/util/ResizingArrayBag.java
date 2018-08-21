package cn.navyd.lib.algs.util;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 该类表示数据结构背包。
 * 使用数组实现，具有自动调整大小的功能
 * 该类使用后进先出的顺序保存和遍历元素
 * @author Navy D
 * @date 20170925094405
 * @param <Item>
 */
public class ResizingArrayBag<Item> implements Iterable<Item> {
	// 保存元素数组
    private Item[] a;
    // 背包元素数量
    private int n;

    /**
     * 初始化一个初始容量为2的空背包
     */
    public ResizingArrayBag() {
    	this(2);
    }

    /**
     * 初始化一个初始容量为cap的空背包
     * @param cap
     */
    @SuppressWarnings("unchecked")
	public ResizingArrayBag(int cap) {
    	 a = (Item[]) new Object[cap];
         n = 0;
    }

    /**
	 * 如果背包中没有元素就返回true
	 * @return 是否为空
	 * @author Navy D
	 * @date 20170714161615
	 */
    public boolean isEmpty() {
        return n == 0;
    }

    /**
	 * 返回背包中的元素数量
	 * @return 栈的大小
	 * @author Navy D
	 * @date 20170714161535
	 */
    public int size() {
        return n;
    }

    // 自动调整数组大小
    private void resize(int capacity) {
        assert capacity >= n;
        Item[] temp = Arrays.copyOfRange(a, 0, capacity);
        a = temp;
    }

    /**
     * 添加一个元素到背包
     * @param item
     * @author Navy D
     * @date 20170925094148
     */
    public void add(Item item) {
        if (n == a.length) resize(a.length << 1);    // double size of array if necessary
        a[n++] = item;                            // add item
    }


    /**
     * 返回一个后进先出的顺序遍历对象iterator
     * @author Navy D
     * @date 20170925094222
     */
    public Iterator<Item> iterator() {
        return new ArrayIterator();
    }

    // an iterator, doesn't implement remove() since it's optional
    private class ArrayIterator implements Iterator<Item> {
        private int i = 0;
        public boolean hasNext()  { return i < n;                               }
        public void remove()      { throw new UnsupportedOperationException();  }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return a[i++];
        }
    }

    public static void main(String[] args) {
        ResizingArrayBag<String> bag = new ResizingArrayBag<String>();
        bag.add("Hello");
        bag.add("World");
        bag.add("how");
        bag.add("are");
        bag.add("you");

        for (String s : bag)
          System.out.println(s);
    }

}