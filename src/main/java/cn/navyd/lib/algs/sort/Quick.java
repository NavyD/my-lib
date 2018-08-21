package cn.navyd.lib.algs.sort;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

/**
 * 标准快速排序：<p>
 * 分治思想：将一个数组分为两个子数组，当两个子数组有序时整个数组有序，先处理数组再递归
 * <p>
 * 实现：
 * 使用切分点使数组在切分点两边总体左小右大，再使切分点回到数组位置
 * 然后再找一个切分点，使左小右大，如此继续
 * </p>
 * 优化：
 * <li>保持随机性：避免对于类增序数组排序这种最坏情况</li>
 * <li>切换到快速排序：小数组使用插入排序</li>
 * <li>切分点优化：使用数组起点，中点，终点的值中等的一个下标作为切分点</li>
 * <p>
 * 对于含大量重复元素的数组排序，请使用quick3way的排序版本
 * </p>
 * <p>
 * 时间复杂度：平均2NlnN ~ 1.39NlgN的比较和1/6的交换，
 * 最多需要N^2/2比较(),partition()复杂度最坏n次比较,sort()平均n/2次比较
 * 空间复杂度：lgN
 * </p>
 * 稳定性：否 </br>
 *
 * 提供一个使用切分点的快排查找算法select()
 * @author Navy D
 * @date 20170814141602
 */
public class Quick {
	private static Random rand = new Random(new Random().nextLong());
	//小数组大小为cutoff时切换到插入排序
	private static final int CUTOFF = 15;

	private Quick() {

	}

	/**
	 * 对数组a进行升序排序
	 * @param a
	 * @author Navy D
	 */
	public static <T extends Comparable<? super T>> void sort(T[] a) {
		if (a == null)
			throw new IllegalArgumentException("argument array is null");
		shuffle(a);
		sort(a, 0, a.length - 1);

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
		sort(a, 0, a.length - 1, comparator);

		assert isSorted(a, comparator);
	}


	/**
	 * 快速排序：将一个数组分为两个数组，子数组独立排序，当子数组有序时整个数组即有序
	 * 先使数组在切分点左小右大序，再递归调用小数组使有序
	 * @param a
	 * @param lo
	 * @param hi
	 * @author Navy D
	 * @date 20170807213656
	 */
	private static <T extends Comparable<? super T>> void sort(T[] a, int lo, int hi) {
		//子数组大小为cutoff时使用插入排序
		if (hi <= lo + CUTOFF) {
			insertion(a, lo, hi);
			return;
		}
		//三取样切分：使用数组起点中点终点三点值居中的一个，以替换使用lo作为切分点
		int m = mediumOf3(a, lo, lo + (hi - lo)/2, hi);
		exch(a, lo, m);

		//使用lo切分数组，返回新的切分点，数组左小右大
		int j = partition(a, lo, hi);

		//左边排序
		sort(a, lo, j-1);
		//右边排序
		sort(a, j+1, hi);

	}

	/**
	 * 使用指定的顺序排序
	 * @param a
	 * @param lo
	 * @param hi
	 * @param comparator
	 * @author Navy D
	 * @date 20171001130819
	 */
	private static <T extends Comparable<? super T>> void sort(T[] a, int lo, int hi, Comparator<T> comparator) {
		//子数组大小为cutoff时使用出入排序
		if (hi <= lo + CUTOFF) {
			insertion(a, lo, hi, comparator);
			return;
		}

		//三取样切分：使用数组起点中点终点三点值居中的一个，以替换使用lo作为切分点
		int m = mediumOf3(a, lo, lo + (hi - lo)/2, hi, comparator);
		exch(a, lo, m);

		//使用lo切分数组，返回新的切分点，数组左小右大
		int j = partition(a, lo, hi, comparator);

		//左边排序
		sort(a, lo, j-1, comparator);
		//右边排序
		sort(a, j+1, hi, comparator);

	}

	/**
	 * 切分数组，返回新切分点(j左边小，右边可等于大于)并使数组切分点左边小，右边大;
	 *
	 * 切分数组，返回新切分点并使数组切分点左边小，右边大（总体而言，左右可能无序）;
	 * partition使用lo作为切分点，i从左到右扫描，直到遇到比a[lo]大的，停止。
	 * j从右到左扫描，直到遇到比a[lo]小的，停止。交换ij值(大的到右边，小的到左边)，
	 * 然后继续左扫，右扫 直到ij相遇，交换lo与j，使j作为下一个切分点
	 * @param a
	 * @param lo
	 * @param hi
	 * @return
	 * @author Navy D
	 * @date 20170807215749
	 */
	private static <T extends Comparable<? super T>> int partition(T[] a, int lo, int hi) {
		//左边从lo开始，右边从hi开始
		int i = lo, j = hi + 1;
		//取lo作为切分点（比较）
		T v = a[lo];
		// 一次循环中左边找一个大的，右边找一个小的交换这两个。如此循环
		while (true) {
			//左边找大元素，需要考虑i越界
			while (less(a[++i], v))
				if (i == hi)
					break;
			//右边找小元素，最后a[lo]=v不需要判断
			while (less(v, a[--j]))
				;
			//扫描完全部元素
			if (i >= j)
				break;
			//交换，一定是左大右小的元素情况
			exch(a, i, j);
		}

		//数组扫描完成，交换左边最后一个元素j与lo使j成为新的切分点
		//当这个数组增序，最后j=lo，sort()时lo>=hi就起作用了
		exch(a, lo, j);
		return j;
	}

