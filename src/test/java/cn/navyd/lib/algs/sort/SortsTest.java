package cn.navyd.lib.algs.sort;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SortsTest {
    private Random random = new Random();
    private Integer[] a;
    
    @Before
    public void setArray() {
        int len = 100;
        a = new Integer[len];
        for (int i = 0; i < len; i++)
            a[i] = random.nextInt();
    }
    
    @Test
    public void exchTest() {
        int a = 2, b = 5;
        log.debug("before exch. a: {}, b: {}", a, b);
        a ^= b;
        b ^= a;
        a ^= b;
        log.debug("after  exch. a: {}, b: {}", a, b);
    }
    
    @Test
    public void selectionTest() {
        assertFalse(Sorts.isSorted(a));
        
        Sorts.selection(a);
        
        assertTrue(Sorts.isSorted(a));
    }
    
    @Test
    public void insertionTest() {
        assertFalse(Sorts.isSorted(a));
        
        Sorts.insertion(a);
        
        assertTrue(Sorts.isSorted(a));
    }
    
}
