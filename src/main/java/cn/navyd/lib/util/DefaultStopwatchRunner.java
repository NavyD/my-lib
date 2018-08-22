package cn.navyd.lib.util;

/**
 * 一个对WatchRunner的完全实现。支持WatchRunner的所有用法。
 * <p>注意：如果使用lambda在run()，即使没有在stop()前调用任何方法，其中的otherTime也会有100ms以上的时间，这不是bug，而是java lambda需要执行的时间。
 * @author navyd
 *
 */
public class DefaultStopwatchRunner extends AbstractStopwatchRunner {

    @Override
    protected long getStartTime() {
        return System.currentTimeMillis();
    }
    
    public static DefaultStopwatchRunner of() {
        return new DefaultStopwatchRunner();
    }
}
