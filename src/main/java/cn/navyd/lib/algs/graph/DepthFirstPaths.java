package cn.navyd.lib.algs.graph;

import cn.navyd.lib.algs.util.In;
import cn.navyd.lib.algs.util.ArrayStack;

/**
 * 深度优先搜索路径：寻找从定点s到某个定点v的一条路径<br>
 * 性能：深搜从起点到任意标记顶点的路径所需时间与路径长度成正比
 * @author Navy D
 * @date 20170901174110
 */
public class DepthFirstPaths {
	//节点是否已被搜索标记
	private boolean[] marked;
	// 从起点开始能连通的路径记录 如：edgeTo[v] = w ,edgeTo[w] = x, edgeTo[y] = z直到找到起点s
	private int[] edgeTo;
	// 起点
	private final int s;

	/**
	 * 在图中计算起点s到其他顶点的一条路径
	 * @param g
	 * @param s
	 */
	public DepthFirstPaths(Graph g, int s) {
		marked = new boolean[g.getV()];
		edgeTo = new int[g.getV()];
		validateVertex(s);
		this.s = s;
		dfs(g, s);
	}

	/**
	 * 深搜记录路径
	 * @param g
	 * @param v
	 * @author Navy D
	 * @date 20170920181030
	 */
	private void dfs(Graph g, int v) {
		marked[v] = true;
		for (int w : g.adj(v))
			if (!marked[w]) {
				//将每个v能连通的顶点w在数组中的值置为v，通过读取w得到v：w-v-x-y
				edgeTo[w] = v;
				dfs(g, w);
			}
	}

	/**
	 * 如果存在起点s到顶点v的路径就返回true
	 * @param v
	 * @return
	 * @author Navy D
	 * @date 20170920181141
	 */
	public boolean hasPathTo(int v) {
		validateVertex(s);
		return marked[v];
	}

	/**
	 * 返回起点s到顶点v的路径。如果不存在这条路径，就返回null
	 * @param v
	 * @return
	 * @author Navy D
	 * @date 20170920181207
	 */
	public Iterable<Integer> pathTo(int v) {
		validateVertex(s);
		if (!hasPathTo(v))
			return null;
		ArrayStack<Integer> path = new ArrayStack<>();
		for (int x = v; x != s; x = edgeTo[x])
			path.push(x);
		path.push(s); // 遍历从s开始指向后面
		return path;
	}

	private void validateVertex(int v) {
		if (v < 0 || v >= marked.length)
			throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (marked.length-1));
	}

	public static void main(String[] args) {
		Graph g = new Graph(new In("../Algs_4/tinyCG.txt"));
		int s = 1;
		DepthFirstPaths dfs = new DepthFirstPaths(g, s);

		for (int v = 0; v < g.getV(); v++) {
			if (dfs.hasPathTo(v)) {
				System.out.printf("%d to %d:  ", s, v);
				for (int x : dfs.pathTo(v)) {
					if (x == s)
						System.out.print(x);
					else
						System.out.print("-" + x);
				}
				System.out.println();
			}

			else {
				System.out.printf("%d to %d:  not connected\n", s, v);
			}

		}

	}
}
