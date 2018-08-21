package cn.navyd.lib.algs.string;

public interface StringST<Value> {
	void put(String key, Value val);

	Value get(String key);

	void delete(String key);

	boolean contains(String key);

	boolean isEmpty();

	String longestPrefixOf(String s) ;

	Iterable<String> keysWithPrefix(String s);

	Iterable<String> keysThatMatch(String s);

	int size();

	Iterable<String> keys();


}
