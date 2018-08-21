package cn.navyd.lib.util;

import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

	/**
	 * url查询参数正则表达式
	 */
	private static final String URL_QUERY_STRING_REGEX = "((^|&)([^=]+)=)([^&]+)";

	private static final Pattern URL_QUERY_STRING_PATTERN = Pattern.compile(URL_QUERY_STRING_REGEX);

	/**
	 * <p>
	 * 设置url 中的查询参数，并返回修改后的查询参数(不包括'?');
	 * <p>
	 * 如果urlQueryString==null将直接返回新的查询参数
	 * <p>
	 * 如果在url中找到paramName，且paramValue != null，将参数值设置为paramValue。 如果paramValue
	 * == null，就删除该参数。
	 * <p>
	 * 如果在url中找不到paramName，且paramValue != null就在url中添加新的参数和值
	 * <p>
	 * 注意：实现使用正则表达式替换字符串。传入的urlQueryString如果不是标准的查询参数字符串时不保证正确
	 *
	 * @param paramName
	 *            参数名
	 * @param paramValue
	 *            参数值(允许为null，如果存在时删除参数，否则异常)
	 * @param urlQueryString
	 *            url查询参数
	 * @return 新的url 查询参数(不包括'?')
	 */
	public static String setUrlParam(String paramName, String paramValue, String urlQueryString) {
		if (paramName == null)
			throw new NullPointerException("paramName==null");
		if (paramValue != null && paramValue.length() == 0)
			throw new IllegalArgumentException("不允许paramValue.length=0");
		// 如果传入的url为空 就返回新的url参数
		if (urlQueryString == null) {
			if (paramValue == null)
				throw new NullPointerException("urlQueryString==null时，不允许paramValue==null");
			return paramName + "=" + paramValue;
		}
		Matcher m = URL_QUERY_STRING_PATTERN.matcher(urlQueryString);
		StringBuffer sb = new StringBuffer();
		while (m.find())
			if (paramName.equals(m.group(3))) {
				// 删除 该参数
				if (paramValue == null)
					m.appendReplacement(sb, "");
				else // 更新参数值
					m.appendReplacement(sb, "$1" + paramValue);
				m.appendTail(sb);
				// 删除第一个参数时需要删除第一个字符'&'
				if (m.start() == 0 && paramValue == null)
					sb.delete(0, 1);
				return sb.toString();
			}

		// 添加参数 不允许paramValue=null
		if (paramValue == null)
			throw new NullPointerException("找不到 " + paramName + " 参数，添加到url时paramValue==null");

		sb.append(urlQueryString).append("&").append(paramName).append("=").append(paramValue);
		return sb.toString();
	}

	/**
	 * 返回字符串s被md5加密后的全小写加密字符
	 *
	 * @param s
	 * @return
	 */
	public final static String MD5(String s) {
		try {
			if (s == null)
				throw new NullPointerException();
			char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
			byte[] btInput = s.getBytes();
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j << 1];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str).toLowerCase();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static void testSetUrlParam() {
		String urlQueryString = "selected_field=name&search_word=t&search_page=0";
		String paramName = "selected_field";
		String paramValue = "test";
		System.out.println(setUrlParam(paramName, paramValue, urlQueryString));

		System.out.println(urlQueryString.replaceAll(URL_QUERY_STRING_REGEX, "$1test"));
	}

	public static void main(String[] args) {
		testSetUrlParam();
	}

}
