package cn.navyd.lib.algs.tree;

import java.util.NoSuchElementException;

import cn.navyd.lib.algs.util.In;
import cn.navyd.lib.algs.util.Queue;

/**
 * <p>基于红黑树的符号表：</p>
 * 规则：
 * <li>1.每个节点只能是红色或黑色
 * <li>2.根节点是黑色
 * <li>3.不能出现连续的两个红色节点
 * <li>4.所有从根节点到树底端的简单路径中的黑色节点数量一致
 * <br>
 * 复杂度：<p>
 * 大小为N的红黑树的高度不会超过2lgn。即叶子节点之间最大高度差为2倍
 * 根节点到任意节点的平均长度是1.00lgn
 * @author Navy D
 * @date 20170829094424
 * @param <K>
 * @param <V>
 */
public class RedBlackBST<K extends Comparable<K>, V> implements ST<K, V>{
	private static final boolean RED = true;
	private static final boolean BLACK = false;

	private Node<K, V> root;

	private static class Node<K, V> {
		private K key;
		private V val;
		private Node<K, V> left, right;
		private boolean color;
		private int n;

		public Node (K key, V val, int n, boolean color) {
			this.key = key;
			this.val = val;
			this.n = n;
			this.color = color;
		}
	}

	/**
	 * 如果节点为红色就返回false。如果h为null就返回false
	 */
	private boolean isRed(Node<K, V> h) {
		return h != null ? h.color == RED : false;
	}

	/**
	 * 如果符号表为红就返回true
	 */
	@Override
	public boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * 返回符号表中键的数量
	 * @author Navy D
	 * @date 20171008110251
	 */
	@Override
	public int size() {
		return size(root);
	}

	/**
	 * 返回节点h的子节点数量。如果h为null就返回0
	 */
	private int size(Node<K, V> h) {
		return h != null ? h.n : 0;
	}

	/**
	 * 返回符号表中指定的key关联的键值。如果符号表中不存在指定的key就返回null
	 * @author Navy D
	 * @date 20171008110542
	 */
	@Override
	public V get(K key) {
		if (key == null)
			throw new IllegalArgumentException("argument to get() is null");
		return get(root, key);
	}
	/**
	 * 递归
	 * @param h
	 * @param key
	 * @return
	 * @author Navy D
	 * @date 20170823213203
	 */
	private V get(Node<K, V> h, K key) {
		if (h == null)
			return null;
		int cmp = key.compareTo(h.key);
		if (cmp < 0)
			return get(h.left, key);
		else if (cmp > 0)
			return get(h.right, key);
		else
			return h.val;
	}

	/**
	 * 如果指定的key存在符号表中就返回true
	 * @author Navy D
	 * @date 20171008110638
	 */
	@Override
	public boolean contains(K key) {
		return get(key) != null;
	}

	/**
	 * 在符号表中插入指定的键值对。如果val=null，就删除符号表中存在的对应key
	 *
	 * @author Navy D
	 * @date 20171008110710
	 */
	@Override
	public void put(K key, V val) {
		if (key == null)
			throw new IllegalArgumentException("first argument to put() is null");
		// 如果是null值，表示立即执行删除
		if (val == null) {
			delete(key);
			return;
		}
		root = put(root, key, val);

		// 保持根节点为黑色，一旦根节点由红变黑表示树的高度+1
		root.color = BLACK;
		assert check();
	}

