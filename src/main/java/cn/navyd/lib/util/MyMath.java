package cn.navyd.lib.util;

/**
 * 该类用于一般的数学公式计算。与{@code java.lang.Math}不同的是，该类仅用于学习，不打算实际使用
 * @see java.lang.Math
 * @author navyd
 *
 */
public class MyMath {
    /**
     * 取余运算。取余操作：{@code q = dividend / divisor, r = dividend - (q * divisor)}
     * <p>java运算符"%"的操作为取余运算。
     * <p>与取模mod的区别：如果dividend，divisor的符号相反，对于取余运算，商q的值向0方向舍入。对于取模运算，商q的值向负无穷舍入。如果符号一致，则两者完全一致
     * <p>如：q = -7 / 4，取余： q = -1.75 ==> q = -1，r = -7 - (-1 * 4) = -3。取模： q = -1.75 ==> q = -2，r = -7 - (-2 * 4) = 1。
     * <p>对java除法运算符"/"取整 来说，正好对应了取余操作。而取模需要向下舍入
     * @param dividend
     * @param divisor
     * @return
     */
    public final static long complementation(long dividend, long divisor) {
        return dividend % divisor;
    }
    
    /**
     * 取模操作与取余操作步骤完全一致。仅当dividend，divisor符号不同时结果不同。{@linkplain #complementation(long, long) complementation}
     * @param dividend
     * @param divisor
     * @return
     */
    public final static long mod(long dividend, long divisor) {
        long quotient = floorDiv(dividend, divisor);
        return dividend - quotient * divisor;
    }
    
    /**
     * 对除法的商向下取整。对应关系：{@code dividend ÷ divisor = quotient <===> 被除数 ÷ 除数 = 商}
     * <p>如果dividend和divisor的符号一致 或 为整除运算时，则与 {@code dividend/divisor} 的结果一致。
     * <li>如：floorDiv(7, 4), floorDiv(-7, -4), floorDiv(-8, 4)与自带的 / 结果一致</li>
     * <p>如果两者符号不一致且不是整除时，则为 {@code dividend/divisor -1}。
     * <li>如：floorDiv(-7, 4)在自带的 / 结果上 - 1。</li>
     * <p>注意：
     * <ul>
     * <li>java除法运算"/"是取整，而不是舍入，即丢弃小数部分。1.75 ==> 1
     * <li>java异或运算符"^"，而不是指数运算符
     * <li>判断两个数是否符号是否一致，可以使用 * 判断 < 0。或者 使用异或运算 ^ 判断 < 0即可（最高位相反则 为 1为负数）
     * </ul>
     * @param dividend 被除数
     * @param divisor 除数
     * @return
     */
    public final static long floorDiv(long dividend, long divisor) {
        long quotient = dividend / divisor;
        // 符号不一致，并且需要存在余数，非整除运算 如 -4 / 2 = -2，这是整除运算，但是不能 - 1
        if ((dividend ^ divisor) < 0 && (quotient * divisor != dividend))
            quotient --;
        return quotient;
    }
}
