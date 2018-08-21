package cn.navyd.lib.algs.sort;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

/**
 * <p>快速三向切分排序：</p>
 * <p>与快排的区别：切分不在只区分大小，还区分相等的元素，将左边遇到的相等元素放在左边
 * 右边的放到右边，然后合并相等的放中间，再找切分点循环
 * </p>
 * <p>该类是一个优化版的快速排序，针对小数组使用插入排序，中等数组加入三取样切分，大数组使用九取样切分
 * 快排算法将数组分为4个区，左右两边为等于切分点v的区，靠左为小于v的，靠右为大于v的区<p>
 * <p>该算法同时适用大量重复元素与无重复元素的情况，均提供稍好的性能，对于相应的quick sort版本</p>
 * 稳定性：否
 *
 * @author Navy D
 * @date 20170814151656
 */
public class QuickX {
	// 切换插入排序阀值
	private static final int INSERTION_SORT_CUTOFF = 8;
	// 中等数组使用三取样切分，超过这个大小的使用ninther as partitioning element
	private static final int MEDIAN_OF_3_CUTOFF = 40;

	private QuickX() {

	}

	/**
	 * 对数组a进行升序排序
	 * @param a
	 * @author Navy D
	 * @date 20171001163527
	 */
	public static <T extends Comparable<? super T>> void sort (T[] a) {
		if (a == null)
			throw new IllegalArgumentException("argument array is null");
		shuffle(a);
		quickSort3way(a, 0, a.length - 1);

		assert isSorted(a);
	}

	/**
	 * 对数组a使用指定的顺序comparator对象排序
	 * @param a
	 * @param comparator
	 * @author Navy D
	 * @date 20171001130850
	 */
	public static <T extends Comparable<? super T>> void sort(T[] a, Comparator<T> comparator) {
		if (a == null)
			throw new IllegalArgumentException("argument array is null");
		if (comparator == null)
			throw new IllegalArgumentException("argument comparator is null");
		shuffle(a);
		quickSort3way(a, 0, a.length - 1, comparator);

		assert isSorted(a, comparator);
	}


	/**
	 * 快速三向切分：与标准的快速排序相似，只不过在遇到与切分点相同的元素时分开处理
	 * 左边扫描到的交换后放入最左边相等数组a[lo..p],右边扫描的交换后放入最右边数组a[q..hi]
	 * 最后两边可能存在一个与切分点相等的元素数组，然后将相等数组插入中间即可
	 * @param a
	 * @param lo
	 * @param hi
	 * @author Navy D
	 * @date 20170807232243
	 */
	private static <T extends Comparable<? super T>> void quickSort3way(T[] a, int lo, int hi) {
		int n = hi - lo + 1;
		if (n <= INSERTION_SORT_CUTOFF) {
			insertion(a, lo, hi);
			return;
		} else if (n <= MEDIAN_OF_3_CUTOFF) {
			int m = mediumOf3(a, lo, lo + n / 2, hi);
			exch(a, m, lo);
		}
		// use Tukey ninther as partitioning element
		else {
			int eps = n / 8;
			int mid = lo + n / 2;
			int m1 = mediumOf3(a, lo, lo + eps, lo + eps + eps);
			int m2 = mediumOf3(a, mid - eps, mid, mid + eps);
			int m3 = mediumOf3(a, hi - eps - eps, hi - eps, hi);
			int ninther = mediumOf3(a, m1, m2, m3);
			exch(a, ninther, lo);
		}
		// p..i为小于v，j..q为大于v
		int i = lo, j = hi + 1;
		// lo..p与q..hi为等于v
		int p = lo, q = hi + 1;
		// 切分点
		T v = a[lo];

		while (true) {
			//左边扫描时遇到大的终止
			while (less(a[++i], v))
				if (i == hi)
					break;
			//右边扫描遇到小的终止
			while (less(v, a[--j]))
				;
			// 如果最后中间的值与切分点相等就将这个值放到左边p
			if (i == j && equal(a[i], v))
				exch(a, ++p, i);
			if (i >= j)
				break;
			//交换左大右小的元素
			exch(a, i, j);
			//左边i与切点相等就放入p
			if (equal(a[i], v))
				exch(a, ++p, i);
			//右边j切点相等就放入q
			if (equal(a[j], v))
				exch(a, --q, j);
//			System.out.println("p = " + p + " q = " + q);
		}

		//确保i下标为j后面一个，为下面交换数组准备
		i = j + 1;
		//交换左边等于数组和小于数组，j为左边小于数组最后下标
		for (int k = lo; k <= p; k++)
			//其中有k==j自己交换的情况
			exch(a, k, j--);
		//交换右边两个数组
		for (int k = hi; k >= q; k--)
			exch(a, k, i++);
		//交换后的j已经是左边小于数组的最后下标
		quickSort3way(a, lo, j);
		//i是右边大于数组的开始下标
		quickSort3way(a, i, hi);

		assert isSorted(a, lo, hi);
	}

