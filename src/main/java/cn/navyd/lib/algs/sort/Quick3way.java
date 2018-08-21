package cn.navyd.lib.algs.sort;

import java.util.Arrays;
import java.util.Random;

/**
 * <p>三向切分的快速排序：</p>
 * <p>相比于标准的快排，增加了对重复元素的处理，即将重复元素排在一起后跳过不对这部分排序</p>
 * <p>适用于存在大量重复元素的数组
 * <p>复杂度：对于包含大量重复元素的数组，排序时间为线性级别
 * 时间复杂度：平均~(2ln2)NH，其中H最坏为所有主键不重复H=lgN即比标准快排慢;
 * 最坏情况下：当所有元素都不重复时，算法会对每个元素做一个次交换，交换次数多于快排
 * </p>
 * @author Navy D
 * @date 20171001154509
 */
public class Quick3way {
	//切换插入排序
	private static final int CUTOFF = 15;

	/**
	 * 对数组a进行升序排序
	 * @param a
	 * @author Navy D
	 * @date 20171001154449
	 */
	public static <T extends Comparable<? super T>> void sort(T[] a) {
		if (a == null)
			throw new IllegalArgumentException("argument array is null");
		shuffle(a);
		quickSort3way(a, 0, a.length - 1);

		assert isSorted(a);
	}

	// 三向切分
	private static <T extends Comparable<? super T>> void quickSort3way(T[] a, int lo, int hi) {
		if (hi <= lo + CUTOFF) {
			insertion(a, lo, hi);
			return;
		}
		int lt = lo;
		int gt = hi;
		int i = lt + 1;
		T v = a[lo];
		// 将小于切分点v的元素放到左边lt，等于v的元素放中间，大于v的元素放右边
		while (i <= gt) {
			int cmp = a[i].compareTo(v);
			if (cmp < 0)
				exch(a, i++, lt++);
			else if (cmp > 0)
				exch(a, i, gt--);
			else
				i++;
		}
		quickSort3way(a, lo, lt - 1);
		quickSort3way(a, gt + 1, hi);

		assert isSorted(a, lo, hi);
	}

	// 交换数组两个值
	private static <T> void exch(T[] a, int i, int j) {
		T t = a[i];
		a[i] = a[j];
		a[j] = t;
	}

	// 插入排序
	private static <T extends Comparable<? super T>> void insertion(T[] a, int lo, int hi) {
		for (int i = lo + 1; i <= hi; i++) {
			T v = a[i];
			int j = i;
			for (; j != 0 && less(v, a[j-1]); j--)
				a[j] = a[j-1];
			a[j] = v;
		}
	}

	private static <T extends Comparable<? super T>> boolean less(T v, T w) {
		return v.compareTo(w) < 0;
	}

	private static <T> void shuffle(T[] a) {
		int n = a.length;
		Random rand = new Random(new Random().nextLong());
		for (int i = 0; i < a.length; i++) {
			int r = rand.nextInt(n-i) + i;
			T temp = a[r];
			a[r] = a[i];
			a[i] = temp;
		}
	}



	/******************		验证	*******************************/

	private static <T extends Comparable<? super T>> boolean isSorted(T[] a) {
		return isSorted(a, 0, a.length - 1);
	}

	private static <T extends Comparable<? super T>> boolean isSorted(T[] a, int lo, int hi) {
		for (int i = lo + 1; i <= hi; i++)
			if (less(a[i], a[i - 1]))
				return false;
		return true;
	}

	public static void main(String[] args) {
		String[] s = "E a S Y Q U E y S T I O N".split(" ");
		// sort(s);
		sort(s);
		System.out.println(Arrays.toString(s));

	}


}