	// 切分数组
	private static <T extends Comparable<? super T>> int partition(T[] a, int lo, int hi, Comparator<T> comparator) {
		//左边从lo开始，右边从hi开始
		int i = lo, j = hi + 1;
		//取lo作为切分点（比较）
		T v = a[lo];
		while (true) {
			//左边小元素，需要考虑i越界
			while (less(comparator, a[++i], v))
				if (i == hi)
					break;
			//右边大元素，最后a[lo]=v不需要判断
			while (less(comparator, v, a[--j]))
				;
			//扫描完全部元素
			if (i >= j)
				break;
			//交换，一定是左大右小的元素情况
			exch(a, i, j);
		}

		//数组扫描完成，交换左边最后一个元素j与lo使j成为新的切分点
		exch(a, lo, j);
		return j;
	}

	/**
	 * 随机重组数组
	 * @param a
	 * @author Navy D
	 * @date 20170807215404
	 */
	private static <T> void shuffle(T[] a) {
		int n = a.length;
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
			for (; j != 0 && less(comparator, v, a[j-1]); j--)
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

	private static <T extends Comparable<? super T>> int mediumOf3(T[] a, int i, int j, int k, Comparator<T> comparator) {
		return (less(comparator, a[i], a[j]) ? (less(comparator, a[j], a[k]) ? j : less(comparator, a[i], a[k]) ? k : i)
				: (less(comparator, a[k], a[j]) ? j : less(comparator, a[k], a[i]) ? k : i));
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

	private static <T extends Comparable<? super T>> boolean less(Comparator<T> comparator, T v, T w) {
		return comparator.compare(v, w) < 0;
	}

	/**
	 * 快速选择算法：快速找到数组a有序时下标k的值
	 * 基于切分的选择算法可以不用完全排序数组，使切分点不断接近下标k，
	 * 当切分点等于k时完成(此时数组可能部分有序)
	 * 时间复杂度：最坏n^2/2，平均为线性级别(比快排好)
	 * @param a
	 * @param k
	 * @return
	 * @author Navy D
	 * @date 20170814150226
	 */
	public static <T extends Comparable<? super T>> T select(T[] a, int k) {
		if (a == null)
			throw new IllegalArgumentException("argument array is null");
		shuffle(a);
		int lo = 0, hi = a.length - 1;
		//当数组有一个以上的元素时循环
		while (lo < hi) {
			//使数组在lo两边部分有序，并返回新的第一下标
			int j = partition(a, lo, hi);
			//k在切分点左边
			if (k < j)
				hi = j-1;
			//右边
			else if (k > j)
				lo = j+1;
			//相等时找到
			else
				return a[k];
		}
		return a[k];
	}

	public static <T extends Comparable<? super T>> T selectRecursion(T[] a, int k, int lo, int hi) {
		if (a == null)
			throw new IllegalArgumentException("argument array is null");

		if (lo >= hi)
			return a[k];

		int j = partition(a, lo, hi);
		if (k < j)
			selectRecursion(a, k, lo, j-1);
		else if (k > j)
			selectRecursion(a, k, j+1, hi);
		else
			return a[k];
		return a[k];
	}

	/*************		验证方法										*******************/
	/*******************************************************************************/

	private static <T extends Comparable<? super T>> boolean isSorted(T[] a) {
		return isSorted(a, 0, a.length - 1);
	}

	private static <T extends Comparable<? super T>> boolean isSorted(T[] a, int lo, int hi) {
		for (int i = lo + 1; i <= hi; i++)
			if (less(a[i], a[i - 1]))
				return false;
		return true;
	}

	private static <T extends Comparable<? super T>> boolean isSorted(T[] a, Comparator<T> comparator) {
		return isSorted(a, 0, a.length - 1, comparator);
	}

	private static <T extends Comparable<? super T>> boolean isSorted(T[] a, int lo, int hi, Comparator<T> comparator) {
		for (int i = lo + 1; i <= hi; i++)
			if (less(comparator, a[i], a[i - 1]))
				return false;
		return true;
	}


	public static void main(String[] args) {

		String[] s = "E a S Y Q U E y S T I O N".split(" ");
		Integer[] a = {2, 2, 1};
//		sort(s);
		sort(s, String.CASE_INSENSITIVE_ORDER);
		System.out.println(Arrays.toString(s));
		System.out.println(selectRecursion(a, 2, 0, a.length-1));
	}

}
