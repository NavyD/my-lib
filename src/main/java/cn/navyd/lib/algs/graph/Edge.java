package cn.navyd.lib.algs.graph;

/**
 * 加权无向边的数据类型
 * @author Navy D
 * @date 20170917195757
 */
public class Edge implements Comparable<Edge> {
	// 顶点之一
	private final int v;
	// 另一个顶点
	private final int w;
	// 边的权重
	private final double weight;

	public Edge(int v, int w, double weight) {
		if (v < 0)
			throw new IllegalArgumentException("vertex index must be a nonnegative integer");
		if (w < 0)
			throw new IllegalArgumentException("vertex index must be a nonnegative integer");
		if (Double.isNaN(weight))
			throw new IllegalArgumentException("Weight is NaN");
		this.v = v;
		this.w = w;
		this.weight = weight;
	}

	/**
	 * 返回这个边的权值
	 * @return
	 * @author Navy D
	 * @date 20170917195852
	 */
	public double weight() {
		return weight;
	}

	/**
	 * 返回边两端的顶点之一
	 * @return
	 * @author Navy D
	 * @date 20170909153430
	 */
	public int either() {
		return v;
	}

	/**
	 * 返回另一个顶点
	 * @param vertex
	 * @return
	 * @author Navy D
	 * @date 20170909153445
	 */
	public int other(int vertex) {
		if (vertex == v)
			return w;
		if (vertex == w)
			return v;
		throw new RuntimeException("Illegal endpoint");
	}

	@Override
	public int compareTo(Edge that) {
		return Double.compare(weight(), that.weight());
	}

	public String toString() {
		return String.format("%d-%d %.2f", v, w, weight);
	}
	
	public static void main(String[] args) {
		Edge e = new Edge(12, 34, 4.77);
		System.out.println(e);
		
		
		
	}
}
