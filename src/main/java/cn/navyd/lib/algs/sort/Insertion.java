package cn.navyd.lib.algs.sort;

import java.util.Arrays; 
import java.util.Comparator;
import java.util.Random;
/**
 * 插入排序：</br>
 * 实现：
 * 将原数组中的元素一个个插入已经排序的小数组中，移动小数组元素使有序，如此循环</br>
 * 将原数组中第1个位置作为大小为1的小数组，取第二个元素与这个小数组元素比较并插入</br>
 *
 * 优化：</br>
 * 移动大元素：插入时总是交换变为将数组大的元素向后移动一位</br>
 * 消除循环j>0条件：通过设置哨兵，开始将数组最小的元素移动到第一个元素，这样less(v, 0)时永远不可能为true</br>
 *
 * 时间复杂度：最坏N^2/2比较和N^2/2次交换</br>
 * 数组元素是降序时，内循环平均N/2次比较，外循环N次</br>
 * 稳定性：是</br>
 *
 * @author Navy D
 * @date 20170731193811
 */
public class Insertion {

	private Insertion() {

	}

	/**
	 * 将数组使用升序排序
	 * @param a
	 * @author Navy D
	 * @date 20170929095700
	 */
	public static <T extends Comparable<? super T>> void sort(T[] a) {
		int n = a.length;
		int exchanges = 0;
		//通过将第一个下标交换成为最小的元素，消除排序内循环中j>0的条件
		for (int i = n-1; i > 0; i--) {
			if (less(a[i], a[i-1])) {
				exch(a, i, i-1);
				exchanges++;
			}
		}
		//是否已是增序
		if (exchanges == 0)
			return ;

		/**
		 * 第一个数已经是最小，while最后将退出 由于左边必定是排好序的，一旦当前元素比前一个元素大，直接下一轮
		 * 如当前元素较小，可以使用exch()，但是比较费时间，直接找到最后比当前元素
		 * 大的下标，这之前将所有元素往后移动一位，找到的下标一定是与它的下一位 值相同，这时将改下标的值替换成当前元素；
		 *
		 * 如：0， 2， 3， 1 ->0， 2， 3， 3->0, 2, 2, 3->0, 1, 2, 3;
		 */
		for (int i = 1; i < n; i++) {
			T v = a[i];
			int j = i;
			// 将i与数组前的元素比较，小的就往前移动
			while (less(v, a[j-1])) {
				// 将大的数往后移动，小的前移
				a[j] = a[j-1];
				j--;
			}
			// 将最后这个放到j的位置
			a[j] = v;

			assert isSorted(a, 0, i);
		}

		assert isSorted(a);
	}

	/**
	 * 使用指定地点comparator对象对数组排序
	 * @param a
	 * @param comparator
	 * @author Navy D
	 * @date 20170929103406
	 */
	public static <T> void sort(T[] a, Comparator<T> comparator) {
		int n = a.length;
		int exchanges = 0;
		for (int i = 0; i < a.length; i++)
			if (less(a[i], a[0], comparator)) {
				exch(a, 0, i);
				exchanges++;
			}

		if (exchanges == 0)
			return;

		for (int i = 1; i < n; i++) {
			T v = a[i];
			int j = i;
			while (less(a[j], a[j-1], comparator)) {
				a[j] = a[j-1];
				j--;
			}
			a[j] = v;

			assert isSorted(a, 0, i, comparator);
		}

		assert isSorted(a, comparator);
	}

	/**
	 * 原始的插入排序
	 * @param a
	 * @author Navy D
	 * @date 20170929095617
	 */
	private static <T extends Comparable<? super T>> void sortOrigin(T[] a) {
		int n = a.length;
		//最简单的写法
		for (int i = 1; i < n; i++) {
			T v = a[i];
			int j = i;
			for (; j != 0 && less(v, a[j-1]); j--)
				a[j] = a[j-1];
			a[j] = v;
		}
	}

	/**
	 * 如果v较小，返回true，否则返回false
	 * @param v
	 * @param w
	 * @return
	 * @author Navy D
	 * @date 20170731194135
	 */
	private static <T extends Comparable<? super T>> boolean less(T v, T w) {
		return v.compareTo(w) < 0;
	}

	private static <T> boolean less(T v, T w, Comparator<T> comparator) {
		return comparator.compare(v, w) < 0;
	}

	/**
	 * 交换数组i与j的值
	 * @param a
	 * @param i
	 * @param j
	 * @author Navy D
	 * @date 20170731194308
	 */
	private static <T> void exch(T[] a, int i, int j) {
		T temp = a[i];
		a[i] = a[j];
		a[j] = temp;
	}

	// 数组a是否有序
	private static <T extends Comparable<? super T>> boolean isSorted(T[] a) {
		return isSorted(a, 0, a.length-1);
	}

	// 数组a在lo..hi范围中是否有序
	private static <T extends Comparable<? super T>> boolean isSorted(T[] a, int lo, int hi)  {
		for (int i = lo + 1; i <= hi; i++)
			// 如果后一个元素比前一个与元素小
			if (less(a[i], a[i-1]))
				return false;
		return true;
	}

	//数组a是否在指定的顺序有序
	private static <T> boolean isSorted(T[] a, Comparator<T> comparator) {
		return isSorted(a, 0, a.length-1, comparator);
	}

	private static <T> boolean isSorted(T[] a, int lo, int hi, Comparator<T> comparator)  {
		for (int i = lo + 1; i <= hi; i++)
			// 如果后一个元素比前一个与元素小
			if (less(a[i], a[i-1], comparator))
				return false;
		return true;
	}


	public static void main(String[] args) {
		Random r = new Random(new Random().nextLong());
		int n = 10;
		Integer[] a = new Integer[n];
		for (int i = 0; i < n; i++) {
			a[i] = r.nextInt(n);
		}
		System.out.println(Arrays.toString(a));
		sortOrigin(a);
		System.out.println(Arrays.toString(a));
//		Arrays.sort(a);
	}


}
