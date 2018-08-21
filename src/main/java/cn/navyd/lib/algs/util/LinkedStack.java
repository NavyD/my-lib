package cn.navyd.lib.algs.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 一个使用单链表的stack的实现
 * @author navyd
 *
 * @param <Item>
 */
public class LinkedStack<Item> implements Stack<Item> {
    private Node<Item> first;
    private int size;
    
    public LinkedStack() {
        first = null;
        size = 0;
    }
    
    /**
     * 在栈中以LIFO的顺序返回一个iterator对象遍历所有元素
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

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public void push(Item e) {
        first = new Node<Item>(e, first);
        size ++;
    }

    @Override
    public Item pop() {
        if (isEmpty())
            throw new NoSuchElementException("Stack underflow");
        Item item = first.item;
        first = first.next;
        size--;
        return item;
    }

    @Override
    public Item peek() {
        if (isEmpty())
            throw new NoSuchElementException("Stack underflow");
        return first.item;
    }
    
    /**
     * 打印链表元素
     * @author Navy D
     * @date 20170714161818
     */
    @Override
    public String toString() {
         StringBuilder s = new StringBuilder();
            for (Item item : this) {
                s.append(item);
                s.append(' ');
            }
            return s.toString();
    }
    
    public static <Item> LinkedStack<Item> of() {
        return new LinkedStack<>();
    }
    
    // 单向链表
    private static class Node<Item> {
        Item item;
        Node<Item> next;
        public Node(Item item, Node<Item> next) {
            this.item = item;
            this.next = next;
        }
    }
}