	// 使用comparator提供的顺序排序
	private static <T extends Comparable<? super T>> void quickSort3way(T[] a, int lo, int hi, Comparator<T> comparator) {
		int n = hi - lo + 1;
		if (n <= INSERTION_SORT_CUTOFF) {
			insertion(a, lo, hi, comparator);
			return;
		} else if (n <= MEDIAN_OF_3_CUTOFF) {
			int m = mediumOf3(a, lo, lo + n / 2, hi);
			exch(a, m, lo);
		}
		// use Tukey ninther as partitioning element
		else {
			int eps = n / 8;
			int mid = lo + n / 2;
			int m1 = mediumOf3(a, lo, lo + eps, lo + eps + eps);
			int m2 = mediumOf3(a, mid - eps, mid, mid + eps);
			int m3 = mediumOf3(a, hi - eps - eps, hi - eps, hi);
			int ninther = mediumOf3(a, m1, m2, m3);
			exch(a, ninther, lo);
		}
		// p..i为小于v，j..q为大于v
		int i = lo, j = hi + 1;
		// lo..p与q..hi为等于v
		int p = lo, q = hi + 1;
		// 切分点
		T v = a[lo];

		while (true) {
			//左边扫描时遇到大的终止
			while (less(a[++i], v, comparator))
				if (i == hi)
					break;
			//右边扫描遇到小的终止
			while (less(v, a[--j], comparator))
				;
			// 如果最后中间的值与切分点相等就将这个值放到左边p
			if (i == j && equal(a[i], v, comparator))
				exch(a, ++p, i);
			if (i >= j)
				break;
			//交换左大右小的元素
			exch(a, i, j);
			//左边i与切点相等就放入p
			if (equal(a[i], v, comparator))
				exch(a, ++p, i);
			//右边j切点相等就放入q
			if (equal(a[j], v, comparator))
				exch(a, --q, j);
//			System.out.println("p = " + p + " q = " + q);
		}

		//确保i下标为j后面一个，为下面交换数组准备
		i = j + 1;
		//交换左边等于数组和小于数组，j为左边小于数组最后下标
		for (int k = lo; k <= p; k++)
			//其中有k==j自己交换的情况
			exch(a, k, j--);
		//交换右边两个数组
		for (int k = hi; k >= q; k--)
			exch(a, k, i++);
		//交换后的j已经是左边小于数组的最后下标
		quickSort3way(a, lo, j, comparator);
		//i是右边大于数组的开始下标
		quickSort3way(a, i, hi, comparator);

		assert isSorted(a, lo, hi, comparator);
	}


	/**
	 * 如果v和w相等，就返回true
	 * @param v
	 * @param w
	 * @return
	 * @author Navy D
	 * @date 20170807235235
	 */
	private static <T extends Comparable<? super T>> boolean equal(T v, T w) {
		return v.compareTo(w) == 0;
	}

	private static <T extends Comparable<? super T>> boolean equal(T v, T w, Comparator<T> comparator) {
		return comparator.compare(v, w) == 0;
	}

	/**
	 * 随机重组数组
	 * @param a
	 * @author Navy D
	 * @date 20170807215404
	 */
	private static <T> void shuffle(T[] a) {
		int n = a.length;
		Random rand = new Random(new Random().nextLong());
		for (int i = 0; i < n; i++) {
			int r = i + rand.nextInt(n-i);
			T tem = a[i];
			a[i] = a[r];
			a[r] = tem;
		}
	}
	/**
	 * 插入排序
	 * @param a
	 * @param lo
	 * @param hi
	 * @author Navy D
	 * @date 20170807215538
	 */
	private static <T extends Comparable<? super T>> void insertion(T[] a, int lo, int hi) {
		for (int i = lo + 1; i <= hi; i++) {
			T v = a[i];
			int j = i;
			for (; j != 0 && less(v, a[j-1]); j--)
				a[j] = a[j-1];
			a[j] = v;
		}
	}

