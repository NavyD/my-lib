package cn.navyd.lib.algs.graph;

import cn.navyd.lib.algs.sort.MinPQ;
import cn.navyd.lib.algs.util.In;
import cn.navyd.lib.algs.util.Queue;
import cn.navyd.lib.algs.util.QuickUF;
import cn.navyd.lib.algs.util.UF;

/**
 * 加权无向图kruskal算法：<br>
 * 思想：按照图中所有边的权重顺序加入生成树，并使加入的边不会形成环<br>
 * 复杂度：空间与e成正比，时间与eloge成正比
 * @author Navy D
 * @date 20170910124352
 */
public class KruskalMST implements MinSpanTree {
	// 最小生成树的边
	private Queue<Edge> mst;
	// 最小生成树的总权重
	private double weight;

	public KruskalMST(EdgeWeightedGraph g) {
		mst = new Queue<>();
		//　将所有边加入最小队列
		MinPQ<Edge> pq = new MinPQ<>();
		for (Edge e : g.edges())
			pq.insert(e);
		// 使用路径加权的uion-find算法
		UF uf = new QuickUF(g.getV());

		weight = 0.0;
		//生成树的边数=顶点数-1
		int edges = g.getV()-1;
		while (!pq.isEmpty() && mst.size() < edges) {
			Edge e = pq.delMin();
			int v = e.either(), w = e.other(v);
			// 如果两个顶点已经连接了，跳过当前最小边避免生成环
			if (uf.connected(v, w))
				continue;
			// 连接两个顶点
			uf.union(v, w);
			mst.enqueue(e);
			weight += e.weight();
		}

		assert check(g);
	}

	/**
	 * 返回最小生成树的路径
	 * @return
	 * @author Navy D
	 * @date 20170917213044
	 */
	public Iterable<Edge> edges() {
		return mst;
	}

	/**
	 * 返回最小生成树的权值总和
	 * @return
	 * @author Navy D
	 * @date 20170917213541
	 */
	public double weight() {
		return weight;
	}

	/**
	 * 主要检查条件：
	 * 1.生成树是否含环
	 * 2.生成树的边是否在图中横切边中最小
	 * @param G
	 * @return
	 * @author Navy D
	 * @date 20170917211650
	 */
	private boolean check(EdgeWeightedGraph g) {

		// 检查最小生成树的延时版和即时版的权重是否一致
		double total = 0.0;
        for (Edge e : edges()) {
            total += e.weight();
        }
        if (Math.abs(total - weight()) > 1E-12) {
            System.err.printf("Weight of edges does not equal weight(): %f vs. %f\n", total, weight());
            return false;
        }

        // 检查最小生成树是否含有环
        UF uf = new QuickUF(g.getV());
        for (Edge e : edges()) {
            int v = e.either(), w = e.other(v);
            // 如果将生成树的顶点从0开始连接边，发现在顶点连接之前就已经存在一条边，表示存在环
            if (uf.connected(v, w)) {
                System.err.println("Not a forest");
                return false;
            }
            // 连接顶点
            uf.union(v, w);
        }

        // 检查是否是一个森林
        for (Edge e : g.edges()) {
            int v = e.either(), w = e.other(v);
            // 如果生成树未连接原图所有顶点即有一个顶点不在生成树中，生成树错误
            if (!uf.connected(v, w)) {
                System.err.println("Not a spanning forest");
                return false;
            }
        }

        // 给定的生成树删除任意边在原图中正好是顶点横切边最小的边
        for (Edge e : edges()) {

        	// 构建一个删除边e的最小生成树
			uf = new QuickUF(g.getV());
			for (Edge f : edges()) {
				int x = f.either(), y = f.other(x);
				// 不连接e
				if (f != e)
					uf.union(x, y);
			}

			// 在原图中检查顶点横切边生成树删除的边是否最小
			for (Edge f : g.edges()) {
				int x = f.either(), y = f.other(x);
				// 找到横切边顶点
				if (!uf.connected(x, y)) {
					// 如果横切边不是最小
					if (f.weight() < e.weight()) {
						System.err.println("Edge " + f + " violates cut optimality conditions");
						return false;
					}
				}
			}

        }
        return true;
    }

	public static void main(String[] args) {
		EdgeWeightedGraph g = new EdgeWeightedGraph(new In("../Algs_4/tinyEWG.txt"));
		KruskalMST mst = new KruskalMST(g);
		for (Edge e : mst.edges())
			System.out.println(e );
		System.out.println(mst.weight());






	}

}
