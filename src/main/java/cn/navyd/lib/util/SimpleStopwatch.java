package cn.navyd.lib.util;

/**
 * 一个简单的Stopwatch实现。
 * @author navyd
 *
 */
public class SimpleStopwatch implements Stopwatch {
    private long totalTime;
    private boolean isStarted;
    
    public static SimpleStopwatch of() {
        return new SimpleStopwatch();
    }
    
    private SimpleStopwatch() {
        totalTime = 0;
        isStarted = false;
    }
    
    @Override
    public Stopwatch start() {
        checkRunning();
        isStarted = true;
        totalTime = System.currentTimeMillis();
        return null;
    }

    @Override
    public Stopwatch stop() {
        checkNotRunning();
        totalTime = System.currentTimeMillis() - totalTime;
        isStarted = false;
        return null;
    }

    @Override
    public boolean isRunning() {
        return isStarted;
    }

    @Override
    public long getTotalTimeMillis() {
        checkRunning();
        return totalTime;
    }

    @Override
    public long getTotalTimeSeconds() {
        checkRunning();
        return totalTime/1000;
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
}
