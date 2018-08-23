package cn.navyd.lib.algs.sort;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import org.junit.Ignore;
import org.junit.Test;

import cn.navyd.lib.util.Sorts;
import cn.navyd.lib.util.StrictWatchRunner;
import cn.navyd.lib.util.WatchRunner;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SortsTest {
    private Random random = new Random(47);
    private Integer[] a;
    private WatchRunner watch = StrictWatchRunner.of();
    private int freq = 1;
    
    public void setArray() {
        int len = 5000000;
        a = new Integer[len];
        for (int i = 0; i < len; i++)
            a[i] = random.nextInt(len*10);
        
        assertFalse(Sorts.isSorted(a));
    }

    public void exchTest() {
        int a = 2, b = 5;
        log.debug("before exch. a: {}, b: {}", a, b);
        a ^= b;
        b ^= a;
        a ^= b;
        log.debug("after  exch. a: {}, b: {}", a, b);
    }

    @Ignore
    @Test
    public void selectionTest() {
        watch.frequency(1)
                .before(() -> setArray())
                .after(() -> assertTrue(Sorts.isSorted(a)))
                .start()
                .runThenStop(() -> Sorts.selection(a));
        log.debug("ms: {}", watch.getAvgTimeMillis());
    }
    
    @Ignore
    @Test
    public void insertionTest() {
        watch.frequency(freq)
                .before(() -> setArray())
                .after(() -> assertTrue(Sorts.isSorted(a)))
                .start()
                .runThenStop(() -> Sorts.insertion(a));
        log.debug("ms: {}", watch.getAvgTimeMillis());
//        log.debug("{}", Arrays.toString(Arrays.copyOfRange(a, start, end)));
    }

    @Ignore
    @Test
    public void shellTest() {
        watch.frequency(freq)
                .before(() -> setArray())
                .after(() -> assertTrue(Sorts.isSorted(a)))
                .start()
                .runThenStop(() -> Sorts.shell(a));
        log.debug("ms: {}", watch.getAvgTimeMillis());
    }

    @Test
    public void mergerTest() {
        watch.frequency(freq)
                .before(() -> setArray())
                .after(() -> assertTrue(Sorts.isSorted(a)))
                .start()
                .runThenStop(() -> Sorts.merger(a));
        log.debug("ms: {}", watch.getAvgTimeMillis());
    }
    
    @Test
    public void quickTest() {
        watch.frequency(freq)
                .before(() -> setArray())
                .after(() -> {
                    assertTrue(Sorts.isSorted(a));
                })
                .start()
                .runThenStop(() -> Sorts.quick(a));
        log.debug("ms: {}", watch.getAvgTimeMillis());
    }
    
    @Test
    public void headTest() {
        watch.frequency(freq)
                .before(() -> {
                    setArray();
                })
                .after(() -> {
                    assertTrue(Sorts.isSorted(a));
                })
                .start()
                .runThenStop(() -> Sorts.heap(a));
        log.debug("ms: {}", watch.getAvgTimeMillis());
    }
}
