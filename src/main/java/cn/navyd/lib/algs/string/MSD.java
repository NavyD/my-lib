package cn.navyd.lib.algs.string;

import java.util.Arrays;

import cn.navyd.lib.algs.util.In;

/**
 * <h1>高位优先的字符串排序：采用从左到右的键索引计数法</h1>
 * 思想：<br>
 * 该算法针对于不定长的字符串排序。先使用键索引计数法将所有字符串首字符排序，再递归的将每个首字符对应的子字符串排序<br>
 * 复杂度：<br>
 * n为数组大小，R为基数。平均需要检查nlogRn个字符。<br>
 * w是字符串的平均长度。访问数组次数在8n+3R和7wn+3wR间。<br>
 * 最坏情况下空间与R*最长字符串的长度+n成正比，时间是线性的<br>
 * 最坏情况为所有字符串均相同<br>
 * 稳定性：稳定<br>
 * 注意：<br>
 * 对于字符串末尾使用一个r+2的计数数组标记已经结尾的字符串和计数法需要的一个位置。<br>
 * 对于小数组使用插入排序提高性能<br>
 * 该算法不适合于含大量字符相等的字符串数组
 *
 * @author Navy D
 * @date 20170924152245
 */
public class MSD {
	private static final int BITS_PER_BYTE =   8;
    private static final int BITS_PER_INT  =  32;

	// 字符编码基数如：ascii扩展是2^8，共256种编码，unicode=2^16共65536中编码
	private static int R = 1 << 8;
	// 切换为插入排序的数组大小值
	private static final int CUTOFF = 15;

	private MSD() {

	}

	/**
	 * 对扩展ascii码的字符串数组进行排序
	 * @param a
	 * @author Navy D
	 * @date 20170924145408
	 */
	public static void sort(String[] a) {
		if (a == null)
			throw new IllegalArgumentException("argument array is null");
		int n = a.length;
		String[] aux = new String[n];
		sort(a, 0, n - 1, 0, aux);

		assert isSorted(a);
	}

	private static void sort(String[] a, int lo, int hi, int d, String[] aux) {
		// 大小为cutoff的子数组切换到插入排序
		if (hi <= lo + CUTOFF) {
			insertion(a, lo, hi, d);
			return;
		}
		// 如果没有注释掉插入排序，需要放置lo >= hi的情况出现导致转换索引时开始出错
//		if (lo >= hi)
//			return;

		// 计算第d位字符出现频率
		// +2表示 count[0]==0为true。count[1]表示本轮该字符串已经结束的数量。加上键索引计数法本来多出一个
		int[] count = new int[R + 2];
		for (int i = lo; i <= hi; i++)
			count[charAt(a[i], d) + 2]++;
		// 将频率转换为索引
		for (int r = 0; r < R + 1; r++)
			count[r + 1] += count[r];
		// 数据排序分类
		for (int i = lo; i <= hi; i++)
			aux[count[charAt(a[i], d) + 1]++] = a[i];

		// 回写
//		for (int i = lo; i <= hi; i++)
//			a[i] = aux[i-lo];
		System.arraycopy(aux, 0, a, lo, hi-lo + 1);

		// 递归以每个字符作为键排序
		for (int r = 0; r < R; r++)
			// count[r]表示第d位字符相同的字符串数组递归比较下一位d+1字符
			if (count[r] < count[r+1])
				sort(a, lo + count[r], lo + count[r + 1] - 1, d + 1, aux);

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
			for (; j != lo && less(v, a[j-1], d); j--)
				a[j] = a[j - 1];
			a[j] = v;
		}
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

	/**
	 * 对一个正整数数组使用键索引计数法升序排序
	 * @param a
	 * @author Navy D
	 * @date 20170924211230
	 */
	public static void sort(int[] a) {
		if (a == null)
			throw new IllegalArgumentException("argument array is null");
		int n = a.length;
		int[] aux = new int[n];
		sort(a, 0, n - 1, 0, aux);

		assert isSorted(a);
	}

