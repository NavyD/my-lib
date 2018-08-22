package cn.navyd.lib.util;

/**
 * 一个仅对lambda表达式计时的WatchRunner实现。仅支持对WatchRunner.run()中的方法时间的记录，放弃了对start()方法开始的记录时间，记录时间更加精确。
 * <p>
 * 注意：该类对于start()与stop()之间的外部方法不会记录时间，当前实现不会报错。如果该类不在WatchRunner.run()运行，则抛出异常
 * <p>典型用法：
 * <pre>watch.frequency(1).start().run(() -> Sorts.merger(a)).stop();
 * </pre>
 * <p>下面的典型用法将 不支持!
 * <pre>
 * watch.start(); 
 * Sorts.merger(a); 
 * watch.stop();
 * </pre>
 * 
 * @author navyd
 *
 */
public class StrictWatchRunner extends AbstractStopwatchRunner {
    @Override
    protected long getStartTime() {
        return 0;
    }

    public static StrictWatchRunner of() {
        return new StrictWatchRunner();
    }
}
