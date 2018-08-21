package cn.navyd.lib.algs.util;

import java.util.AbstractCollection; 
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 一个背包类。支持插入和以特定的顺序遍历
 * 使用了一个静态单链表类实现。背包中的顺序和栈一样以LIFO的顺序存储
 * @author Navy D
 * @param <Item>
 */
public class Bag<Item> extends AbstractCollection<Item> {
    // 最近插入的元素
    private Node<Item> first;
    // 背包的元素数量
    private int size;
    
    public Bag() {
        this.first = null;
        this.size = 0;
    }
    
    public Bag(Collection<Item> c) {
        if (c == null)
            throw new NullPointerException();
        addAll(c);
    }
    
    /**
     * 以LIFO顺序保存插入元素，当前插入元素在遍历时第一个被遍历
     */
    @Override
    public boolean add(Item e) {
        first = new Node<Item>(e, first);
        size ++;
        return true;
    }
    
    @Override
    public Iterator<Item> iterator() {
        return new BagIterator();
    }

    @Override
    public int size() {
        return size;
    }
    
    private class BagIterator implements Iterator<Item> {
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
    
    // 静态内部单链表
    private static class Node<Item> {
        Item item;
        Node<Item> next;
        public Node(Item item, Node<Item> next) {
            this.item = item;
            this.next = next;
        }
    }
}