	/**
	 * 插入情况：
	 * 在2-节点插入
	 * 1.如果新节点比父节点大成为红右节点。就左旋转为左红连。否则成为左红链接不需要旋转
	 * 在3-节点插入
	 * 2.如果新节点比左边父节点小成为它的红左链接，即连续的两条红链接，需要右旋转为4-节点。
	 * 3.如果新节点比右边父节点大成为它的红右链接，即成为4-节点。
	 * 4.如果新节点比左边父节点大且比右边父节点小就成为左边父节点的右红链接，需要左旋转后成为连续两条红链接后右旋转成为4-节点
	 * 5.所有的4-节点颜色转换向上传递颜色转换为2-节点
	 * @param h
	 * @param key
	 * @param val
	 * @return
	 * @author Navy D
	 * @date 20170823215306
	 */
	private Node<K, V> put(Node<K, V> h, K key, V val) {
		if (h == null)
			return new Node<>(key, val, 1, RED);
		// *******如果在这里判断flipCorlors，就会变成2-3-4树算法
		// *******该算法允许4-节点的存在，向下首先分解4-节点，使成为2-节点，插入后向上配平4-节点
		//查找key是否存在，不存在则连接新节点
		int cmp = key.compareTo(h.key);
		if (cmp < 0)
			h.left = put(h.left, key, val);
		else if (cmp > 0)
			h.right = put(h.right, key, val);
		else
			h.val = val;
		// 先处理插入在2-节点的右红链接转换为左链接3-节点。插入到左边的红链接如果与父节点构成连续的两个红链接会在下一个if处理
		//如果是红色右链接，则左旋转，不管插入在2-节点还是3-节点。后者会到下一条if
		if (isRed(h.right) && !isRed(h.left))
			h = rotateLeft(h);
		// 插入在3-节点的左红链接构成4-节点
		// 处理插入旋转导致的连续两个红链接和向上传递的
		//如果是连续的两个红色左链接，则右旋转
		if (isRed(h.left) && isRed(h.left.left))
			h = rotateRight(h);
		//如果是左右都为红色连接，则颜色转换
		if (isRed(h.left) && isRed(h.right))
			flipColors(h);
		//更新子节点的值
		h.n = 1 + size(h.left) + size(h.right);
		return h;
	}

	/**
	 * 删除符号表中的最小键值对
	 * @author Navy D
	 * @date 20171008110957
	 */
	@Override
	public void deleteMin() {
	     if (isEmpty())
	    	 throw new NoSuchElementException("BST underflow");
		// root左右子节点都是黑色 是2-节点，默认根节点都是黑色的，但是在实现中要求根节点需要为红色
		if (!isRed(root.left) && !isRed(root.right))
			root.color = RED;
		root = deleteMin(root);
		//根节点还存在，恢复黑色
		if (!isEmpty())
			root.color = BLACK;
		  System.out.println("min=" + size());
		assert check();
	}

	/**
	 * 算法：
	 * 当前节点的左子节点不是2-节点，一个3-节点的左节点可直接删除
	 * 当前节点的左子节点是2-节点，如果右子节点是2-节点，则将这三个节点作为4-节点(转变颜色)，删除后变为只有一个右链接再平衡为3-节点
	 * 当前节点的左子节点是2-节点，如果右子节点不是2-节点，则需要移动元素使成为5-节点，删除后成为4-节点后平衡
	 *
	 * 如果一个节点是2-节点，且这个节点的兄弟节点是非2-节点，需要移动兄弟节点使当前节点与原父节点转换为3-节点()
	 * 如果兄弟节点是2-节点，直接转化为3-或4-节点
	 * 使用h.left.left表示要删除的节点这样删除后h.left=null可以直接赋值
	 * @param h
	 * @return
	 * @author Navy D
	 * @date 20170823221827
	 */
	private Node<K, V> deleteMin(Node<K, V> h) {
		if (h.left == null)
			return null;
		// 判断一个节点h.left是否是一个2-节点
		//当前节点的左节点是2-节点，多一层判断h.left.left是表示左节点是2-节点，如果没有就可能下一个是红链接
		if (!isRed(h.left) && !isRed(h.left.left)) {
			h = moveRedLeft(h);
		}
		h.left = deleteMin(h.left);
		return balance(h);
	}

	/**
	 * 删除符号表中最大的键值对
	 * @author Navy D
	 * @date 20171008111028
	 */
	@Override
	public void deleteMax() {
		 if (isEmpty())
			 throw new NoSuchElementException("BST underflow");
		if (!isRed(root.left) && !isRed(root.right))
			root.color = RED;
		root = deleteMax(root);
		if (!isEmpty())
			root.color = BLACK;

		assert check();
	}

