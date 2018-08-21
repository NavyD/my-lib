package cn.navyd.lib.algs.tree;

import java.util.NoSuchElementException;

import cn.navyd.lib.algs.util.In;
import cn.navyd.lib.algs.util.Queue;
import cn.navyd.lib.algs.util.ArrayStack;

/**
 * <p>二叉查找树：</p>
 * <p>思想：二叉树，其中左子树都小于父节点。右子树大于父节点。使用静态链表实现
 * <p>时间复杂度：N个随机键的二叉查找树查找平均2lnN~1.39lgN
 * 插入需要2lnN次比较
 * <p> 最坏情况；当N个键有序(顺逆)插入时，树的高度是N-1，查找插入需要线性时间
 * 所有操作在最坏情况下所需时间与树的高度成正比
 * @author Navy D
 * @date 20170822135859
 * @param <K>
 * @param <V>
 */
public class BST<K extends Comparable<K>, V> implements ST<K, V> {
	// 二叉树的根节点
	private Node<K, V> root;

	private static class Node<K, V> {
		private K key;
		private V val;
		//左右子树
		private Node<K, V> left, right;
		//以当前节点为根的子树结点总数
		private int n;

		public Node(K key, V val, int n) {
			this.key = key;
			this.val = val;
			this.n = n;
		}
	}

	/**
	 * 初始化一个空的符号表
	 */
	public BST() {

	}

	/**
	 * 返回符号表中键值对的数量
	 * @author Navy D
	 * @date 20171005174446
	 */
	public int size() {
		return size(root);
	}

	/**
	 * 返回一节点x为根的子树节点的数量
	 * @param x
	 * @return
	 * @author Navy D
	 * @date 20170819160155
	 */
	private int size(Node<K, V> x) {
		return x != null ? x.n : 0;
	}
	/**
	 * 如果符号表为空就返回true
	 * @author Navy D
	 * @date 20171005174846
	 */
	public boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * 如果符号表中存在指定的key就返回true
	 * @author Navy D
	 * @date 20171005175152
	 */
	public boolean contains(K key) {
		if (key == null)
			throw new IllegalArgumentException("argument to contains() is null");
		return get(key) != null;
	}

	/**
	 * 返回指定的key关联的值。如果不存在这个key就返回null
	 * @author Navy D
	 * @date 20171005175246
	 */
	public V get(K key) {
		if (key == null)
			throw new IllegalArgumentException("called get() with a null key");
		if (isEmpty())
			throw new NoSuchElementException();
		Node<K, V> result = get(root, key);
		return result != null ? result.val : null;
	}

	/**
	 * 查找的方式：
	 * 1.如果key与当前节点的键相等 返回这个值
	 * 2.如果key比当前节点的键小，就查找左子树
	 * 3.如果key与当前节点的键大，就查找右子树
	 * @author Navy D
	 * @date 20171005175433
	 */
	private Node<K, V> get(Node<K, V> x, K key) {
		if (x == null)
			return null;
		//比较key与当前node的关系
		int cmp = key.compareTo(x.key);
		//key在node左边
		if (cmp < 0)
			return get(x.left, key);
		//key在node右边
		else if (cmp > 0)
			return get(x.right, key);
		//找到key与node相等
		else
			return x;
	}

	/**
	 * 在符号表中插入键值对。如果符号表中包含这个key，新的val将覆盖旧val。
	 * 如果val=null将在符号表中删除这个键值对
	 * @author Navy D
	 * @date 20171005175808
	 */
	public void put(K key, V val) {
		if (key == null)
			  throw new IllegalArgumentException("called put() with a null key");
		if (val == null) {
            delete(key);
            return;
        }
		root = put(root, key, val);

		assert check();
	}

	/**
	 * 插入情况：
	 * 1.当节点存在与key相等时，覆盖节点的val
	 * 2.当树中不存在与key相等的节点时，顺着查找的顺序(找到null)在节点上添加一个新节点
	 * @author Navy D
	 * @date 20171005180019
	 */
	private Node<K, V> put(Node<K, V> x, K key, V val) {
		//当某个子树根节点的子节点匹配为null时将节点添加到树中
		if (x == null)
			return new Node<K, V>(key, val, 1);
		//查找key是否存在x的子树中，存在则更新，否则插入新节点
		int cmp = key.compareTo(x.key);
		if (cmp < 0)
			//由于栈保存的x是当前的上一个node，所以 上一个.left = 左子或新节点
			x.left = put(x.left, key, val);
		else if (cmp > 0)
			x.right = put(x.right, key, val);
		else
			x.val = val;
		//如果插入新的节点，size()会返回1，否则只是原来的数量，注意这是将本身节点算在内
		x.n = size(x.left) + size(x.right) + 1;
		return x;
	}

