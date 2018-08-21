package cn.navyd.lib.util;

/**
 * 专门创建对象的生成器。属于工厂方法设计模式，不需要传入任何参数，只需要对应类实现该方法就行。
 * @author Navy D
 */
public interface Generator<T> {
	/**
	 * 返回一个创建的新对象
	 * @return
	 */
	T next();
}
