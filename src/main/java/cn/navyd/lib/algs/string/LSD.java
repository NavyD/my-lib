package cn.navyd.lib.algs.string;

import java.util.Arrays;
import java.util.Random;

/**
 * <h1>低位优先的字符串排序算法：从右到左的键索引计数法</h1>
 * 思想：通过计算每个字符串中特定位置的字符出现的频率，使用字符的顺序计算索引，再将数据分类,如此计算每个字符，可得到稳定的排序结果<br>
 * 复杂度：<br>
 * 需要访问7wn+3wR次数组，空间与n+R成正比，如果基数R远小于n，则运行时间与wn成正比，即与输入的规模成正比<br>
 * 稳定性：稳定<br>
 * 注意：<br>
 * 该算法仅适用与定长字符串。对于字符串数组中长度不一的字符串会报异常
 *
 * @author Navy D
 * @date 20170923203245
 */
public class LSD {

	private LSD() {

	}

	/**
	 * 按字符串前w个字符将字符串数组排序。
	 * @param a 要排序的字符串数组
	 * @param w 从1开始的w个字符
	 * @author Navy D
	 * @date 20170923202916
	 */
	public static void sort(String[] a, int w) {
		if (a == null)
			throw new IllegalArgumentException("argument array is null");
		checkLength(a);
		int n = a.length;
		// 扩展ascii字符集8位的基数大小
		int R = 256;
		// 辅助数组
		String[] aux = new String[n];
		// 根据第d个字符用键索引计数法排序。d为一个字符串的第d个字符
		for (int d = w - 1; d >= 0; d--) {
			// 计算字符串数组中每个d位字符出现的频率。使用r+1是为了方便count[r+1] += count[r];count[0]永远为0
			int[] count = new int[R+1];
			for (int i = 0; i < n; i++)
				count[a[i].charAt(d) + 1]++;
			// 将频率转化为索引，字符按从小到大排序
			for (int r = 0; r < R; r++)
				count[r+1] += count[r];
			// 数据分类。将对应编号的字符串从索引下标开始
			for (int i = 0; i < n; i++)
				aux[count[a[i].charAt(d)]++] = a[i];
			// 回写
//			for (int i = 0; i < n; i++)
//				a[i] = aux[i];
			System.arraycopy(aux, 0, a, 0, n);
		}

		assert isSorted(a);
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

	private static void checkLength(String[] a) {
		int len = a[0].length();
		for (int i = 1; i < a.length; i++)
			if (len != a[i].length())
				throw new IllegalArgumentException("arrays length inconformity");
	}

	static void test(char[] b, int R) {
		Random rand = new Random();
		for (int i = 0; i < b.length; i++) {
			int r = rand.nextInt(R);
			b[i] = (char) r;
		}
	}


	public static void main(String[] args) {
//		In in = new In("lsdtest.txt");
//		int n = in.readInt();
//		in.readLine();
//		String[] str = new String[n];
//		int i = 0;
//		while (!in.isEmpty()) {
//			str[i++] = in.readString() + in.readInt();
//			in.readLine();
//		}

		String s = "no is th ti fo al go pe to co to th ai of th pa";
		String[] a = s.split(" ");
		System.out.println(Arrays.toString(a));
		sort(a, 2);
		System.out.println(Arrays.toString(a));

	}


}
