package cn.navyd.lib.algs.graph;

/**
 * 加权有向边的数据结构
 * @author Navy D
 * @date 20170916152249
 */
public class DirectedEdge {
	// 边的起点
	private final int v;
	// 边的终点
	private final int w;
	// 边的权重
	private final double weight;

	/**
	 * 构造一个起点v，终点w权重为weight的边
	 * @param v
	 * @param w
	 * @param weight
	 */
	public DirectedEdge(int v, int w, double weight) {
		if (v < 0)
			throw new IllegalArgumentException("Vertex names must be nonnegative integers");
		if (w < 0)
			throw new IllegalArgumentException("Vertex names must be nonnegative integers");
		if (Double.isNaN(weight))
			throw new IllegalArgumentException("Weight is NaN");
		this.v = v;
		this.w = w;
		this.weight = weight;
	}

	/**
	 * 返回边的权重
	 * @return
	 * @author Navy D
	 * @date 20170916152354
	 */
	public double weight() {
		return weight;
	}

	/**
	 * 返回边的起点
	 * @return
	 * @author Navy D
	 * @date 20170916152406
	 */
	public int from() {
		return v;
	}

	/**
	 * 返回边的终点
	 * @return
	 * @author Navy D
	 * @date 20170916152417
	 */
	public int to() {
		return w;
	}

	/**
	 * 返回边的字符串输出
	 * @author Navy D
	 * @date 20170916153431
	 */
	public String toString() {
		return String.format("%d->%d %.2f", v, w, weight);
	}

	public static void main(String[] args) {
		DirectedEdge e = new DirectedEdge(12, 34, 5.67);
        System.out.println(e);


	}
}
