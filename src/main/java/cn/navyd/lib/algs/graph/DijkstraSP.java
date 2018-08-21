package cn.navyd.lib.algs.graph;

import cn.navyd.lib.algs.sort.IndexMinPQ;
import cn.navyd.lib.algs.util.In;
import cn.navyd.lib.algs.util.ArrayStack;

/**
 * Dijkstrasp算法：解决边权重非负的加权有向图的单起点最短路径问题<br>
 * 思想：
 * 类似prim算法，将顶点分为树中与树外两个集合，顶点一旦加入树中，最小边不会再变化<br>
 * 顶点v如果在树外，那么树中顶点会仅保持一条与v最小的连接。
 * 而一个顶点w加入树中时，此时树与顶点v就保持了两条连接，需要比较从起点s开始的距离
 * 是s-v还是新的s-w-v的距离<br>
 * 复杂度：计算所需空间与v成正比，时间与elogv成正比
 * @author Navy D
 * @date 20170911204237
 */
public class DijkstraSP {
	// 最短路径树中的边 如edgeTo[v]表示顶点s到v的最短路径上的一条边
	private DirectedEdge[] edgeTo;
	// 到达起点的最短距离 如distTo[v] 表示顶点s到v的最短距离
	private double[] distTo;
	// 索引优先队列保存树连接树外的边顶点的最小权值
	private IndexMinPQ<Double> pq;

	/**
	 * 在一个加权有向图中计算从起点s开始到任何的顶点的最短路径
	 * @param g
	 * @param s
	 */
	public DijkstraSP(EdgeWeightedDigraph g, int s) {
		// 判断图是否存在负权值的边
		for (DirectedEdge e : g.edges()) {
            if (e.weight() < 0)
                throw new IllegalArgumentException("edge " + e + " has negative weight");
        }

		edgeTo = new DirectedEdge[g.getV()];
		distTo = new double[g.getV()];

		validateVertex(s);

		pq = new IndexMinPQ<>(g.getV());
		// 将所有顶点距离权值初始化为正无限大
		for (int i = 0; i < g.getV(); i++)
			distTo[i] = Double.POSITIVE_INFINITY;
		// 顶点0初始化
		distTo[s] = 0.0;
		pq.insert(s, 0.0);
		// 不断放松树外顶点
		while (!pq.isEmpty()) {
			// 将优先队列中最小边的顶点加入树中（删除顶点）
			relax(g, pq.delMin());
		}

		assert check(g, s);
	}

	/**
	 * 放松树中顶点v指向的边，更新树到v的所有顶点的最小边权值
	 * @param g
	 * @param v
	 * @author Navy D
	 * @date 20170911203849
	 */
	private void relax(EdgeWeightedDigraph g, int v) {
//		System.out.println(v + "   " + distTo[v]);
		// 将刚加入树的顶点v的所有有向边的权值比较
		for (DirectedEdge e : g.adj(v)) {
			// 树外的顶点
			int w = e.to();
//			System.out.println(distTo[w] + " " + distTo[v]);
			// 如果刚加入顶点v新连接到w的边权重更小就更新
			if (distTo[w] > distTo[v] + e.weight()) {
				// 更新w的距离 更小
				distTo[w] = e.weight() + distTo[v];
				// 更新到w的边
				edgeTo[w] = e;
				// 将w加入即将加入树中的队列，w的权值仍然可能变化，一旦加入树中（删除队列）就不会在变化
				if (pq.contains(w))
					pq.changeKey(w, distTo[w]);
				else
					pq.insert(w, distTo[w]);
//				System.out.println("change edge:" + e);
			}
		}
	}

	/**
	 * 返回起点s到顶点v的最短距离
	 * @param v
	 * @return
	 * @author Navy D
	 * @date 20170916163540
	 */
	public double distTo(int v) {
		validateVertex(v);
		return distTo[v];
	}

	/**
	 * 如果存在起点s到顶点v的最短路径就返回true
	 * @param v
	 * @return
	 * @author Navy D
	 * @date 20170916163704
	 */
	public boolean hasPathTo(int v) {
		validateVertex(v);
		return distTo[v] < Double.POSITIVE_INFINITY;
	}