	/**
	 * 移除符号表中最小的键和关联的值
	 * @author Navy D
	 * @date 20171005180910
	 */
	public void deleteMin() {
		  if (isEmpty())
			  throw new NoSuchElementException("Symbol table underflow");
		root = deleteMin(root);

		assert check();
	}

	/**
	 * 删除最小键：能找到最小的左子节点说明这个节点一定没有左子节点
	 * 一个节点的左子节点最小，删除左子节点后使用被删除节点的右子节点链接为该节点的新左子节点
	 *
	 * 删除左子树最后一个节点x，将该节点的右子树赋给上一个节点指向自己的左子树 连接
	 * 这是根据二叉查找树的有序性质得到的：子树的子树的左子节点大于子树根节点
	 * @param x
	 * @return
	 * @author Navy D
	 * @date 20170820140232
	 */
	private Node<K, V> deleteMin(Node<K, V> x) {
		//判断x的左节点为空，则返回x的右节点给 上一个x.left
		//注意：这里不能使用x==null，这样上一个x就是最后一个节点，达不到删除的效果
		if (x.left == null)
			return x.right;
		//用x.right的替代x.left达到删除效果
		x.left = deleteMin(x.left);
		//删除后重新计算x的节点数量
		x.n = size(x.left) + size(x.right) + 1;
		return x;
	}

	/**
	 * 移除符号表中最大的键和关联的值
	 * @author Navy D
	 * @date 20171005181740
	 */
	public void deleteMax() {
		if (isEmpty())
			throw new NoSuchElementException("Symbol table underflow");
		root = deleteMax(root);

		assert check();
	}

	/**
	 * 删除最大键：能找到最大的右子节点说明这个节点一定没有右子节点
	 * 一个节点的右子节点最大，删除右子节点后使用被删除节点的左子节点链接为该节点的新右子节点
	 *
	 * 删除右子树最后一个节点x，并将x.left赋给上一个x.right
	 * @param x
	 * @return
	 * @author Navy D
	 * @date 20170820141339
	 */
	private Node<K, V> deleteMax(Node<K, V> x) {
		//判断最后一个右节点x，是则返回x的左子节点
		if (x.right == null)
			return x.left;
		//将返回的左子节点覆盖上一个节点的右连接，删除
		x.right = deleteMax(x.right);
		//计算大小
		x.n = size(x.left) + size(x.right) + 1;
		return x;
	}

	/**
	 * 移除符号表中指定的键和关联的值。如果存在就删除
	 * @author Navy D
	 * @date 20171005182424
	 */
	public void delete(K key) {
		if (key == null)
			throw new IllegalArgumentException("called delete() with a null key");
		root = delete(root, key);

		assert check();
	}

	/**
	 * 删除任意节点情况：
	 * 1.删除节点只含有其中一个子节点：
	 * 与删除最大最小键一样，将指向删除节点的链接改为链接其子节点就行了
	 * 2.含两个子节点：
	 * 将指向删除节点的链接改为其右子节点中最小的或左子节点中最大的代替删除节点
	 * @author Navy D
	 * @date 20171005182531
	 */
	private Node<K, V> delete(Node<K, V> x, K key) {
		//如果不存在key，x.left = null
		if (x == null)
			return null;
		int cmp = key.compareTo(x.key);
		//如果key在左子树，delete返回将x的链接转到新的x上
		if (cmp < 0)
			x.left = delete(x.left, key);
		//如果key在右子树
		else if (cmp > 0)
			x.right = delete(x.right, key);
		//如果key找到了
		else {
			//这两个判断表示该节点要么在子树中最大或最小，可直接替换就有序
			//如果要删除的节点右子树是空的就直接返回其左子树与x连接
			if (x.right == null)
				return x.left;
			//如果左子树是空的，使用右子树连接x
			if (x.left == null)
				return x.right;

			Node<K, V> t = x;
			//将删除节点x替换为右子树最小的节点
			x = min(t.right);
			//将新节点的链接更新，右子树需要删除替换上来的小节点
			x.right = deleteMin(t.right);
			x.left = t.left;
		}
		//计算删除后的节点大小，本来x应该是1，换到某个点后可能很大
		x.n = size(x.left) + size(x.right) + 1;
		//最后返回替换后的节点并与上一个节点连接
		return x;

	}