	/**
	 * 算法：
	 * 当前节点的右子节点不是2-节点，右旋转后为右链接可直接删除
	 * 当前节点的右子节点是2-节点，如果左子节点是2-节点，将这3个节点转换为4-节点，可直接删除
	 * 当前节点的右子节点是2-节点，如果左子节点不是2-节点，则需要移动元素使右子节点成为右链接的3-节点，可直接删除
	 * @param h
	 * @return
	 * @author Navy D
	 * @date 20170823221230
	 */
	private Node<K, V> deleteMax(Node<K, V> h) {
		//当右链接节点是一个3-节点左链接一个节点,右旋转一下就可以删除
		if (isRed(h.left))
			h = rotateRight(h);
		if (h.right == null)
			return null;
		// 如果 h.right是一个2-节点就变为3-节点如果h.left的兄弟节点非2-节点就移动左边节点过来
		if (!isRed(h.right) && !isRed(h.right.left))
			h = moveRedRight(h);
		h.right = deleteMax(h.right);
		return balance(h);
	}

	/**
	 * 删除符号表中指定的键值对
	 * @author Navy D
	 * @date 20171008111048
	 */
	@Override
	public void delete(K key) {
		if (key == null)
			throw new IllegalArgumentException("argument to delete() is null");
		if (!contains(key))
			return;
		if (!isRed(root.left) && !isRed(root.right))
			root.color = RED;
		root = delete(root, key);
		if (!isEmpty())
			root.color = BLACK;
		assert check();
	}

	/**
	 * 算法：
	 * 在key向下的过程中，将2-节点创建为3-或4-节点，让可能的节点成为可删除状态
	 * 找到key后使用右子树的最小值替换当前node,
	 *
	 * 删除重点在于对于遇到的所有2-节点均要变为3-或4-节点。如果他的兄弟节点是一个2-节点就直接将这3个节点组成4-节点。如果
	 * 兄弟节点是一个非2-节点，就需要移动元素使成为3-节点
	 * @param h
	 * @param key
	 * @return
	 * @author Navy D
	 * @date 20170823220822
	 */
	private Node<K, V> delete(Node<K, V> h, K key) {
		//如果key在左子树中
		if (key.compareTo(h.key) < 0) {
			//如果当前节点的左子节点是2-节点，就使当前节点与子节点组成3-或4-节点
			if (!isRed(h.left) && !isRed(h.left.left))
				h = moveRedLeft(h);
			h.left = delete(h.left, key);
		}
		//key在右子树中(根节点也算在右子树)
		else {
			//如果当前节点的左节点不是2-节点，就使右旋转成右链接可删除状态
			if (isRed(h.left))
				h = rotateRight(h);
			//如果找到当前节点并且没有右节点，删除该节点(如果只有2个元素，左节点为根，如果有多个元素，就会进入后面的分解为可删除状态)
			if (key.compareTo(h.key) == 0 && h.right == null)
				return null;
			//如果右节点是2-节点，就移动元素变成3-或4-节点
			if (!isRed(h.right) && !isRed(h.right.left))
				h = moveRedRight(h);
			//如果找到节点有右节点
			if (key.compareTo(h.key) == 0) {
				//取右子树中最小的元素替换当前元素
				Node<K, V> x = min(h.right);
				//只替换值，连接不用替换
				h.key = x.key;
				h.val = x.val;
				h.right = deleteMin(h.right);
			}
			//不等就继续找
			else
				h.right = delete(h.right, key);
		}
		//向上平衡
		return balance(h);
	}


