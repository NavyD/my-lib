package cn.navyd.lib.algs.sort;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

/**
 * <p>归并排序的优化版本：自顶向下的归并方式</p>
 * 优化项：
 * <li>对小数组使用插入排序</li>
 * <li>对有序数组不进行归并merge</li>
 * <li>不在merge归并中将数组复制到辅助数组</li>
 * <li>使用java的方法提高性能如objec.clone,arrayCopy</li>
 *
 */
public class MergeX {
	// cutoff to insertion sort
	private static final int CUTOFF = 7;

	private MergeX() {
	}

	/**
	 * 对数组a进行升序排序
	 * @param a
	 * @author Navy D
	 * @date 20171002122552
	 */
	public static <T extends Comparable<? super T>> void sort(T[] a) {
		// 复制一个原始数组而不是在merge中赋值，提高性能
		T[] aux = a.clone();
		// 将辅助 原数组调换
		sort(aux, a, 0, a.length - 1);

		assert isSorted(a);
	}

	/**
	 * 递归sort和复制数组都是将数据排序到辅助数组，merge将数组到原数组
	 *
	 * 将原本在merge中的复制数组的方式变为提前复制完整数组，并且要保证在merge中使用辅助数组比较时有序，
	 * 在sort中对a有序的两个数组直接赋值对应的范围。
	 * @author Navy D
	 * @date 20171002121315
	 */
	private static <T extends Comparable<? super T>>  void sort(T[] src, T[] dst, int lo, int hi) {
		if (hi <= lo + CUTOFF) {
			insertionSort(dst, lo, hi);
			return;
		}
		int mid = lo + (hi - lo) / 2;
		// 	 a,   aux
		// 将数据从原数组排序到辅助数组
		sort(dst, src, lo, mid);
		sort(dst, src, mid + 1, hi);

		// if (!less(src[mid+1], src[mid])) {
		// for (int i = lo; i <= hi; i++) dst[i] = src[i];
		// return;
		// }

		// 如果两个源子数组是有序的，就将这段元素复制到辅助数组aux中，以避免merge再下次可能使用aux比较时使顺序正确
		// src是a数组，dst是aux数组
		if (less(src[mid], src[mid + 1])) {
			System.arraycopy(src, lo, dst, lo, hi - lo + 1);
			return;
		}
		// aux, a, lo, mid, hi
		// 将数据从辅助数组排序到原数组a
		merge(src, dst, lo, mid, hi);
	}

	/**
	 * src为aux，dst为a
	 * 相比标准的merge少了复制数组的循环，虽然看上去src dst复杂了，但是merge是将src的元素归并到dst数组中
	 * 使用src中元素比较	 由于src数组已经是dst的数组的clone，不再需要赋值
	 * @author Navy D
	 * @date 20171002115708
	 */
	private static <T extends Comparable<? super T>>  void merge(T[] src, T[] dst, int lo, int mid, int hi) {

		assert isSorted(src, lo, mid);
		assert isSorted(src, mid + 1, hi);

		int i = lo, j = mid + 1;
		// 将输入数组排序到辅助数组
		for (int k = lo; k <= hi; k++) {
			if (i > mid)
				dst[k] = src[j++];
			else if (j > hi)
				dst[k] = src[i++];
			else if (less(src[j], src[i]))
				dst[k] = src[j++]; // to ensure stability
			else
				dst[k] = src[i++];
		}

		assert isSorted(dst, lo, hi);
	}

	// 优化的插入排序
	private static  <T extends Comparable<? super T>> void insertionSort(T[] a, int lo, int hi) {
		for (int i = lo; i <= hi; i++) {
			T v = a[i];
			int j = i;
			for (; j != lo && less(v, a[j - 1]); j--)
				a[j] = a[j - 1];
			a[j] = v;
		}
	}

	/*******************************************************************
	 * Utility methods.
	 *******************************************************************/

	private static <T extends Comparable<? super T>>  boolean less(T a, T b) {
		return a.compareTo(b) < 0;
	}

	private static <T>  boolean less(T a, T b, Comparator<T> comparator) {
		return comparator.compare(a, b) < 0;
	}

	/*******************************************************************
	 * Version that takes Comparator<T> as argument.
	 *******************************************************************/

	/**
	 * 对数组a使用指定的顺序comparator排序
	 * @param a
	 * @param comparator
	 * @author Navy D
	 * @date 20171002124129
	 */
	public static <T extends Comparable<? super T>>  void sort(T[] a, Comparator<T> comparator) {
		T[] aux = a.clone();
		sort(aux, a, 0, a.length - 1, comparator);
		assert isSorted(a, comparator);
	}

	private static <T extends Comparable<? super T>>  void merge(T[] src, T[] dst, int lo, int mid, int hi, Comparator<T> comparator) {

		assert isSorted(src, lo, mid, comparator);
		assert isSorted(src, mid + 1, hi, comparator);

		int i = lo, j = mid + 1;
		for (int k = lo; k <= hi; k++) {
			if (i > mid)
				dst[k] = src[j++];
			else if (j > hi)
				dst[k] = src[i++];
			else if (less(src[j], src[i], comparator))
				dst[k] = src[j++];
			else
				dst[k] = src[i++];
		}

		assert isSorted(dst, lo, hi, comparator);
	}

	private static  <T extends Comparable<? super T>> void sort(T[] src, T[] dst, int lo, int hi, Comparator<T> comparator) {
		if (hi <= lo + CUTOFF) {
			insertionSort(dst, lo, hi, comparator);
			return;
		}
		int mid = lo + (hi - lo) / 2;
		sort(dst, src, lo, mid, comparator);
		sort(dst, src, mid + 1, hi, comparator);

		if (less(src[mid], src[mid + 1], comparator)) {
			System.arraycopy(src, lo, dst, lo, hi - lo + 1);
			return;
		}

		merge(src, dst, lo, mid, hi, comparator);
	}

	// sort from a[lo] to a[hi] using insertion sort
	private static <T extends Comparable<? super T>>  void insertionSort(T[] a, int lo, int hi, Comparator<T> comparator) {
		for (int i = lo; i <= hi; i++) {
			T v = a[i];
			int j = i;
			for (; j != lo && less(v, a[j - 1], comparator); j--)
				a[j] = a[j - 1];
			a[j] = v;
		}
	}

	/***************************************************************************
	 * Check if array is sorted - useful for debugging.
	 ***************************************************************************/
	private static <T extends Comparable<? super T>>  boolean isSorted(T[] a) {
		return isSorted(a, 0, a.length - 1);
	}

	private static <T extends Comparable<? super T>>  boolean isSorted(T[] a, int lo, int hi) {
		for (int i = lo + 1; i <= hi; i++)
			if (less(a[i], a[i - 1]))
				return false;
		return true;
	}

	private static <T extends Comparable<? super T>>  boolean isSorted(T[] a, Comparator<T> comparator) {
		return isSorted(a, 0, a.length - 1, comparator);
	}

	private static <T extends Comparable<? super T>>  boolean isSorted(T[] a, int lo, int hi, Comparator<T> comparator) {
		for (int i = lo + 1; i <= hi; i++)
			if (less(a[i], a[i - 1], comparator))
				return false;
		return true;
	}

	public static void main(String[] args) {
		int n = 20;
		Random rand = new Random(47);
		Integer[] a = new Integer[n];
		for (int i = 0; i < a.length; i++) {
			a[i] = rand.nextInt(100);
		}
		System.out.println(Arrays.toString(a));
		sort(a);
		System.out.println(Arrays.toString(a));
	}
}


