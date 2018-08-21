package cn.navyd.lib.algs.string;

import java.util.Arrays;
import java.util.Random;

import cn.navyd.lib.algs.util.In;

/**
 * <h1>三向字符串快速排序：改进的快排</h1>
 * 思想：<br>
 * 将快排针对于字符串中的相同字符排序改进，分为小于等于大于三块.对相同的字符下一轮排序会跳过。<br>
 * 复杂度：<br>
 * 平均比较字符2nlnn次，运行时间与n*字符串的长度 + 2nlnn成正比<br>
 * 与快排相比最多减少2lnn次比较<br>
 * 稳定性：不稳定<br>
 * 注意：
 * 该算法可以提供unicode的字符串排序
 * @author Navy D
 * @date 20170924173630
 */
public class Quick3String {
	// 切换为插入排序的数组大小值
	private static final int CUTOFF = 15;

	private Quick3String() {

	}

	/**
	 * 对字符串数组升序排序
	 * @param a
	 * @author Navy D
	 * @date 20170924173405
	 */
	public static void sort(String[] a) {
		if (a == null)
			throw new IllegalArgumentException("argument array is null");
		shuffle(a);
		sort(a, 0, a.length - 1, 0);

		assert isSorted(a);
	}

	/**
	 * 对数组字符串第d位快速3向排序
	 * @param a
	 * @param lo
	 * @param hi
	 * @param d
	 * @author Navy D
	 * @date 20170924173134
	 */
	private static void sort(String[] a, int lo, int hi, int d) {
		if (hi <= lo + CUTOFF) {
			insertion(a, lo, hi, d);
			return ;
		}
		int lt = lo;
		int gt = hi;
		// 取数组第一个字符串的d字符作为切分元素
		int v = charAt(a[lo], d);
		// 从数组第二个字符串开始
		int i = lo + 1;
		while (i <= gt) {
			// 取数组第i个字符串比较d位字符
			int t = charAt(a[i], d);
			// 如果后面的字符串比第一个字符串的d位字符较小，就移动到左边
			if (t < v)
				exch(a, lt++, i++);
			// 较大就移动到右边
			else if (t > v)
				exch(a, i, gt--);
			// 等于就不移动只改变下标指向下一个元素
			else
				i++;
		}
		// 先排序左边的数组
		sort(a, lo, lt-1, d);
		// 然后排序等于v的数组，忽略d位相等的字符
		if (v >= 0)
			sort(a, lt, gt, d+1);
		// 排序右边的数组
		sort(a, gt+1, hi, d);
	}

	/**
	 * 返回字符串第d位的字符值。如果d与数组长度相等就返回-1
	 * 注意d不能比数组长度大
	 * @param s
	 * @param d 从0开始
	 * @return
	 * @author Navy D
	 * @date 20170924145124
	 */
	private static int charAt(String s, int d) {
		// 验证d是否合理
		assert d >= 0 && d <= s.length();

		return d != s.length() ? s.charAt(d) : -1;
	}

	/**
	 * 插入排序。对第d位及以后的字符排序。认为d位以前的字符均相同
	 * @param a
	 * @param lo
	 * @param hi
	 * @param d
	 * @author Navy D
	 * @date 20170924145509
	 */
	private static void insertion(String[] a, int lo, int hi, int d) {
		for (int i = lo; i <= hi; i++) {
			String v = a[i];
			int j = i;
			for (; j > lo && less(v, a[j-1], d); j--)
				a[j] = a[j-1];
			a[j] = v;
		}
	}

	// 交换数组值
	private static void exch(String[] a, int v, int w) {
		String temp = a[v];
		a[v] = a[w];
		a[w] = temp;
	}

	/**
	 * 如果字符串v在d位以后的字符中小于字符串w就返回true
	 * @param v
	 * @param w
	 * @param d
	 * @return
	 * @author Navy D
	 * @date 20170924145752
	 */
	private static boolean less(String v, String w, int d) {
		int minLen = v.length() <= w.length() ? v.length() : w.length();
		// 每个字符比较
		for (int i = d; i < minLen; i++) {
			if (v.charAt(i) != w.charAt(i))
				return v.charAt(i) < w.charAt(i);
			// 相等就比较下一个字符
		}
		// 如果一个字符串包含另一个字符串(长度内相等)
		return v.length() < w.length();

	}

	/**
	 * 将数组元素随机打乱
	 * @param a
	 * @author Navy D
	 * @date 20170924173601
	 */
	private static void shuffle(String[] a) {
		int n = a.length;
		Random rand = new Random(new Random().nextLong());
		for (int i = 0; i < a.length; i++) {
			int r = rand.nextInt(n-i) + i;
			String temp = a[r];
			a[r] = a[i];
			a[i] = temp;
		}
	}

	/**
	 * 检查字符串数组相邻字符串是否有序
	 * @param a
	 * @return
	 * @author Navy D
	 * @date 20170924165058
	 */
	private static boolean isSorted(String[] a) {
		for (int i = 1; i < a.length; i++)
			if (a[i].compareTo(a[i - 1]) < 0)
				return false;
		return true;
	}

	 public static void main(String[] args) {
			In in = new In("../MyAlgs/algs4-data/shells.txt");
			String[] a = in.readAllStrings();
			System.out.println(Arrays.toString(a));

			sort(a);

			System.out.println(Arrays.toString(a));

			String s = "now is the time for all good people to come to the aid of";
			a = s.split(" ");
			sort(a);
			System.out.println(Arrays.toString(a));
		}

}