	/**
	 * 左旋转：
	 * 2-3红黑树出现红色右链接时，需要转换为红色左连接
	 * 即转换两节点及子树的的位置，按2-3树的规则顺序交换
	 * 旋转需要改变子树连接使有序
	 * @param h
	 * @return
	 * @author Navy D
	 * @date 20170822170850
	 */
	private Node<K, V> rotateLeft(Node<K, V> h) {
		//红色右链接节点
		Node<K, V> x = h.right;
		h.right = x.left;
		x.left = h;
		//将原节点的链接颜色赋给转换的节点
		x.color = h.color;
		//将左链接置为红链接
		h.color = RED;
		//更新节点的子节点数量，交换的是h，原来有2-3个节点还是多少个
		 x.n = h.n;
		//重新计算有新子树交换过来的新紅链接节点
		 h.n = size(h.left) + size(h.right) + 1;
		return x;
	}
	/**
	 * 右旋转：
	 * 2-3红黑树出现连续的两个左链接(4-节点)时，需要交换节点位置
	 * 还需要转变颜色
	 * @param h
	 * @return
	 * @author Navy D
	 * @date 20170822172620
	 */
	private Node<K, V> rotateRight(Node<K, V> h) {
		Node<K, V> x = h.left;
		h.left = x.right;
		x.right = h;
		x.color = h.color;
		//将左链接置为红链接
		h.color = RED;
		x.n = h.n;
		//卧槽   ，这个bug搞了我2个小时啊 啊啊啊啊日，老子为了它，居然先对比一遍发现不了，又全复制过来到这才发现的  日
//		h.n = size(x.left) + size(x.right) + 1;
		h.n = size(h.left) + size(h.right) + 1;
		return x;
	}

	/**
	 * 颜色转换：
	 *  2-3红黑树出现左右两紅链接(4-节点)，需要将父链接变红，子节点的链接都变黑，
	 * @param h
	 * @author Navy D
	 * @date 20170822172929
	 */
	private void flipColors(Node<K, V> h) {
		h.color = !h.color;
		h.left.color = !h.left.color;
		h.right.color = !h.right.color;
	}

	/**
	 * 节点h为红色，h.left与h.left.left都是黑色
	 * 由于put()算法保证了一个节点的两个子节点是黑色则父节点一定是红色
	 * 默认插入的节点是红色，一旦一个节点有两个紅链接，最后都会颜色转换为黑色子链接
	 * 与红色父节点
	 *
	 * h节点的左子节点是2-节点才能使用该方法
	 * 如果是h的左右子节点都是2-节点，则将这三个节点转化为4-节点
	 * 如果是h的右子节点不是2-节点，则移动右子节点一个元素作为父节点，使左子节点与原来的父节点组成3-节点
	 * @param h
	 * @return
	 * @author Navy D
	 * @date 20170822210454
	 */
	private Node<K, V> moveRedLeft(Node<K, V> h) {
		//两种情况都需要转化颜色，先后情况不影响
		flipColors(h);
		//当前节点的左子节点是2-节点，并且兄弟节点是不是2-节点，将左子节点的兄弟节点的一个键移动到左子节点中
		if (isRed(h.right.left)) {
			//将连接变成右红链接，将右子节点中的小键移动到前面（变成不正常的右红链接 小的在前）
			h.right = rotateRight(h.right);
			//移动右链接，使两两红链接，将h的左右子节点变成4-节点（类5-节点）
			h = rotateLeft(h);
			//转换，只有左子链接为3-节点
			flipColors(h);
		}
		return h;
	}

	/**
	 * h节点的右子节点是2-节点才能使用该方法
	 * 如果h的左右子节点是2-节点，则将这三个节点转化为4-节点
	 * 如果h的左子节点不是2-节点，则移动元素使右子节点成为3-节点
	 * @param h
	 * @return
	 * @author Navy D
	 * @date 20170823202859
	 */
	private Node<K, V> moveRedRight(Node<K, V> h) {
		//这两种情况都直接转化
		flipColors(h);
		//如果左子节点不是2-节点
		if (isRed(h.left.left)) {
			//右旋转左小上移，右大下移
			h = rotateRight(h);
			//转化颜色，使右子节点为3-节点
			flipColors(h);
		}
		return h;
	}

	/**
	 * 恢复树的平衡
	 * @param h
	 * @return
	 * @author Navy D
	 * @date 20170823204651
	 */
	private Node<K, V> balance(Node<K, V> h) {
		//实在无法理解  这一行代码不是与后面的重复了吗？？？？？？？
//		if (isRed(h.right)) {
//			h = rotateLeft(h);
//		}
		//如果是红色右链接，则左旋转
		if (isRed(h.right) && !isRed(h.left))
			h = rotateLeft(h);
		//如果是连续的两个红色左链接，则右旋转
		if (isRed(h.left) && isRed(h.left.left))
			h = rotateRight(h);
		//如果是左右都为红色连接，则颜色转换
		if (isRed(h.left) && isRed(h.right))
			flipColors(h);
		//更新子节点的值
		h.n = size(h.left) + size(h.right) + 1;
		return h;
	}

