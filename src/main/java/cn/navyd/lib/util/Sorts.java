package cn.navyd.lib.util;

import java.util.Random;

public class Sorts {
    private static final Random RANDOM = new Random(new Random().nextLong());
    // 快速排序切换插入排序数组长度
    private static final int QUICK_CUTOFF = 15;
    
    /**
     * 堆排序
     * @param a
     */
    public static <T extends Comparable<? super T>> void  heap(T[] a) {
        final int len = a.length;
        // 构造一个完整的大顶堆
        for (int i = (len/2)-1; i >= 0; i--)
            sink(a, i, len);
        int i = len;
        // 从大顶堆中选择最大的元素下标0交换到数组最后，使得数组有序
        while (i-- > 0) {
            exch(a, 0, i);
            // 交换的小元素0下沉保持堆有序
            sink(a, 0, i);
        }
    }
    
    /**
     * 对大顶堆指定位置k在[0..length)范围内下沉调整k到合适的位置。堆数组从0开始
     * @param a
     * @param k
     * @param length
     */
    private static <T extends Comparable<? super T>> void  sink(T[] a, int k, int length) {
        int i = k;
        // 父节点i(k)的左子节点
        while ((i = (2*i + 1)) < length) {
            // 如果存在i+1子节点，则获取较大的子节点下标
            if (i+1 < length && less(a[i], a[i+1]))
                i++;
            // 如果父节点k >= 子节点中较大的一个i，满足大顶堆的定义，退出
            if (!less(a[k], a[i]))
                break;
            // 父节点 k < 大的子节点i 则交换两者 
            exch(a, i, k);
            // 将父节点变为子节点的位置，继续向下调整
            k = i;
        }
    }
    
    /**
     * 随机打乱整个数组
     * @param a
     */
    public static <T extends Comparable<? super T>> void shuffle(T[] a) {
        shuffle(a, 0, a.length);
    }
    
    /**
     * 随机打乱数组a在[start..end)范围的元素
     * @param a
     * @param start
     * @param end
     */
    public static <T extends Comparable<? super T>> void shuffle(T[] a, int start, int end) {
        for (int i = start; i < end; i++) {
            int r = i + RANDOM.nextInt(end-i);
            exch(a, i, r);
        }
    }
    
    public static <T extends Comparable<? super T>> void quick(T[] a) {
        shuffle(a);
        quickSort(a, 0, a.length - 1);
    }
    
    private static <T extends Comparable<? super T>> void quickSort(T[] a, int lo, int hi) {
        // 在子数组length = CUTOFF时使用插入排序
        if (lo + QUICK_CUTOFF > hi) {
            insertion(a, lo, hi + 1);
            return;
        }
        int j = partition(a, lo, hi);
        quickSort(a, lo, j - 1);
        quickSort(a, j + 1, hi);
    }
    
    private static <T extends Comparable<? super T>> int partition(T[] a, int lo, int hi) {
        int i = lo, j = hi + 1;
        // 切分元素
        T v = a[i];
        while (true) {
            // 左 --> 右扫描，直到找到一个 e > v
            while (less(a[++i], v))
                if (i >= hi)
                    break;
            // 右 --> 左扫描，直到找到一个 e < v，找到自动退出循环
            while (less(v, a[--j]))
                ;
            // 指针相遇，切分完成，必定有 左 <= v <= 右
            if (i >= j)
                break;
            // 交换，使得 左大 右小 --> 左小 右大
            exch(a, i, j);
        }
        exch(a, lo, j);
        return j;
    }
    
