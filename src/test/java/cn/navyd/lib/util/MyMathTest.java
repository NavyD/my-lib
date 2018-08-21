package cn.navyd.lib.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MyMathTest {
    @Test
    public void complementationTest() {
        int dividend = -7, divisor = -4;
        int quotient = dividend % divisor;
        
        assertEquals(quotient, MyMath.complementation(dividend, divisor));
    }
    
    @Test
    public void modTest() {
        int dividend = -7, divisor = 4;
        assertEquals(Math.floorMod(dividend, divisor), MyMath.mod(dividend, divisor));
    }
    
    @Test
    public void floorDivTest() {
        int dividend = -7, divisor = 4;
        assertEquals(Math.floorDiv(dividend, divisor), MyMath.floorDiv(dividend, divisor));
    }
}
