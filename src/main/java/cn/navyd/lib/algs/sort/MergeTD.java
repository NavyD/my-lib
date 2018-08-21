package cn.navyd.lib.algs.sort;

import java.util.Arrays;
import java.util.Random;


/**
 * <p>
 * 自顶向下的归并排序MergeTopDown：
 * <p>
 * 分治思想：将一个数组分为两个有序数组再将结果归并起来
 * <p>
 * 实现： 将一个数组用mid分为两个数组，在merge时依次比较两个数组的元素的大小并将小的放入原数组中
 * 在merge前先递归将数组变为有序（1个元素的数组为有序），之后merge就都是有序的<br>
 * 虽然说是自顶向下，但是，实际归并数组时只能是从小到大，即先递归在归并
 * </p>
 * 优化：
 * <li>对小数组使用插入排序
 * <li>测试数组是否已经有序，有序就不用归并
 * <p>
 * 时间复杂度：1/2NlgN--NlgN次比较，最多6NlgN次访问数组 1/2是当归并时刚好左边或右边全部小于或大于另一边，不用比较另一边的数组
 * 空间复杂度：N
 * </p>
 * 稳定性： 是
 *
 * @author Navy D
 * @date 20170802173125
 */
public class MergeTD {
	private static final int CUTOFF = 7;

	private MergeTD() {

	}

	/**
	 * 对数组a进行归并排序，使用自顶向下的排序方法
	 * @param a
	 * @author Navy D
	 * @date 20170802181613
	 */
	public static <T extends Comparable<? super T>> void sort(T[] a) {
		if (a == null)
			throw new IllegalArgumentException("argument array is null");
		@SuppressWarnings("unchecked")
		T[] aux = (T[]) new Comparable[a.length];
		sort(a, aux, 0, a.length-1);

		assert isSorted(a);
	}

	/**
	 * 自顶向下的归并排序：
	 * 利用递归将数组从大分为一个个小数组，然后小数组开始排序，并一个个完成直到大数组
	 * @param a 排序原数组
	 * @param aux 辅助数组
	 * @param lo 数组开始位置
	 * @param hi 数组终点位置
	 * @author Navy D
	 * @date 20170802175416
	 */
	private static <T extends Comparable<? super T>> void sort(T[] a, T[] aux, int lo, int hi) {
		//小数组为cutoff长度时直接插入排序
		if (hi <= lo + CUTOFF) {
			insertion(a, lo, hi);
			return;
		}
		//对数组取中点分为a[0-mid],a[mid+1-hi];
		int mid = lo + (hi - lo)/2;
		/*
		 * 注意：
		 * 1.[4,0,2,16,5,17]6个元素排序，必定是[4,0] [4,0,2]   [16,5] [16,5,17] 最后是[4,0,2] [16,5,17]归并
		 * 2.[4,0,2,16,5,17,1]7个，必定是：[4,0] [2,16] [4,0,2,16]    [5,17][5,17,1] 最后两个数组归并
		 * 即将数组用mid不断细分直至从两个只有一个元素的数组开始归并，（数组长度可不等）左边归并为一个大数组之后，开始右边
		 */
		sort(a, aux, lo, mid);
		sort(a, aux, mid+1, hi);

		//当数组无序就归并（由于两数组分别有序，放入merge()必须满足）
//		if (less(a[mid + 1], a[mid])) {
//			for (int i = lo; i <= hi; i++) {
//				if (i == mid)
//					System.out.print(" mid=");
//				System.out.print(a[i] + " ");
//
//			}
//			System.out.println();
//		}
		if (!less(a[mid], a[mid + 1]))
			merge(a, aux, lo, mid, hi);


	}

