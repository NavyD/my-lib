package cn.navyd.lib.util;

/**
 * 一个允许使用lambda表达式中方法的执行时间，该方法更加精确。同时允许编程式的start, stop记录执行时间，也可以结合两者同时使用。
 * <p>使用方式1:
 * <pre>
 *  watch.setFrequent(freq).start().runThenStop(() -> Sorts.selection(a));
 *  log.debug("time ms: " + stopwatch.getAvgTimeMillis())
 * </pre>
 * <p>使用方式2:
 * <pre>
 *  watch.setFrequent(freq).start();
 *  obj.valid();
 *  watch.runThenStop(() -> Sorts.selection(a));
 *  log.debug("time ms: " + stopwatch.getAvgTimeMillis())
 * </pre>
 * @author navyd
 *
 */
public interface WatchRunner extends Stopwatch {
    // 修改Stopwatch对应的返回值
    WatchRunner start();
    WatchRunner stop();
    
    WatchRunner run(Runner runner);
    
    WatchRunner runThenStop(Runner runner);
    
    /**
     * 设置在run()方法中的Runner调用次数
     * @param freq
     * @return
     */
    WatchRunner frequency(int freq);
    
    /**
     * 在每次run()中设置的runner.run()之前调用的方法，如果设置frequency将调用相同次数，不计入统计时间。需要在start前调用。
     * @param before
     * @return
     */
    WatchRunner before(Runner before);
    
    /**
     * 在每次run()中设置的runner.run()之后调用运行的方法，如果设置frequency将调用相同次数，不计入统计时间。在start前调用。
     * @param after
     * @return
     */
    WatchRunner after(Runner after);
    
    /**
     * 获取平均时间。该时间在设置frequency时，仅平均Runner.run()的运行时间，而start()开始的其他时间不会平均
     * @return
     */
    long getAvgTimeMillis();
    
    long getAvgTimeSeconds();
    
    @FunctionalInterface
    public static interface Runner {
        void run();
    }
}