	/**
	 * 返回符号表中最小的键
	 * @author Navy D
	 * @date 20171008111118
	 */
	@Override
	public K min() {
		 if (isEmpty())
			 throw new NoSuchElementException("called min() with empty symbol table");
		return min(root).key;
	}

	private Node<K, V> min(Node<K, V> h) {
		if (h.left == null)
			return h;
		return min(h.left);
	}

	/**
	 * 返回符号表中最大的键
	 * @author Navy D
	 * @date 20171008111509
	 */
	@Override
	public K max() {
		if (isEmpty())
			throw new NoSuchElementException("called max() with empty symbol table");
		return max(root).key;
	}

	private Node<K, V> max(Node<K, V> h) {
		if (h.right == null)
			return h;
		return max(h.right);
	}

	/**
	 * 返回符号表中小于等于指定键的最大键
	 * @author Navy D
	 * @date 20171008111601
	 */
	@Override
	public K floor(K key) {
		if (key == null)
			throw new IllegalArgumentException("argument to floor() is null");
		if (isEmpty())
			throw new NoSuchElementException("called floor() with empty symbol table");
		Node<K, V> x = floor(root, key);
		if (x == null)
			return null;
		return x.key;
	}

	/**
	 * 当key小于当前节点，返回左子节点
	 * 当key大于当前节点，如果右子节点不存在，就返回当前节点
	 * 如果右子节点存在，就返回有右子节点
	 * @param x
	 * @param key
	 * @return
	 * @author Navy D
	 * @date 20170824151751
	 */
	private Node<K, V> floor(Node<K, V> x, K key) {
		if (x == null)
			return null;
		int cmp = key.compareTo(x.key);
		if (cmp == 0)
			return x;
		if (cmp < 0)
			return floor(x.left, key);
		//key在右边时，右子树可能存在，或不存在，否则就跟<0一样直接返回了
		Node<K, V> t =  floor(x.right, key);
		if (t != null)
			return t;
		return x;

	}

	/**
	 * 返回符号表中大于等于指定键的最小键
	 *
	 * @author Navy D
	 * @date 20171008111713
	 */
	@Override
	public K ceiling(K key) {
		if (key == null)
			throw new IllegalArgumentException("argument to ceiling() is null");
		if (isEmpty())
			throw new NoSuchElementException("called ceiling() with empty symbol table");
		Node<K, V> x = ceiling(root, key);
		if (x == null)
			return null;
		return x.key;
	}

	/**
	 * 后继：大于等于当前节点的最小节点
	 * 当key大于当前节点，就在返回右子节点
	 * 当key小于当前节点，如果右子节点不存在，则返回当前节点
	 * 如果左子节点存在，且大于key，则返回左子节点
	 * 如果小于key，就返回左子节点的父节点
	 * 当表中存在key，就返回该节点
	 * @param x
	 * @param key
	 * @return
	 * @author Navy D
	 * @date 20170824144941
	 */
	private Node<K, V> ceiling(Node<K, V> x, K key) {
		if (x == null)
			return null;
		int cmp = key.compareTo(x.key);
		if (cmp == 0)
			return x;
		if (cmp > 0)
			return ceiling(x.right, key);
		Node<K, V> t = ceiling(x.left, key);
		//找不到，且x.left>key，就返回左子节点
		if (t != null)
			return t;
		//左子节点为null就返回父节点
		return x;
	}

	/**
	 * 返回符号表中指定排名的键从0开始
	 * @author Navy D
	 * @date 20171008111802
	 */
	@Override
	public K select(int k) {
		if (k < 0 || k >= size())
			 throw new IllegalArgumentException("called select() with invalid argument: " + k);
		return select(root, k).key;

	}
	/**
	 * 红黑二叉查找树的查找算法与二叉查找树一样
	 * 如果k比根节点数t小，则在左子树中查找
	 * 如果k>t，则在右子树中查找排名为k-t-1(以右子节点为新的根节点排名自然为k-t-1)
	 * 如果k==t，则返回当前节点
	 * @param x
	 * @param k
	 * @return
	 * @author Navy D
	 * @date 20170824143922
	 */
	private Node<K, V> select(Node<K, V> x, int k) {
		assert x != null;
		assert k >= 0 && k < size(x) : "k=" + k + " size=" + size();
		int t = size(x.left);
		if (k < t)
			return select(x.left, k);
		else if (k > t)
			return select(x.right, k - t - 1);
		else
			return x;
	}

