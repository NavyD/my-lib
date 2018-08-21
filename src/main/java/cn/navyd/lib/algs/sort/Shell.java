package cn.navyd.lib.algs.sort;

import java.util.Arrays;
import java.util.Random;

/**
 * 希尔排序：</br>
 * 基于插入排序的算法，对于大规模的数组，使数组任意间隔为h的元素有序
 * 即将数组中间隔h的元素看做为一个数组来排序</br>
 *
 * 时间复杂度：无法准确描述</br>
 * <p>
 * 使用递增序列1/2(3^k - 1)， 1,4,13,40...的比较次数 < N的若干倍*递增序列的长度
 * </p>
 * 稳定性：否
 *
 * @author Navy D
 * @date 20170731194607
 */
public class Shell {

	private Shell() {

	}

	/**
	 * 将数组a进行升序排序
	 * @param a
	 * @author Navy D
	 * @date 20170731194636
	 */
	public static <T extends Comparable<? super T>> void sort(T[] a) {
		int n = a.length;
		//h是shell排序的增量，将间隔h的元素作为一组使用插入排序
		int h = 1;
		// 1,4,13,40
		while (h < n/3)
			h = h*3 + 1;
		/**
		 * shell将数据间隔d作为一组插入排序，间隔h的元素都是有序的，
		 * 不断的缩小d直到d=1即完成排序
		 */
		while (h >= 1) {
			//不需要交换的插入排序，将大的元素直接后移一位
			for (int i = h; i < n; i++) {
				T v = a[i];
				int j = i;
				while (j >= h && less(v, a[j-h])) {
					a[j] = a[j-h];
					j -= h;
				}
				a[j] = v;
			}
			assert isHsorted(a, h);
			h /= 3;
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
		return v.compareTo((T) w) < 0;
	}

	/**
	 * 交换数组i与j的值
	 * @param a
	 * @param i
	 * @param j
	 * @author Navy D
	 * @date 20170731194308
	 */
//	private static <T> void exch(T[] a, int i, int j) {
//		T temp = a[i];
//		a[i] = a[j];
//		a[j] = temp;
//	}

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

	private static <T extends Comparable<? super T>> boolean isHsorted(T[] a, int h) {
		for (int i = h; i < a.length; i++) {
			if (less(a[i], a[i - h]))
				return false;
		}
		return true;
	}

	public static void main(String[] args) {
		Random r = new Random(12);
		int n = 100;
		Integer[] a = new Integer[n];
		for (int i = 0; i < n; i++) {
			a[i] = r.nextInt(n*2);
		}
		System.out.println(Arrays.toString(a));
		sort(a);
		System.out.println(Arrays.toString(a));
//		test();
	}

}