	/**
	 * 返回符号表中最小的键
	 * @author Navy D
	 * @date 20171005183552
	 */
	public K min() {
		if (isEmpty())
			throw new NoSuchElementException("called min() with empty symbol table");
		return min(root).key;
	}

	/**
	 * 返回节点x为根的树中最小的节点
	 * @param x
	 * @return
	 * @author Navy D
	 * @date 20171005183624
	 */
	private Node<K, V> min(Node<K, V> x) {
		if (x.left == null)
			return x;
		return min(x.left);
	}

	/**
	 * 返回符号表中最大的键
	 * @author Navy D
	 * @date 20171005183758
	 */
	public K max() {
		if (isEmpty())
			throw new NoSuchElementException("called max() with empty symbol table");
		return max(root).key;
	}

	/**
	 * 返回以节点x为根的树中最大的节点
	 * @param x
	 * @return
	 * @author Navy D
	 * @date 20171005183827
	 */
	private Node<K, V> max(Node<K, V> x) {
		if (x.right == null)
			return x;
		return max(x.right);
	}

	/**
	 * 返回符号表小于等于指定键的最大键。如果不存在这样的键就返回null
	 * @author Navy D
	 * @date 20171005183926
	 */
	public K floor(K key) {
		if (key == null)
			throw new IllegalArgumentException("argument to floor() is null");
		if (isEmpty())
			throw new NoSuchElementException("called floor() with empty symbol table");
		Node<K, V> x = floor(root, key);
		return x != null ? x.key : null;
	}

	/**
	 * 向上取整情况：对于指定的键取小于或等于的键
	 * 1.如果找到相等的节点就返回这个节点
	 * 2.如果键小于当前节点就取左子树
	 * 3.如果键大于当前节点，如果当前节点不存在右子树，那么当前节点就是第一个小于这个key的节点，返回
	 * 	如果当前节点存在右子树，就在右子树中寻找比当前节点更大的节点(仍然小于key)
	 *
	 * 如果key小于根节点的键，则一定在左子树存在<=key的键
	 * 如果key大于根节点的键，则当右子树中存在<=key的键时才在右子树中找到，
	 * 否则根节点就是小于等于key节键
	 * @param x
	 * @param key
	 * @return
	 * @author Navy D
	 * @date 20170819182858
	 */
	private Node<K, V> floor(Node<K, V> x, K key) {
		if (x == null)
			return null;
		int cmp = key.compareTo(x.key);
		//如果key就是根x
		if (cmp == 0)
			return x;
		//如果小于根x的键一定还在左边
		else if (cmp < 0)
			return floor(x.left, key);
		/**
		 * 分为两种情况：
		 * key在右边中间:当floor(x.left)返回null时(在某一个中间，没找到)，回到node t = null
		 * 然后返回x.right>key,x.left=null的x，x刚好比key小
		 * key比右边最后还大：最后floor(x.right)返回null，还是返回最后一个的前一个
		 */
		// 当key在根右边时，可能返回node(相等)或null(找不到)
		Node<K, V> t = floor(x.right, key);
		// 找到一个node
		if (t != null)
			return t;
		// 找不到
		// 返回的是x==null的上一个方法调用时的x
		return x;
	}

	/**
	 * 返回符号表中大于等于指定键的最小键。如果不存在就返回null
	 * @author Navy D
	 * @date 20171005195436
	 */
	public K ceiling(K key) {
		if (key == null)
			throw new IllegalArgumentException("argument to ceiling() is null");
		if (isEmpty())
			throw new NoSuchElementException("called ceiling() with empty symbol table");
		Node<K, V> x = ceiling(root, key);
		return x != null ? x.key : null;
	}

	/**
	 * 返回符号表大于等于key的最小的node
	 * 与floor方法相反
	 * @param x
	 * @param key
	 * @return
	 * @author Navy D
	 * @date 20170819203011
	 */
	private Node<K, V> ceiling(Node<K, V> x, K key) {
		if (x == null)
			return null;
		int cmp = key.compareTo(x.key);
		if (cmp == 0)
			return x;
		else if (cmp > 0)
			return ceiling(x.right, key);
		// 如果key小于当前节点，就在左子树中继续寻找是否有必当前节点小但大于等于key的键
		Node<K, V> t = ceiling(x.left, key);
		if (t != null)
			return t;
		return x;
	}

	/**
	 * 返回在符号表中排名为k的键   默认0最小
	 * @author Navy D
	 * @date 20171005200954
	 */
	public K select(int k) {
		if (k < 0 || k >= size())
			 throw new IllegalArgumentException("called select() with invalid argument: " + k);
		return select(root, k).key;
	}