	/**
	 * 将一个整型数组使用键索引计数法排序。这个整型数组不支持负整数。原因在于移位后使用一个字节作为索引标记，一旦这个最高位是1，
	 * 这个负数就排在最大了。这个排序利用定长的键索引，不需要R+2的计数，使用255一个字节作为基数取索引。有一点不是很明白。
	 * a[i]>>shift。当d=4时，shift=-8，即a[i]>>-8.大概只有最高字节才能起作用，搞不清这个意思
	 *
	 * @param a
	 * @param lo
	 * @param hi
	 * @param d
	 * @param aux
	 * @author Navy D
	 * @date 20170924210325
	 */
	private static void sort(int[] a, int lo, int hi, int d, int[] aux) {

		if (hi <= lo + CUTOFF) {
			insertion(a, lo, hi, d);
			return;
		}

		// 定长4个字节，不需要标记结尾
		int[] count = new int[R + 1];
		int mask = R - 1; // 0xFF;
		// 将一个int值分为4个字节，每次取一个字节比较，d=0保留高8位,d=1保留高16位
		int shift = BITS_PER_INT - BITS_PER_BYTE * d - BITS_PER_BYTE;
		System.out.format("lo=%3d hi=%3d d=%3d shift=%3d\n", lo, hi, d, shift);
		// 将整数转化为256的余数索引
		for (int i = lo; i <= hi; i++) {
			int c = (a[i] >> shift) & mask; // 保留低8位
			// System.out.format("c=%d\n", c);
			count[c + 1]++;
		}

		for (int r = 0; r < R; r++)
			count[r + 1] += count[r];

		for (int i = lo; i <= hi; i++) {
			int c = (a[i] >> shift) & mask;
			aux[count[c]++] = a[i];
		}

		for (int i = lo; i <= hi; i++)
			a[i] = aux[i - lo];
		// 所有字节完成排序
		if (d == 4)
			return;

		// 现将高位非0字节排序
		// 当一个字节有许多重复如最高8位与之后多数 分类后count[0]=23...count[1]=3;很有 可能
		// 需要将一个数字的下一个字节继续比较直到找出一个字节不是0
		// 如果没有这个判断，count[0]=5,那么这5个数就直接跳过，即对于字节相等的就跳过了，错误
		if (count[0] > 0)
			sort(a, lo, lo + count[0] - 1, d + 1, aux);
		for (int r = 0; r < R; r++)
			// if (count[r+1] > count[r])// 可加可不加，插入排序可以避免lo>hi
			sort(a, lo + count[r], lo + count[r + 1] - 1, d + 1, aux);
	}

	private static void insertion(int[] a, int lo, int hi, int d) {
		for (int i = lo; i <= hi; i++)
			for (int j = i; j > lo && a[j] < a[j - 1]; j--)
				exch(a, j, j - 1);
	}

	private static void exch(int[] a, int i, int j) {
		int temp = a[i];
		a[i] = a[j];
		a[j] = temp;
	}

	private static boolean isSorted(int[] a) {
		for (int i = 1; i < a.length; i++)
			if (a[i] < a[i - 1])
				return false;
		return true;
	}

	public static void main(String[] args) {
		In in = new In("../MyAlgs/algs4-data/shells.txt");
		String[] a = in.readAllStrings();
		System.out.println(Arrays.toString(a));

		sort(a);

		System.out.println(Arrays.toString(a));

//		int[] s = {32, 68435457, 111,2147483647, 89, 5, 912};
//		Random rand = new Random();
//		s = new int[10];
//		for (int i = 0; i < s.length; i++)
//			s[i] = rand.nextInt(1000);
//		System.out.println(Arrays.toString(s));
//		sort(s);
//		System.out.println(Arrays.toString(s));

//		String[] aa = {"a", "b", "c"};
//		String[] bb = new String[10];
//		bb[1] = "bb";
//		bb[2]= "bb2";
//		bb[0] = "bb0";
//		System.arraycopy(aa, 1, bb, 1, aa.length-1);
//		System.out.println(Arrays.toString(bb));

	}

}
