package cn.navyd.lib.algs.sort;

public class Sorts {
    
    public static <T extends Comparable<? super T>> void insertion(T[] a) {
        for (int i = 1; i < a.length; i++) {
            T val = a[i];
            int j = i;
            while (j > 0 && less(val, a[j-1]))
                // 注意：先执行j--，再执行后面的j ===> a[j--] = a[j-1]会导致先j--再j-1越界
                a[j--] = a[j];
            a[j] = val;
        }
    }
    
    /**
     * 选择排序。
     * @param a
     */
    public static <T extends Comparable<? super T>> void selection(T[] a) {
        for (int i = 0; i < a.length; i++) {
            int min = i;
            for (int j = i + 1; j < a.length; j++)
                if (less(a[j], a[min]))
                    min = j;
            exch(a, i, min);
        }
    }
    
    /**
     * 如果数组元素是顺序递增的，则返回true
     * @param a
     * @return
     */
    public static <T extends Comparable<? super T>> boolean isSorted(T[] a) {
        for (int i = 1; i < a.length; i++)
            if (less(a[i], a[i-1]))
                return false;
        return true;
    }
    
    /**
     * 交换数组i与j的值。
     * <p>交换两个变量的值常用方法：
     * <ol>
     * <li>使用临时变量。适用性最广
     * <li>加减法。适用于所有允许加减的类型。如：{@code a = 2, b = 5; ==> a = a + b : 7; b = a - b : 2; a = a - b : 5}.
     * 缺点：可能会溢出或精度损失。
     * <li>异或'^'。仅适用与除double, float, boolean之外的原始类型。如：{@code a = 0010, b = 0101; ==> a = a ^ b : 0111; b = a ^ b : 0010; a = a ^ b : 0101}
     * </ol>
     */
    private static <T extends Comparable<? super T>> void exch(T[] a, int i, int j) {
        T temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
    
    /**
     * 如果 {@code v < w} 则返回true
     * @param v
     * @param w
     * @return
     */
    private static <T extends Comparable<? super T>> boolean less(T v, T w) {
        return v.compareTo(w) < 0;
    }
    
    /**
     * 如果数组a中下标i表示的元素比j表示的元素小，则返回true。即 a[i] < a[j]
     * @param a
     * @param i
     * @param j
     * @return
     */
//    private static <T extends Comparable<? super T>> boolean less(T[] a, int i, int j) {
//        return a[i].compareTo(a[j]) < 0;
//    }
}