	/**
	 * 二叉查找树中每一个根节点的子树数量就是在有序数组中的下标(只有一个时例外)
	 * 如果左子树的节点数k大于排名k，则在左子树中递归查找k键
	 * 如果右子树中的节点数小于排名k，则在右子树中查找k-t-1的键
	 * 如果相等，则返回根节点
	 * 0<=k<size();
	 * @param x
	 * @param k
	 * @return
	 * @author Navy D
	 * @date 20170819210655
	 */
	private Node<K, V> select(Node<K, V> x, int k) {
		if (x == null)
			return null;
		//取左子树的节点数
		int t = size(x.left);
		//排名k在左子树边
		if (t > k)
			return select(x.left, k);
		//排名k在右子树边
		else if (t < k)
			//将二叉查找树看做是以根节点为切点的两个数组，取第二个数组时，下标相对变化(从0开始)
			//t为左边数组长度，1为根节点，k-t-1是右边子树的长度
			return select(x.right, k - t -1);
		//如果排名k等于左子树的数量(数组从0开始，这样表示根节点)
		return x;
	}

	/**
	 * 返回指定键在符号表中的排名    默认0为最小递增。如果键不存在，就返回这个键插入后应该在的位置
	 * 如age位置0，ag不在表中，就返回0
	 * @author Navy D
	 * @date 20171005201726
	 */
	public int rank(K key) {
		 if (key == null)
			 throw new IllegalArgumentException("argument to rank() is null");
		return rank(root, key);
	}

	/**
	 * 还是将树看做是两个数组
	 * 如果key与根节点的键相等，直接返回左子树的排名(数组中为根节点的下标)
	 * 如果key小于根节点的键，则在左子树中递归
	 * 如果key大于根节点的键，则在右子树+根节点下标+1递归
	 *
	 * 注意：如果key不存在符号表中，任然会返回有序时key的位置
	 * @param x
	 * @param key
	 * @return
	 * @author Navy D
	 * @date 20170819214014
	 */
	private int rank(Node<K, V> x, K key) {
		if (x == null)
			return 0;
		int cmp = key.compareTo(x.key);
		//如果key在左子树中，直接返回左子树中的排名
		if (cmp < 0)
			return rank(x.left, key);
		//如果key在右子树中，返回t+1(根节点)+在右子树上的排名
		else if (cmp > 0)
			return size(x.left) + 1 + rank(x.right, key);
		//如果key与根键相等，直接返回左子树的排名
		else
			return size(x.left);
	}

	/**
	 * 返回符号表中指定键范围的键的数量。如果lo比hi排名大就报异常
	 * @author Navy D
	 * @date 20171005203032
	 */
	public int size(K lo, K hi) {
		if (lo == null)
			throw new IllegalArgumentException("first argument to size() is null");
		if (hi == null)
			throw new IllegalArgumentException("second argument to size() is null");
		if (lo.compareTo(hi) > 0)
			throw new IllegalArgumentException();
		// 不管lo是否在表中，rank(lo)值在就在这个位置上，而hi不在表中会扩大一个位置
		// 如果hi刚好在表中，需要加上hi这个元素
		if (contains(hi))
			return rank(hi) - rank(lo) + 1;
		// 如果hi不在
		return rank(hi) - rank(lo);
	}

	/**
	 * 返回符号表中所有键的iterable对象
	 * @author Navy D
	 * @date 20171005202743
	 */
	public Iterable<K> keys() {
		return keys(min(), max());
	}

	/**
	 * 返回符号表中指键的范围的iterable对象
	 * @author Navy D
	 * @date 20171005202812
	 */
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
	 * 中序遍历：
	 * 对于一个二叉查找树，指定一个范围key并查找出来
	 * 只要这个树左子树中有一个lo=key开始向上向右直到hi=key就完成遍历
	 * @param x
	 * @param queue
	 * @param lo
	 * @param hi
	 * @author Navy D
	 * @date 20170820161507
	 */
	private void keys(Node<K, V> x, Queue<K> queue, K lo, K hi) {
		if (x == null)
			return;
		//x.key是否在lo..hi范围内
		int cmpLo = lo.compareTo(x.key);
		int cmpHi = hi.compareTo(x.key);
		//如果x.key未达到lo的位置，就在左子树找下一个更小的
		if (cmpLo < 0)
			keys(x.left, queue, lo, hi);
		//x.key从lo开始慢慢往上一直到hi
		if (cmpLo <= 0 && cmpHi >= 0)
			queue.enqueue(x.key);
		//如果x.key未达到hi的位置，取x的右子树找符合lo..hi范围的元素，因为以x为根的子树左子树任然存在lo..hi的元素
		if (cmpHi > 0)
			keys(x.right, queue, lo, hi);
	}

