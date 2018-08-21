package cn.navyd.lib.algs.string;

import java.util.Arrays;

/**
 * KMP字符串查找算法：
 * <p>kmp的核心思想是使用模式字符串的前后缀取得在文本中回退的位置
 * 暂时无法正确表达。
 * 详见<a href="http://blog.csdn.net/v_july_v/article/details/7041827">从头到尾彻底理解KMP</a>
 * <p>复杂度：长度为m的模式字符串和长度为n的文本，访问次数为m+n次
 * @author Navy D
 * @date 20171011090300
 */
public class KMP {

	/**
	 * 根据模式字符串最大公共前后缀长度求next数组
	 * next[]: 保存失配字符的上一位字符所对应的最大长度值
	 * next 数组相当于“最大长度值” 整体向右移动一位，然后初始值赋为-1
	 * @param s
	 * @return
	 * @author Navy D
	 * @date 20171010190040
	 */
	private static int[] next(String pat) {
		int len = pat.length();
		int[] next = new int[len];
		next[0] = -1;
		int j = 0;
		int k = -1;
		while (j < len-1) {
			// pat[k]表示前缀，pat[j]表示后缀，检查下一个字符表示的前后缀是否相同
			if (k == -1 || pat.charAt(k) == pat.charAt(j)) {
				k++;
				j++;
				// 不能允许p[j] = p[ next[j]]，避免失配移位后必然的失败
				if (pat.charAt(j) != pat.charAt(k))
					next[j] = k;
				else
					// 因为不能出现p[j] = p[ next[j ]]，所以当出现时需要继续递归，k = next[k] =
					// next[next[k]]
					next[j] = next[k];
			} else {
				// 失配后只能从开头找长度更短的前后缀，递归
				k = next[k];
			}
		}
		return next;
	}

	/**
	 * 返回与模式字符串匹配的文本串中首字符出现的位置。如果找不到，就返回-1
	 * @param s
	 * @param pat
	 * @return
	 * @author Navy D
	 * @date 20171010183937
	 */
	public static int kmpSearch(String s, String pat) {
		// 文本字符位置
		int i = 0;
		// 模式字符位置
		int j = 0;
		int[] next = next(pat);
		while (i < s.length() && j < pat.length()) {
			if (j == -1 || s.charAt(i) == pat.charAt(j)) {
				i++;
				j++;
			} else
				// next[j]即为j所对应的next值，令i不变，移动模式字符与之匹配
				j = next[j];
		}
		return j == pat.length() ? i-j : -1;
	}


	public static void main(String[] args) {
		String s = "DABCDABDE";
		s = "abab";
		String test = "abacababc";
		int[] next = next(s);
		System.out.println(Arrays.toString(next));

		System.out.println(kmpSearch(test, s));


	}

}