	private static <T extends Comparable<? super T>> void insertion(T[] a, int lo, int hi, Comparator<T> comparator) {
		for (int i = lo + 1; i <= hi; i++) {
			T v = a[i];
			int j = i;
			for (; j != 0 && less(v, a[j-1], comparator); j--)
				a[j] = a[j-1];
			a[j] = v;
		}
	}

	/**
	 * 返回数组a中下标i,j,k元素值为中间的一个下标
	 * @param a
	 * @param i
	 * @param j
	 * @param k
	 * @return
	 * @author Navy D
	 * @date 20170807222134
	 */
	private static <T extends Comparable<? super T>> int mediumOf3(T[] a, int i, int j, int k) {
		return (less(a[i], a[j]) ? (less(a[j], a[k]) ? j : less(a[i], a[k]) ? k : i)
				: (less(a[k], a[j]) ? j : less(a[k], a[i]) ? k : i));
	}

	/**
	 * 交换数组两个元素位置
	 * @param a
	 * @param i
	 * @param j
	 * @author Navy D
	 * @date 20170807215642
	 */
	private static <T> void exch(T[] a, int i, int j) {
		T temp = a[i];
		a[i] = a[j];
		a[j] = temp;
	}

	/**
	 * 如果v比w小就返回true
	 * @param v
	 * @param w
	 * @return
	 * @author Navy D
	 * @date 20170807221957
	 */
	private static <T extends Comparable<? super T>> boolean less(T v, T w) {
		return v.compareTo(w) < 0;
	}

	private static <T extends Comparable<? super T>> boolean less(T v, T w, Comparator<T> comparator) {
		return comparator.compare(v, w) < 0;
	}


	/**************************		 验证		*****************************************/



	private static <T extends Comparable<? super T>> boolean isSorted(T[] a) {
		return isSorted(a, 0, a.length - 1);
	}

	private static <T extends Comparable<? super T>> boolean isSorted(T[] a, int lo, int hi) {
		for (int i = lo + 1; i <= hi; i++)
			if (less(a[i], a[i-1]))
				return false;
		return true;
	}

	private static <T extends Comparable<? super T>> boolean isSorted(T[] a, Comparator<T> comparator) {
		return isSorted(a, 0, a.length - 1, comparator);
	}

	private static <T extends Comparable<? super T>> boolean isSorted(T[] a, int lo, int hi, Comparator<T> comparator) {
		for (int i = lo + 1; i <= hi; i++)
			if (less(a[i], a[i-1], comparator))
				return false;
		return true;
	}

	/**
	 * 三切分的快速排序：将数组分为小于切分点，等于切分点，大于切分点三个部分
	 * 注意该排序对于数组重复元素不多的情况交换过多，不可行
	 * @param a
	 * @param lo
	 * @param hi
	 * @author Navy D
	 * @date 20170807222653
	 */
//	public static <T extends Comparable<? super T>> void quickSort3way(T[] a, int lo, int hi) {
//		if (lo >= hi)
//			return;
//		//0..lt-1为小于v，gt+1..hi为大于v，对于i..gt中的元素，小的放入lt,大的放入gt,等于的i++;
//		int lt = lo, i = lo + 1, gt = hi;
//		//切分点
//		T v = a[lo];
//		//a[i..gt]未确定的元素没有才退出
//		while (i <= gt) {
//			//将a[i]于切分点比较
//			int cmp = a[i].compareTo(v);
//			//小的放入lt中，lt始终在i后面，即已经比较过
//			if (cmp < 0)
//				exch(a, lt++, i++);
//			//大的放入gt中，注意交换过来的gt是未知的，需要再次比较，不能i++
//			else if (cmp > 0)
//				exch(a, gt--, i);
//			//等于只增加a[i..gt]的大小
//			else
//				i++;
//		}
//		quickSort3way(a, lo, lt - 1);
//		quickSort3way(a, gt + 1, hi);
//	}

	public static void main(String[] args) {
		String[] s = "E a S Y Q U E y S T I O N".split(" ");
		sort(s, String.CASE_INSENSITIVE_ORDER);
		System.out.println(Arrays.toString(s));



	}



}
