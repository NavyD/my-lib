package cn.navyd.lib.util;

import java.util.Random;

/**
 * 提供基本数据类型及String的随机填充
 * 默认固定随机数种子。
 * @author Navy D
 */
public class RandomGenerator {
	private static Random r = new Random(47);

	/**
	 * 如果rand=true就将random随机
	 * @param random
	 */
	public static void setRandom(boolean rand) {
		if (rand)
			r = new Random(new Random().nextLong());
	}

	/**
	 * 提供boolean的随机填充
	 * @author Navy D
	 */
	public static class Boolean implements Generator<java.lang.Boolean> {
		/**
		 * 返回一个随机的boolean
		 * @author Navy D
		 */
		public java.lang.Boolean next() {
			return r.nextBoolean();
		}
	}

	/**
	 * 提供byte数据的随机填充
	 * @author Navy D
	 */
	public static class Byte implements Generator<java.lang.Byte> {
		public java.lang.Byte next() {
			return (byte) r.nextInt();
		}
	}

	/**
	 * 提供char的数据随机填充。
	 * 注意：该类只提供26个字母的大小写中随机字符填充
	 * @author Navy D
	 */
	public static class Character implements Generator<java.lang.Character> {
		public java.lang.Character next() {
			return CountingGenerator.chars[r.nextInt(CountingGenerator.chars.length)];
		}
	}

	/**
	 * 提供String数据随机填充。
	 * 注意：默认提供7位字符串的长度。仅提供26个字母的大小写的字符随机
	 * 使用当前RandomGenerator.Character类提供随机实现
	 * @author Navy D
	 */
	public static class String extends CountingGenerator.String {
		// Plug in the random Character generator:
		{	//使用当前RandomGenerator.Character类提供随机实现
			cg = new Character();
		} // Instance initializer

		public String() {
		}

		/**
		 * 将创建长度为length的字符串
		 * @param length
		 */
		public String(int length) {
			super(length);
		}
	}

	/**
	 * 提供short数据的随机填充
	 * @author Navy D
	 */
	public static class Short implements Generator<java.lang.Short> {
		public java.lang.Short next() {
			return (short) r.nextInt();
		}
	}

	/**
	 * 提供int数据的随机填充
	 * 注意：默认最大值mod=10000
	 * @author Navy D
	 */
	public static class Integer implements Generator<java.lang.Integer> {
		private int mod = 10000;

		public Integer() {
		}

		public Integer(int modulo) {
			mod = modulo;
		}

		public java.lang.Integer next() {
			return r.nextInt(mod);
		}
	}

	/**
	 * 提供long数据随机填充
	 * 注意：默认最大值mod=10000
	 * @author Navy D
	 */
	public static class Long implements Generator<java.lang.Long> {
		private int mod = 10000;

		public Long() {
		}

		public Long(int modulo) {
			mod = modulo;
		}

		public java.lang.Long next() {
			return new java.lang.Long(r.nextInt(mod));
		}
	}

	/**
	 * 提供float的数据随机填充
	 * @author Navy D
	 */
	public static class Float implements Generator<java.lang.Float> {
		public java.lang.Float next() {
			// Trim all but the first two decimal places:
			int trimmed = Math.round(r.nextFloat() * 100);
			return ((float) trimmed) / 100;
		}
	}

	/**
	 * 提供double的数据随机填充
	 * @author Navy D
	 */
	public static class Double implements Generator<java.lang.Double> {
		public java.lang.Double next() {
			long trimmed = Math.round(r.nextDouble() * 100);
			return ((double) trimmed) / 100;
		}
	}
} /// :~
