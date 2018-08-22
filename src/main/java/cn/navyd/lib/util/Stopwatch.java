package cn.navyd.lib.util;

/**
 * 一个计时器。典型使用方式
 * <pre>
 *  stopwatch.start();
 *  obj.valid();
 *  stopwatch.stop();
 *  log.debug("time ms: " + stopwatch.getTotalTimeMillis())
 * </pre>
 * @author navyd
 *
 */
public interface Stopwatch {
    /**
     * 开始记录。多次调用将会抛出异常
     * @return
     */
    Stopwatch start();
    
    /**
     * 停止记录。多次调用不影响
     * @return
     */
    Stopwatch stop();
    
    boolean isRunning();
    
    // get*仅在停止时获取
    
    long getTotalTimeMillis();
    
    long getTotalTimeSeconds();
}