	/**
	 * 返回符号表中指定的键的排名
	 * @author Navy D
	 * @date 20171008111932
	 */
	@Override
	public int rank(K key) {
		if (key == null)
			throw new IllegalArgumentException();
		return rank(root, key);
	}

	/**
	 * 红黑二叉查找树的查找算法与二叉查找树一样
	 * 如果key小于根节点，则在左子树中查找
	 * 如果key大于根节点，则在右子树中查找1+size(x.left)的节点
	 * 如果等于，就返回其左边节点的数量(从0开始)
	 * @param x
	 * @param key
	 * @return
	 * @author Navy D
	 * @date 20170824144505
	 */
	private int rank(Node<K, V> x, K key) {
		if (x == null)
			return 0;
		int cmp = key.compareTo(x.key);
		//等于
		if (cmp == 0)
			return size(x.left);
		//key在左子树
		else if (cmp < 0)
			return rank(x.left, key);
		//key在右子树，下一个节点前要加上当前节点的位置
		else
			return 1 + size(x.left) + rank(x.right, key);
	}

	@Override
	public Iterable<K> keys() {
		 if (isEmpty())
			 return new Queue<K>();
		return keys(min(), max());
	}

	@Override
	public Iterable<K> keys(K lo, K hi) {
		if (lo == null)
			throw new IllegalArgumentException("first argument to keys() is null");
		if (hi == null)
			throw new IllegalArgumentException("second argument to keys() is null");
		Queue<K> queue = new Queue<>();
		keys(root, queue, lo, hi);
		return queue;
	}

	/**
	 * 如果当前节点在比lo大，就在左子树寻找更小的，直到第一次<=lo
	 * 如果当前节点比hi小，就在右子树寻找更大的，直到>=hi
	 * 如果当前节点x在lo..hi中间，就入栈
	 * @param x
	 * @param queue
	 * @param lo
	 * @param hi
	 * @author Navy D
	 * @date 20170824164730
	 */
	private void keys(Node<K, V> x, Queue<K> queue, K lo, K hi) {
		if (x == null)
			return ;
		int cmpLo = lo.compareTo(x.key);
		int cmpHi = hi.compareTo(x.key);
		//如果lo<x，
		if (cmpLo < 0)
			keys(x.left, queue, lo, hi);
		//从cmpLo==0  x>=lo开始入栈直到x<=hi
		if (cmpLo <= 0 && cmpHi >= 0)
			queue.enqueue(x.key);
		//如果hi>x，还没有到达hi
		if (cmpHi > 0)
			keys(x.right, queue, lo, hi);
	}

	/**
	 * 返回lo..hi之间的元素数量，如果lo，或hi不存在，仍然会返回认为存在的数量
	 *
	 * 使用rank()方法实现
	 * @author Navy D
	 * @date 20170824164240
	 */
	@Override
	public int size(K lo, K hi) {
		if (lo == null || hi == null)
			throw new IllegalArgumentException();
		if (lo.compareTo(hi) > 0)
			return 0;
		//如果包含lo，需要加上lo这个元素
		if (contains(lo))
			return rank(hi) - rank(lo) + 1;
		return rank(hi) - rank(lo);
	}

	public int height() {
		return height(root);
	}

	private int height(Node<K, V> x) {
		if (x == null)
			return -1;
		//取x的左右节点大的
		return size(x.left) < size(x.right) ? height(x.right) + 1 : height(x.left) + 1;
	}


	/***************************************************************************
	 * Check integrity of red-black tree data structure.
	 ***************************************************************************/
	private boolean check() {
		if (!isBST())
			System.out.println("Not in symmetric order");
		if (!isSizeConsistent())
			System.out.println("Subtree counts not consistent");
		if (!isRankConsistent())
			System.out.println("Ranks not consistent");
		if (!is23())
			System.out.println("Not a 2-3 tree");
		if (!isBalanced())
			System.out.println("Not balanced");
		return isBST() && isSizeConsistent() && isRankConsistent() && is23() && isBalanced();
	}

