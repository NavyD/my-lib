package cn.navyd.lib.algs.graph;

import cn.navyd.lib.algs.util.In;
import cn.navyd.lib.algs.util.Queue;
import cn.navyd.lib.algs.util.ArrayStack;

/**
 * 广度优先搜索最短路径：在图中寻找起点s到某一顶点最短的路径(起点s到顶点v经过的边最少)<br>
 * 性能：最坏情况下与 顶点数v+度数e 之和 成正比<br>
 * 若图是连通的，就是所有顶点度数之和2e
 * @author Navy D
 * @date 20170901201247
 */
public class BreadthFirstPaths {
	// 标记广搜过的节点，到达该顶点的最短路径已知吗
	private boolean[] marked;
	// 最短路径记录
	private int[] edgeTo;
	// 起点s
	private final int s;

	/**
	 * 在图中计算起点s到其他顶点的最短路径
	 * @param g
	 * @param s
	 */
	public BreadthFirstPaths(Graph g, int s) {
		marked = new boolean[g.getV()];
		edgeTo = new int[g.getV()];
		validateVertex(s);
		this.s = s;
		bfs(g, s);

		assert check(g, s);
	}
	/**
	 * 广度优先搜索过程：
	 * 1.访问起点s后出队，然后将其所有的邻接点入队
	 * 2.访问其中一个邻接点后出队，将其所有的邻接点入队
	 * 3.按照v1.v2...vn的次序访问每个顶点的邻接点，
	 * 直到与起点s相通的节点访问完成
	 * @param g
	 * @param s
	 * @author Navy D
	 * @date 20170901200155
	 */
	private void bfs(Graph g, int s) {
		Queue<Integer> queue = new Queue<>();
		//将起点入队
		queue.enqueue(s);
		//标记起点s
		marked[s] = true;
		while (!queue.isEmpty()) {
			//将邻接点出队
			int v = queue.dequeue();
			// 遍历v的邻接点
			for (int w : g.adj(v))
				if (!marked[w]) {
					marked[w] = true;
					edgeTo[w] = v;
					// 将邻接点入队
					queue.enqueue(w);
				}
		}
	}

	/**
	 * 如果存在起点s到顶点v的最短路径就返回true
	 * @param v
	 * @return
	 * @author Navy D
	 * @date 20170920181932
	 */
	public boolean hasPathTo(int v) {
		validateVertex(v);
		return marked[v];
	}

	/**
	 * 返回起点s到顶点v的最短路径。如果不存在这样的路径就返回null
	 * @param v
	 * @return
	 * @author Navy D
	 * @date 20170920182052
	 */
	public Iterable<Integer> pathTo(int v) {
		validateVertex(v);
		if (!hasPathTo(v))
			return null;
		ArrayStack<Integer> path = new ArrayStack<>();
		for (int x = v; x != s; x = edgeTo[x])
			path.push(x);
		path.push(s);
		return path;
	}

	/**
	 * 返回起点s到顶点v的边数。如果不存在就返回int的最大值2^31-1
	 * @param v
	 * @return
	 * @author Navy D
	 * @date 20170920184525
	 */
	public int distTo(int v) {
		validateVertex(v);
		if (!hasPathTo(v))
			return Integer.MAX_VALUE;
		int count = 0;
		for (int x = v; x != s; x = edgeTo[x])
			count++;
		// 边应该比路径顶点少一个
		return count;
	}

	private boolean check(Graph g, int s) {

		// 检查起点s自身的边数是否为0(不支持自环)
		if (distTo(s) != 0) {
			System.out.println("distance of source " + s + " to itself = " + distTo(s));
			return false;
		}

		// check that for each edge v-w dist[w] <= dist[v] + 1
		// provided v is reachable from s
		// 检查每条边 v-w 是否满足dist[w] <= dist[v] + 1
		for (int v = 0; v < g.getV(); v++) {
			for (int w : g.adj(v)) {
				// 如果同一条边两个顶点是否有路径产生有两个不同的结果时报错
				if (hasPathTo(v) != hasPathTo(w)) {
					System.out.println("edge " + v + "-" + w);
					System.out.println("hasPathTo(" + v + ") = " + hasPathTo(v));
					System.out.println("hasPathTo(" + w + ") = " + hasPathTo(w));
					return false;
				}
				// 如果不满足dist[w] <= dist[v] + 1
				if (hasPathTo(v) && (distTo(w) > distTo(v) + 1)) {
					System.out.println("edge " + v + "-" + w);
					System.out.println("distTo[" + v + "] = " + distTo(v));
					System.out.println("distTo[" + w + "] = " + distTo(w));
					return false;
				}
			}
		}

		// check that v = edgeTo[w] satisfies distTo(w) = distTo(v) + 1
		// provided v is reachable from s
		// 检查一条路径中一条边的两个顶点是否满足边数差1的关系
		for (int w = 0; w < g.getV(); w++) {
			if (!hasPathTo(w) || w == s)
				continue;
			int v = edgeTo[w];
			if (distTo(w) != distTo(v) + 1) {
				System.out.println("shortest path edge " + v + "-" + w);
				System.out.println("distTo[" + v + "] = " + distTo(v));
				System.out.println("distTo[" + w + "] = " + distTo(w));
				return false;
			}
		}

		return true;
	}

	private void validateVertex(int v) {
		if (v < 0 || v >= marked.length)
			throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (marked.length-1));
	}

	public static void main(String[] args) {

		Graph g = new Graph(new In("../Algs_4/tinyCG.txt"));
		int s = 1;
		BreadthFirstPaths bfs = new BreadthFirstPaths(g, s);

		for (int v = 0; v < g.getV(); v++) {
			System.out.println("dist=" + bfs.distTo(v));
			if (bfs.hasPathTo(v)) {
				System.out.printf("%d to %d (%d):  ", s, v, bfs.distTo(v));
				for (int x : bfs.pathTo(v)) {
					if (x == s)
						System.out.print(x);
					else
						System.out.print("-" + x);
				}
				System.out.println();
			}

			else {
				System.out.printf("%d to %d (-):  not connected\n", s, v);
			}

		}

	}
}
