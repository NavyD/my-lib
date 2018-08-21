package cn.navyd.lib.algs.sort;

import java.util.Arrays;
import java.util.Random;

/**
 * 选择排序：
 * 先找到数组中最小的元素，将其与数组一个元素交换，
 * 再在剩下的数组元素中找最小，交换到第二个如此循环
 *
 * 时间复杂度：N^2/2次比较和N次交换
 *
 * 稳定性：否
 *
 * @author Navy D
 * @date 20170814171927
 */
public class Selection {

	/**
	 * 对一个数组进行升序排序
	 * @param a
	 * @author Navy D
	 * @date 20170929092500
	 */
	public static <T extends Comparable<? super T>> void sort(T[] a) {
		int len = a.length;
		for (int i = 0; i < len; i++) {
			int min = i;
			for (int j = i+1; j < len; j++) {
				if (less(a[j], a[min]))
					min = j;
			}
			exch(a, i, min);
		}

		assert isSorted(a);
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

	/**
	 * 交换数组i与j的值
	 * @param a
	 * @param i
	 * @param j
	 * @author Navy D
	 * @date 20170731194308
	 */
	private static <T extends Comparable<? super T>> void exch(T[] a, int i, int j) {
		T temp = a[i];
		a[i] = a[j];
		a[j] = temp;
	}

	/**
	 * 如果数组元素存在降序的情况，返回false。否则返回true
	 * @param a
	 * @return
	 * @author Navy D
	 * @date 20170731194358
	 */
	private static <T extends Comparable<? super T>> boolean isSorted(T[] a) {
		for (int i = 1; i < a.length; i++)
			if (less(a[i], a[i-1]))
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
		sort(a);
		System.out.println(Arrays.toString(a));

	}


}