	// does this binary tree satisfy symmetric order?
	// Note: this test also ensures that data structure is a binary tree since
	// order is strict
	private boolean isBST() {
		return isBST(root, null, null);
	}

	/**
	 * 对于一个二叉树，如果左子节点大于等于根节点，如果右子节点小于等于根节点就不是二叉搜索树
	 * @param x
	 * @param min
	 * @param max
	 * @return
	 * @author Navy D
	 * @date 20170829112822
	 */
	// is the tree rooted at x a BST with all keys strictly between min and max
	// (if min or max is null, treat as empty constraint)
	// Credit: Bob Dondero's elegant solution
	private boolean isBST(Node<K, V> x, K min, K max) {
		if (x == null)
			return true;
		//如果当前节点x>=上一个节点
		if (min != null && x.key.compareTo(min) <= 0)
			return false;
		//如果当前节点<= 上一个节点
		if (max != null && x.key.compareTo(max) >= 0)
			return false;
		return isBST(x.left, min, x.key) && isBST(x.right, x.key, max);
	}

	// are the size fields correct?
	private boolean isSizeConsistent() {
		return isSizeConsistent(root);
	}

	private boolean isSizeConsistent(Node<K, V> x) {
		if (x == null)
			return true;
		if (x.n != size(x.left) + size(x.right) + 1)
			return false;
		return isSizeConsistent(x.left) && isSizeConsistent(x.right);
	}

	// check that ranks are consistent
	private boolean isRankConsistent() {
		for (int i = 0; i < size(); i++)
			if (i != rank(select(i)))
				return false;
		for (K key : keys())
			if (key.compareTo(select(rank(key))) != 0)
				return false;
		return true;
	}

	// Does the tree have no red right links, and at most one (left)
	// red links in a row on any path?
	private boolean is23() {
		return is23(root);
	}
	/**
	 * 检查一个节点是否同时存在两条红链接连接的节点和红色右链接
	 * @param x
	 * @return
	 * @author Navy D
	 * @date 20170829103412
	 */
	private boolean is23(Node<K, V> x) {
		if (x == null)
			return true;
		//红色右链接
		if (isRed(x.right))
			return false;
		//连续两条红色链接，注：root本身可能是红色节点(操作完成后一定是黑色节点)
		if (x != root && isRed(x) && isRed(x.left))
			return false;
		return is23(x.left) && is23(x.right);
	}

	/**
	 * 检查从根节点到所有空连接的路径上的黑链接的数量是否相等
	 * @return
	 * @author Navy D
	 * @date 20170829104025
	 */
	private boolean isBalanced() {
		int black = 0; // number of black links on path from root to min
		Node<K, V> x = root;
		//遍历整个左子树得到黑链接的数量
		while (x != null) {
			if (!isRed(x))
				black++;
			x = x.left;
		}
		return isBalanced(root, black);
	}

	/**
	 * 节点x是否是黑链接
	 * @param x
	 * @param black
	 * @return
	 * @author Navy D
	 * @date 20170829104344
	 */
	private boolean isBalanced(Node<K, V> x, int black) {
		//如果遍历完成，判断黑链接数量是否相等
		if (x == null)
			return black == 0;
		//如果节点x是黑色链接就将数量--
		if (!isRed(x))
			black--;
		return isBalanced(x.left, black) && isBalanced(x.right, black);
	}

	public static void main(String[] args) {
		RedBlackBST<String, Integer> st = new RedBlackBST<>();
		In in = new In("../MyAlgs/algs4-data/tinyTale.txt");

		for (int i = 0; !in.isEmpty(); i++) {
			String key = in.readString();
			st.put(key, i);
		}

		st.keys().forEach(i -> System.out.print( i + " "));

		st.delete("age");
		System.out.println();
		st.keys().forEach(i -> System.out.print( i + " "));

		// System.out.println(st.isBalanced());
		//
		// }
	}

}