	/**
	 * 返回树的高度
	 * @return
	 * @author Navy D
	 * @date 20171005203306
	 */
	public int height() {
		return height(root);
	}

	/**
	 * 查询树的高度：从底向上
	 * 如果左子树的高度低于右子树的高度，则使用右子树递归，否则使用左子树递归
	 * 高度从1开始
	 * @param x
	 * @return
	 * @author Navy D
	 * @date 20170820165757
	 */
	private int height(Node<K, V> x) {
		if (x == null)
			return 0;
		//如果左子树高度小于右子树
		if (size(x.left) < size(x.right))
			return 1 + height(x.right);
		else
			return 1 + height(x.left);
	}

	public Iterable<K> levelOrder() {
		Queue<K> keys = new Queue<>();
		Queue<Node<K, V>> queue = new Queue<>();
		queue.enqueue(root);
		while (!queue.isEmpty()) {
			Node<K, V> x = queue.dequeue();
			if (x == null)
				continue;
			keys.enqueue(x.key);
			queue.enqueue(x.left);
			queue.enqueue(x.right);
		}
		return keys;
	}



	/*************************	验证	************************************/

	private boolean check() {
		if (!isBST())
			System.out.println("Not in symmetric order");
		if (!isSizeConsistent())
			System.out.println("Subtree counts not consistent");
		if (!isRankConsistent())
			System.out.println("Ranks not consistent");
		return isBST() && isSizeConsistent() && isRankConsistent();
	}

	private boolean isBST() {
		return isBST(root, null, null);
	}

	/**
	 * 验证左	右子节点是否比父节点小 	 大。即节点有序
	 * @date 20171005205526
	 */
	private boolean isBST(Node<K, V> x, K min, K max) {
		if (x == null)
			return true;
		// 如果右子节点小于等于父节点
		if (min != null && x.key.compareTo(min) <= 0)
			return false;
		// 如果左子节点大于等于父节点
		if (max != null && x.key.compareTo(max) >= 0)
			return false;
		return isBST(x.left, min, x.key) && isBST(x.right, x.key, max);
	}

	private boolean isSizeConsistent() {
		return isSizeConsistent(root);
	}

	/**
	 * 验证父节点的节点数量是否是左右子节点数量的和
	 * @date 20171005205657
	 */
	private boolean isSizeConsistent(Node<K, V> x) {
		if (x == null)
			return true;
		if (x.n != size(x.left) + size(x.right) + 1)
			return false;
		return isSizeConsistent(x.left) && isSizeConsistent(x.right);
	}

	/**
	 * 判断所有键的排名方式是否正确
	 * @date 20171005205807
	 */
	private boolean isRankConsistent() {
		for (int i = 0; i < size(); i++)
			if (i != rank(select(i)))
				return false;
		for (K key : keys())
			if (key.compareTo(select(rank(key))) != 0)
				return false;
		return true;
	}

	// 中序遍历非递归
	private void inTraversalNon() {
		ArrayStack<Node<K, V>> stack = new ArrayStack<>();
		Node<K, V> cur = root;
		while (cur != null || !stack.isEmpty()) {
			// 一直找到树的最左边
			if (cur != null) {
				// 栈在遍历第一个完成时存储所有的左节点
				stack.push(cur);
				cur = cur.left;
			}
			// 左子树为null取右子树
			else {
				cur = stack.pop();
				System.out.print(cur.key + " ");
				cur = cur.right;
			}
		}
		System.out.println();
	}



	public static void main(String[] args) {
		BST<String, Integer> st = new BST<>();

		In in = new In("../MyAlgs/algs4-data/tinyTale.txt");

		for (int i = 0; !in.isEmpty(); i++) {
			String key = in.readString();
			st.put(key, i);
		}

		for (String s : st.levelOrder())
			System.out.print(s + " ");

		System.out.println();

		for (int i = 0; i < st.size(); i++)
			System.out.print(st.select(i) + " ");
		System.out.println();
//		for (String s : st.keys())
//			System.out.println(s + " " + st.get(s));

		System.out.println();

		System.out.println("non:");
		st.inTraversalNon();

//		System.out.println("rank:");
//
//		System.out.println(st.select(0));
//		System.out.println(st.rank("age"));
//
//		System.out.println();
//		System.out.println("delete:min");
//
//		st.deleteMin();
//		for (String s : st.keys())
//			System.out.println(s + " " + st.get(s));



	}

}
