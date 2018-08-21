package cn.navyd.lib.algs.graph;

import cn.navyd.lib.algs.util.In;
import cn.navyd.lib.algs.util.Queue;

/**
 * 连通分量：使用深搜找出图中所有的连通分量<br>
 * 提供无向图和加权有向图的连通分量的计算<br>
 * 性能：搜索完整幅图使用的时间和空间与v+e成正比，查询连通性只需常数时间<br>
 * 注意：对应的Union-find算法也能完成连通性的查询，且不用完整的构造一个图<br>
 * 在需要完成大量连通性查询和插入混合任务时使用union-find算法
 * @author Navy D
 * @date 20170902110255
 */
public class CC {
	//当前顶点是否被标记过(最后都被标记，每个顶点都存在所属的连通分量中)
	private boolean[] marked;
	// 顶点所在的连通分量
	private int[] id;
	// 所有的连通分量
	private int count;

	/**
	 * 在无向图中计算所有的连通分量
	 * @param g
	 */
	public CC(Graph g) {
		marked = new boolean[g.getV()];
		id = new int[g.getV()];
		// 遍历所有顶点，如果没有标记，就以该起点深搜标记连通分量
		for (int i = 0; i < g.getV(); i++)
			if (!marked[i]) {
				dfs(g, i);
				//下一个连通分量
				count++;
			}
	}

	/**
	 * 在加权有向图中计算所有连通分量
	 * @param g
	 */
	public CC(EdgeWeightedDigraph g) {
		marked = new boolean[g.getV()];
		id = new int[g.getV()];

		for (int i = 0; i < g.getV(); i++)
			if (!marked[i]) {
				dfs(g, i);
				//下一个连通分量
				count++;
			}
	}

	/**
	 * 深搜给定顶点标记所有连通的顶点，只能搜索到一个连通分量，
	 * 对不在一个连通分量中的顶点没有用
	 * @param g
	 * @param v
	 * @author Navy D
	 * @date 20170902105905
	 */
	private void dfs(Graph g, int v) {
		marked[v] = true;
		// 将当前顶点关联到一个连通分量中
		id[v] = count;
		for (int i : g.adj(v))
			if (!marked[i])
				dfs(g, i);
	}

	private void dfs(EdgeWeightedDigraph g, int v) {
		marked[v] = true;
		id[v] = count;
		for (DirectedEdge e : g.adj(v))
			if (!marked[e.to()])
				dfs(g, e.to());
	}

	/**
	 * 如果顶点v与顶点w是连通的就返回true
	 * @param v
	 * @param w
	 * @return
	 * @author Navy D
	 * @date 20170920195819
	 */
	public boolean connected(int v, int w) {
		validateVertex(v);
		validateVertex(w);
		// 判断两个顶点是否在一个连通分量中，只需判断两个顶点关联的索引是否是一个值
		return id(v) == id(w);
	}

	/**
	 * 返回顶点v所在的连通分量标识
	 * @param v
	 * @return
	 * @author Navy D
	 * @date 20170920195905
	 */
	public int id(int v) {
		validateVertex(v);
		return id[v];
	}

	/**
	 * 返回图中连通分量的数目
	 * @return
	 * @author Navy D
	 * @date 20170920200034
	 */
	public int  count() {
		return count;
	}

	private void validateVertex(int v) {
		if (v < 0 || v >= marked.length)
			throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (marked.length-1));
	}

	public static void main(String[] args) {
		Graph g = new Graph(new In("../Algs_4/tinyCG.txt"));
		CC cc = new CC(g);

		int m = cc.count();
        System.out.println(m + " components");

        // compute list of vertices in each connected component
        @SuppressWarnings("unchecked")
		Queue<Integer>[] components = new Queue[m];
        for (int i = 0; i < m; i++) {
            components[i] = new Queue<Integer>();
        }
        for (int v = 0; v < g.getV(); v++) {
            components[cc.id(v)].enqueue(v);
        }

        // print results
        for (int i = 0; i < m; i++) {
            for (int v : components[i]) {
                System.out.print(v + " ");
            }
            System.out.println();
        }








	}







}
