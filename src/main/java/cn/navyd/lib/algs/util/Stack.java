package cn.navyd.lib.algs.util;

public interface Stack<E> extends Iterable<E> {
    int size();
    
    boolean isEmpty();
    
    void push(E e);
    
    /**
     * 返回并移除最近添加的元素。
     * @return
     */
    E pop();
    
    /**
     * 返回最近添加的元素
     * @return
     */
    E peek();
}