	/**
	 * 原地归并的抽象方法：避免了在每次归并中创建新数组的开销
	 *
	 * 通过一开始对整个数组复制为辅助数组aux，在归并中从aux中复制到a原数组中;
	 * 方法是：对两个有序数组(通过对一个数组a取中作为两个，即必须保证从中间开始两边为增序,
	 * 大小可以不一样)进行排序， 判断左<右,取左边元素，并取下一个左边元素与右边元素比较，
	 * 左>右，取右边元素并使下一个右边元素与左边比较； 考虑当某一边元素取完时，直接取另一边元素
	 *
	 * @param a 原数组
	 * @param aux 辅助数组
	 * @param lo原数组开始位置
	 * @param mid 原数组中点下标，分为两个有序数组
	 * @param hi 原数组终点位置
	 * @author Navy D
	 * @date 20170802173245
	 */
	private static <T extends Comparable<? super T>> void merge(T[] a, T[] aux, int lo, int mid, int hi) {
		// 归并操作必须保证两个小数组有序
		assert isSorted(a, lo, mid);
		assert isSorted(a, mid+1, hi);

		//左边数组第一个，右边数组第一个
		int i = lo, j = mid+1;

//		for (int k = lo; k <= hi; k++)
//			aux[k] = a[k];

		// 复制数组的值到辅助数组
		System.arraycopy(a, lo, aux, lo, hi-lo+1);

		//将aux数组看做两个排序
		for (int k = lo; k <= hi; k++) {
			//左边数组用尽，取右边元素
			if (i > mid)
				a[k] = aux[j++];
			//反之
			else if (j > hi)
				a[k] = aux[i++];
			//右边元素小，将右边元素取出，并下一个右边元素与左边比较
			else if (less(aux[j], aux[i]))
				a[k] = aux[j++];
			//反之
			else
				a[k] = aux[i++];
		}

		assert isSorted(a, lo, hi);
	}

	// 插入排序
	private static <T extends Comparable<? super T>> void insertion(T[] a, int lo, int hi) {
		for (int i = lo; i <= hi; i++) {
			T v = a[i];
			int j = i;
			for (; j != lo && less(v, a[j - 1]); j--)
				a[j] = a[j-1];
			a[j] = v;
		}
	}

	/**
	 * 如果v较小，返回true，否则返回false
	 * @param v
	 * @param w
	 * @return
	 * @author Navy D
	 * @date 20170731194135
	 */
	private static <T extends Comparable<? super T>> boolean less(T v, T w) {
		return v.compareTo(w) < 0;
	}

	/**
	 * 使用数组a的信息排序并返回一个a有序的下标数组
	 * @param a
	 * @return
	 * @author Navy D
	 * @date 20171001204729
	 */
	public static <T extends Comparable<? super T>> int[] indexSort(T[] a) {
		int n = a.length;
		int[] index = new int[n];
		for (int i = 0; i < n; i++)
			index[i] = i;
		int[] aux = new int[n];
		indexSort(a, aux, index, 0, n - 1);

		return index;
	}

	private static <T extends Comparable<? super T>> void indexSort(T[] a, int[] aux, int[] index, int lo, int hi) {
		if (hi <= lo)
			return;
		int mid = lo + (hi - lo) / 2;
		indexSort(a, aux, index, lo, mid);
		indexSort(a, aux, index, mid + 1, hi);

		merge(a, aux, index, lo, mid, hi);
	}

	/**
	 * 使用辅助数组aux保存index的排序信息，并在aux添加index中新的数组来排序保存到index中
	 * @author Navy D
	 * @date 20171001204405
	 */
	private static <T extends Comparable<? super T>> void merge(T[] a, int[] aux, int[] index, int lo, int mid, int hi) {

		System.arraycopy(index, lo, aux, lo, hi - lo + 1);

//		 for (int k = lo; k <= hi; k++) {
//	            aux[k] = index[k];
//	        }

		int i = lo, j = mid+1;
		for (int k = lo; k <= hi; k++) {
			if (i > mid)
				index[k] = aux[j++];
			else if (j > hi)
				index[k] = aux[i++];
			// 使用数组a大小来排序index
			else if (less(a[aux[j]], a[aux[i]]))
				index[k] = aux[j++];
			else
				index[k] = aux[i++];
		}
	}




	/*************************	验证	*****************************************/

	private static <T extends Comparable<? super T>> boolean isSorted(T[] a) {
		return isSorted(a, 0, a.length);
	}

	private static <T extends Comparable<? super T>> boolean isSorted(T[] a, int lo, int hi) {
		for (int i = lo+1; i < hi; i++)
			if (less(a[i], a[i-1]))
				return false;
		return true;
	}

	public static void main(String[] args) {
		int n = 10;
		Random rand = new Random(47);
		Integer[] a = new Integer[n];
		for (int i = 0; i < a.length; i++) {
			a[i] = rand.nextInt(100);
		}
		System.out.println(Arrays.toString(a));
		sort(a);

		System.out.println(Arrays.toString(a));


//		int[] rs = indexSort(a);
//		for (int i : rs) {
//			System.out.format("%d ", i);
//		}
//		System.out.println();
//		for (int i : rs) {
//			System.out.format("%d ", a[i]);
//		}

	}

}