	/**
	 * 返回起点s到顶点v的最短路径，如果不存在就返回null
	 * @param v
	 * @return
	 * @author Navy D
	 * @date 20170916164104
	 */
	public Iterable<DirectedEdge> pathTo(int v) {
		validateVertex(v);
		if (!hasPathTo(v))
			return null;
		ArrayStack<DirectedEdge> path = new ArrayStack<>();
		for (DirectedEdge e = edgeTo[v]; e != null; e = edgeTo[e.from()])
			path.push(e);
		return path;
	}

	/**
	 * 主要检查两种情况：
	 * 1.对于所有顶点是否满足最优条件distTo[e.to()] <= distTo[e.from()] + e.weight()
	 * 2.对于所有最短路径中的边是否满足distTo[e.to()] == distTo[e.from()] + e.weight()
	 * @param G
	 * @param s
	 * @return
	 * @author Navy D
	 * @date 20170916181440
	 */
	private boolean check(EdgeWeightedDigraph G, int s) {
		// 检查图g的边是否含有负权值
		for (DirectedEdge e : G.edges()) {
			if (e.weight() < 0) {
				System.err.println("negative edge weight detected");
				return false;
			}
		}

		// 检查起点s初始化后是否正常，在放松后是否更改过起点
		if (distTo[s] != 0.0 || edgeTo[s] != null) {
			System.err.println("distTo[s] and edgeTo[s] inconsistent");
			return false;
		}

		// 检查所有没有在起点s可达的最短路径外的顶点初始化后是否被更改
		for (int v = 0; v < G.getV(); v++) {
			if (v == s)
				continue;
			// 判断edgeTo与distTo是否一致
			if (edgeTo[v] == null && distTo[v] != Double.POSITIVE_INFINITY) {
				System.err.println("distTo[] and edgeTo[] inconsistent");
				return false;
			}
		}

		// 检查图中所有顶点是否满足了最优条件e = v->w distTo[w] <= distTo[v] + e.weight()
		// 注意：此条件对于起点s不可达顶点也适用，即d[9]=infinity == d[8]=infinity两个无穷数java中表示相等
		for (int v = 0; v < G.getV(); v++) {
			for (DirectedEdge e : G.adj(v)) {
				int w = e.to();
//				System.out.println(v + " " + w);
//				System.out.println(distTo[v] + " " + distTo[w] + (distTo[v] + e.weight() < distTo[w]));
				// 如果某条边不满足最优条件，若v与w值为无限，条件为false
				if (distTo[v] + e.weight() < distTo[w]) {
					System.err.println("edge " + e + " not relaxed");
					return false;
				}
			}
		}

		// 检查最短路径中是否满足e = v->w distTo[w] == distTo[v] + e.weight()
		for (int w = 0; w < G.getV(); w++) {
			if (edgeTo[w] == null)
				continue;
			DirectedEdge e = edgeTo[w];
			int v = e.from();
			if (w != e.to())
				return false;
			// 表示 distTo[v] + e.weight() != distTo[w]在1的-12次方精度内就相等
			if (Math.abs(distTo[w] - (distTo[v] + e.weight()) ) > 1E-12) {
//			if (distTo[v] + e.weight() != distTo[w]) {
				System.err.println("edge " + e + " on shortest path not tight");
				return false;
			}
		}
		return true;
	}

	private void validateVertex(int... args) {
		for (int i : args)
			if (i < 0 || i >= edgeTo.length)
				throw new IllegalArgumentException("vertex " + i + " is not between 0 and " + (edgeTo.length-1));
	}

	public static void main(String[] args) {
		In in = new In("./tinyEWD.txt");
		EdgeWeightedDigraph g = new EdgeWeightedDigraph(in);
		int s = 0;
		DijkstraSP sp = new DijkstraSP(g, s);
		 for (int t = 0; t < g.getV(); t++) {
	            if (sp.hasPathTo(t)) {
	                System.out.printf("%d to %d (%.2f)  ", s, t, sp.distTo(t));
	                for (DirectedEdge e : sp.pathTo(t)) {
	                    System.out.print(e + "   ");
	                }
	                System.out.println();
	            }
	            else {
	                System.out.printf("%d to %d         no path\n", s, t);
	            }
	        }
	}



}
