package cn.navyd.lib.algs.sort;

import java.util.Arrays;
import java.util.Random;
/**
 * <p>归并排序自底向上MergeBottomUp：</p>
 * 分治思想：先归并小数组，再成对的归并得到的子数组，直到归并整个数组</br>
 * <p>
 * 实现：在内循环中以小数组大小做归并，在外循环中以小数组大小成对叠加循环</p>
 *
 * 时间复杂度：1/2NlgN~NlgN次比较，最多访问数组6NlgN次</br>
 * <p>
 * 与td的区别在于；排序时比较的次序不同，如第3次比较时td是归并前两个
 * 已经排序的数组，而bu是继续比较大小相同的两个数组</p>
 *<p>
 * 该排序适合比较使用链表组织的数据</p>
 *
 * 稳定性： 是
 * @author Navy D
 * @date 20170802201132
 */
public class MergeBU {
	private MergeBU() {

	}

	/**
	 * 对数组a进行归并排序
	 * @param a
	 * @author Navy D
	 * @date 20170802181613
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Comparable<? super T>> void sort(T[] a) {
		int n = a.length;
		T[] aux = (T[]) new Comparable[n];
		/*
		 * 两两归并，（奇数个小数组（1个元素）也会通过merge()排序）完成后，四四归并，八八归并。。。
		 */
		//将数组分为sz个小数组（22,44,88...）
		for (int sz = 1; sz < n; sz += sz) {
			//将2小数组sz(22,44,88作为一组)大小归并直至原数组最后
			//n-sz是最后一个数组的起点，sz+sz是将两个数组作为一个
			for (int lo = 0; lo < n-sz; lo += sz+sz) {
				//lo+sz-1是2个数组的mid，lo+sz+sz-1是2个数组长度，最后一次合并可能不等长，使用n-1
				merge(a, aux, lo, lo+sz-1, min(lo+sz+sz-1, n-1));
			}
		}

		assert isSorted(a);
	}
	/**
	 * 原地归并的抽象方法：避免了在每次归并中创建新数组的开销
	 *
	 * 通过一开始对整个数组复制为辅助数组aux，在归并中从aux中复制到a原数组中;
	 * 方法是：对两个有序数组(通过对一个数组a取中作为两个，即必须保证从中间开始两边为增序,
	 * 大小可以不一样)进行排序， 判断左<右,取左边元素，并取下一个左边元素与右边元素比较，
	 * 左>右，取右边元素并使下一个右边元素与左边比较； 考虑当某一边元素取完时，直接取另一边元素
	 *
	 * @param a 原数组
	 * @param aux 辅助数组
	 * @param lo原数组开始位置
	 * @param mid 原数组中点下标，分为两个有序数组
	 * @param hi 原数组终点位置
	 * @author Navy D
	 * @date 20170802173245
	 */
	private static <T extends Comparable<? super T>> void merge(T[] a, T[] aux, int lo, int mid, int hi) {
		assert isSorted(a, lo, mid);
		assert isSorted(a, mid+1, hi);

		//左边数组第一个，右边数组第一个
		int i = lo, j = mid+1;

		for (int k = 0; k < a.length; k++)
			aux[k] = a[k];
		//将aux数组看做两个排序
		for (int k = lo; k < a.length; k++) {
			//左边数组用尽，取右边元素
			if (i > mid)
				a[k] = aux[j++];
			//反之
			else if (j > hi)
				a[k] = aux[i++];
			//右边元素小，将右边元素取出，并下一个右边元素与左边比较
			else if (less(aux[j], aux[i]))
				a[k] = aux[j++];
			//反之
			else
				a[k] = aux[i++];
		}

		assert isSorted(a, lo, hi);
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

	private static int min(int a, int b) {
		return a < b ? a : b;
	}

	private static void test(int n, int trials) {
		Random rand = new Random();
		Integer[] a = new Integer[n];
		long timeTotals = 0;
		for (int i = 0; i < trials; i++) {

			for (int j = 0; j < n; j++)
				a[j] = rand.nextInt(n);
			long start = System.currentTimeMillis();
			sort(a);
			timeTotals += System.currentTimeMillis() - start;
		}
		double aa = timeTotals/1000.0;
		System.out.format("average time:%1$.4f s\n", aa/trials);
	}


	/**
	 * 如果数组元素存在降序的情况，返回false。否则返回true
	 * @param a
	 * @return
	 * @author Navy D
	 * @date 20170731194358
	 */
	private static <T extends Comparable<? super T>> boolean isSorted(T[] a) {
		return isSorted(a, 0, a.length);
	}

	private static <T extends Comparable<? super T>> boolean isSorted(T[] a, int lo, int hi) {
		for (int i = lo+1; i < hi; i++)
			if (less(a[i], a[i-1]))
				return false;
		return true;
	}


	public static void main(String[] args) {
//		String s = "MERGESORTEXAMPLE";
//		char[] ch = s.toCharArray();
//		Character[] a1 = new Character[s.length()];
//		for (int i = 0; i < s.length(); i++) {
//			a1[i] = ch[i];
//		}
//		System.out.println(Arrays.toString(a1));
//		sort(a1);
//		System.out.println(Arrays.toString(a1));
		int n = 100;
		Random rand = new Random();
		Integer[] a = new Integer[n];

		for (int i = 0; i < a.length; i++)
			a[i] = rand.nextInt(100);
		System.out.println(Arrays.toString(a));
		sort(a);
		System.out.println(Arrays.toString(a));


	}

}