    /**
     * 归并排序
     * @param a
     */
    @SuppressWarnings("unchecked")
    public static <T extends Comparable<? super T>> void merger(T[] a) {
        final int len = a.length;
        // 辅助排序数组
        T[] aux = (T[]) new Comparable[len];
        for (int sz = 1; sz < len; sz += sz) {
            // 最后一个小数组的开始下标
            int n = len - sz;
            for (int lo = 0; lo < n; lo += sz*2) {
                // 归并 小数组，如果最后小数组的长度过小，则end = len
                int end = Math.min(lo + sz*2, len);
                // 对于归并排序非递归版本，小数组使用插入排序并不能减少时间
//                if (sz < 0) insertion(a, lo, end);
                merg(a, aux, lo, lo + sz - 1, end);
            }
        }
        
    }
    
    /**
     * 希尔排序。改进的插入排序
     * @param a
     */
    public static <T extends Comparable<? super T>> void shell(T[] a) {
        int len = a.length, h = 1;
        // 计算间隔
        while (h < len / 3) h = 3 * h + 1;
        while (h > 0) {
            // 数组间隔h 对应元素有序
            for (int i = h; i < len; i++) {
                T val = a[i];
                int j = i;
                // 移位操作
                while (j >= h && less(val, a[j-h]))
                    a[j] = a[j -= h];
                a[j] = val;
            }
            h /= 3;
        }
    }
    
    /**
     * 插入排序
     * @param a
     */
    public static <T extends Comparable<? super T>> void insertion(T[] a) {
        insertion(a, 0, a.length);
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
        return isSorted(a, 0, a.length);
    }
    
    public static <T extends Comparable<? super T>> boolean isSorted(T[] a, int start, int end) {
        int min = minIndex(a, start, end);
        if (min != start)
            return false;
        for (int i = start + 1; i < end; i++)
            if (less(a[i], a[i-1]))
                return false;
        return true;
    }
    
    /**
     * 对数组a在指定范围start..end内使用插入排序
     * @param a
     * @param start
     * @param end
     */
    private static <T extends Comparable<? super T>> void insertion(T[] a, int start, int end) {
        if (start >= a.length)
            return;
        // 找出最小的元素下标
        int min = minIndex(a, start, end);
        // 交换 使得最小元素下标为0
        exch(a, min, start);
        for (int i = start + 1; i < end; i++) {
            T val = a[i];
            int j = i;
            // 将大的元素向后移位，代替交换位置exch() 最小元素下标为0，不需要边界条件 j > 0
            while (less(val, a[j-1]))
                // 注意：先执行j--，再执行后面的j ===> a[j--] = a[j-1]会导致先j--再j-1越界
                a[j--] = a[j];
            a[j] = val;
        }
    }
    
    /**
     * 归并两个有序小数组lo..mid, mid+1..end为一个有序数组lo..end，不包括end
     */
    private static <T extends Comparable<? super T>> void merg(T[] a, T[] aux, int start, int mid, int end) {
        // 已经有序
        if (less(a[mid], a[mid+1]))
            return;
        int i = start, j = mid + 1;
        // 复制两个有序的小数组
//        for (int k = start; k < end; k++)
//            aux[k] = a[k];
        System.arraycopy(a, start, aux, start, end-start);
        // 合并两个有序小数组为一个有序小数组
        for (int k = start; k < end; k++)
            //左边数组用尽，取右边元素
            if (i > mid)
                a[k] = aux[j++];
            //反之
            else if (j >= end)
                a[k] = aux[i++];
            else if (less(aux[j], aux[i]))
                a[k] = aux[j++];
            else
                a[k] = aux[i++];
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
     * 获取数组a的最小的下标。
     * @param a
     * @return
     */
//    private static <T extends Comparable<? super T>> int minIndex(T[] a) {
//        return minIndex(a, 0, a.length);
//    }
    
    /**
     * 从数组范围[start..end)查找最小的下标。不包括end
     * @param a
     * @param start
     * @param end
     * @return
     */
    private static <T extends Comparable<? super T>> int minIndex(T[] a, int start, int end) {
        int minIndex = start;
        for (int i = start; i < end; i++)
            if (less(a[i], a[minIndex]))
                minIndex = i;
        return minIndex;
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
