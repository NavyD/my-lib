package cn.navyd.lib.algs.tree;

public interface SimpleSymbolTable<Key, Value> {
	void put(Key key, Value val);

	Value get(Key key);

	void delete(Key key);

	boolean contains(Key key);

	boolean isEmpty();

	int size();

	Iterable<Key> keys();
}
