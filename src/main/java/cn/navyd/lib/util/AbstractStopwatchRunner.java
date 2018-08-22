package cn.navyd.lib.util;

import lombok.extern.slf4j.Slf4j;

/**
 * WatchRunner的抽象类。该类支持两种模式：普通模式，即支持WatchRunner接口所有用法。精确模式，即仅记录在run()中运行的时间，不支持WatchRunner的所有用法，仅支持lambda
 * @author navyd
 *
 */
@Slf4j
public abstract class AbstractStopwatchRunner implements WatchRunner {
    protected long totalTime;
    // runner.run()运行总时间。仅在run()中设置
    protected long runnerTime;
    private int freq;
    protected boolean isStarted;
    // 如果启动时totalTime=0则表示仅使用run(runner)记录时间。根据getStartTime的值判断。如果isOnlyRan = true则为精确模式，否则为普通模式
    protected boolean isOnlyRan;
    private Runner before;
    private Runner after;
    
    protected AbstractStopwatchRunner() {
        totalTime = 0;
        freq = 1;
        isStarted = false;
    }
    
    @Override
    public WatchRunner start() {
        checkRunning();
        totalTime = getStartTime();
        isOnlyRan = totalTime == 0;
        isStarted = true;
        // 清空runnertime
        runnerTime = 0;
        return this;
    }
    
    @Override
    public WatchRunner stop() {
        checkNotRunning();
        totalTime = getStopTime();
        isStarted = false;
        stopHandle();
        log.debug("totalTime: {}, other time: {}, runnerTime: {}", totalTime, getOtherTime(), runnerTime);
        return this;
    }
    
    @Override
    public boolean isRunning() {
        return isStarted;
    }
    
    @Override
    public WatchRunner run(Runner runner) {
        // 未启动
        checkNotRunning();
        // 运行次数， 运行总时间
        int time = freq;
        while (time-- > 0) {
            execute(before);
            runnerTime += execute(runner);
            execute(after);
        }
        runHandle();
        return this;
    }
    
    @Override
    public WatchRunner runThenStop(Runner runner) {
        run(runner);
        return stop();
    }
    
    @Override
    public WatchRunner frequency(int freq) {
        checkRunning();
        if (freq < 1)
            throw new IllegalArgumentException("freq:" + freq);
        this.freq = freq;
        return this;
    }
    
    @Override
    public WatchRunner before(Runner before) {
        checkRunning();
        this.before = before;
        return this;
    }
    
    @Override
    public WatchRunner after(Runner after) {
        checkRunning();
        this.after = after;
        return this;
    }
    
    @Override
    public long getTotalTimeMillis() {
        checkRunning();
        return totalTime;
    }

    @Override
    public long getTotalTimeSeconds() {
        return getTotalTimeMillis() / 1000;
    }

    @Override
    public long getAvgTimeMillis() {
        checkRunning();
        // 非runner.run的运行时间
        long runnerAvgTime = runnerTime / freq;
        return getOtherTime() + runnerAvgTime;
    }

    @Override
    public long getAvgTimeSeconds() {
        return getAvgTimeMillis() / 1000;
    }
    
    /**
     * 执行参数Runner.run()，同时记录方法执行时间间隔。
     * 如果指定Runner为null返回0
     * @param run
     */
    protected long execute(Runner run) {
        if (run == null)
            return 0;
        long period = System.currentTimeMillis();
        run.run();
        period = System.currentTimeMillis() - period;
        String prefix = null;
        if (run == before)
            prefix = "before";
        else if (run == after)
            prefix = "after ";
        else 
            prefix = "run   ";
        log.debug("{} period: {}", prefix, period);
        return period;
    }
    
    /**
     * 在stopwatch.run()运行时调用，而不是在runner.run前调用。
     */
    protected void runHandle() {
        
    }
    
    protected void stopHandle() {
        
    }
    
    /**
     * 在start()调用时记录时间。通常有两种实现：
     * <p>1.设置为0，仅记录runner.run()时间。
     * <p>2.设置为System.currentTimeMillis();，即表示从start()开始记录总时间
     */
    protected abstract long getStartTime();
    
    /**
     * 如果totalTime > 0，则表示使用stop停止时间记录，返回当前时间与开始时间totalTime之差.
     * 否则totalTime == 0，表示仅使用runner.run()的运行时间。即总时间就是runnerTime
     * @return
     */
    private long getStopTime() {
        if (isOnlyRan) 
            return runnerTime;
        return System.currentTimeMillis() - totalTime;
    }
    
    /**
     * 如果当前是在运行状态，则抛出异常
     */
    private void checkRunning() {
        if (isRunning())
            throw new IllegalArgumentException("操作错误！ Stopwatch正在运行");
    }
    
    /**
     * 如果当前不在运行状态，则抛出异常
     */
    private void checkNotRunning() {
        if (!isRunning())
            throw new IllegalArgumentException("操作错误！Stopwatch未运行");
    }
    
    /**
     * 从totalTime和runnerTime中获取非run()运行时间。如果仅允许使用run()计算时间，则返回0.
     * 否则需要从totalTime - runnerTime。一个runnerTime是执行run()所用
     * @return
     */
    private long getOtherTime() {
        if (isOnlyRan)
            return 0;
        return totalTime - runnerTime;
    }
}
