package cn.navyd.lib.algs.graph;

import cn.navyd.lib.algs.util.In;
import cn.navyd.lib.algs.util.ArrayStack;

/**
 * 图g是否是无环图：<br>
 * 思想：使用深搜寻找环，如果当前顶点的邻接表顶点已经被标记就说明含有环
 * 对于自环和平行边额外处理<br>
 * 注意：<br>
 * 该类允许图含自环和平行边并将其算作包含环<br>
 * 对于有多个环的图，该算法只寻找任意一个环
 * @author Navy D
 * @date 20170903110212
 */
public class Cycle {
	// 标记已经搜索过的节点
	private boolean[] marked;
	// 保存找到环之前的连通路径
	private int[] edgeTo;
	// 一个环
	private ArrayStack<Integer> cycle;

	/**
	 * 在无向图中计算图中是否包含一个环
	 * @param g
	 */
	public Cycle(Graph g) {
		if (hasSelfLoop(g))
			return;
		if (hasParallelEdges(g))
			return;
		marked = new boolean[g.getV()];
		edgeTo = new int[g.getV()];
		// 找所有连通分量
		for (int s = 0; s < g.getV(); s++)
			if (!marked[s])
				dfs(g, s, s);
	}

	/**
	 * 对于环状图来说，最后一个顶点邻接起点，起点是已标记过的，此时只要比较，下一个邻接点与上一个顶点
	 * 是否一致，若一致说明(返回上一个顶点)不是环状图而是一条边连接已经标记的节点，若不一致说明是环状图
	 * @param g
	 * @param v
	 * @param u 上一个顶点，即边的起点
	 * @author Navy D
	 * @date 20170903104425
	 */
	private void dfs(Graph g, int v, int u) {
		marked[v] = true;
		for (int w : g.adj(v)) {
			if (hasCycle())
				return;
			if (!marked[w]) {
				edgeTo[w] = v;
				dfs(g, w, v); // 一旦进入elseif后递归调用会迅速返回完成
			}
			// 如果邻接点w和上一个顶点u不一致就是环状图
			else if (w != u) {
				cycle = new ArrayStack<>();
				for (int x = v; x != w; x = edgeTo[x])
					cycle.push(x);
				cycle.push(w);
				// 加入顶点v形成环    循环
				cycle.push(v);
			}
		}
	}

	/**
	 * 如果图中含有环就返回true
	 * @return
	 * @author Navy D
	 * @date 20170920204324
	 */
	public boolean hasCycle() {
		return cycle != null;
	}

	/**
	 * 返回环的路径。如果不存在就返回null
	 * @return
	 * @author Navy D
	 * @date 20170920204404
	 */
	public Iterable<Integer> cycle() {
		return cycle;
	}

	/**
	 * 如果图含有自环就返回true
	 * @param g
	 * @return
	 * @author Navy D
	 * @date 20170920201815
	 */
	private boolean hasSelfLoop(Graph g) {
		// 遍历所有顶点的邻接表
		for (int v = 0; v < g.getV(); v++)
			for (int w : g.adj(v))
				// 自环
				if (v == w) {
					// 添加自环
					cycle = new ArrayStack<>();
					cycle.push(v);
					cycle.push(v);
					return true;
				}
		return false;
	}

	/**
	 * 如果图含有平行边就返回true
	 * @param g
	 * @return
	 * @author Navy D
	 * @date 20170920202513
	 */
	private boolean hasParallelEdges(Graph g) {
		// 标记邻接表中的元素，如果再次遇到已经标记的元素，说明含有平行边
		marked = new boolean[g.getV()];
		for (int v = 0; v < g.getV(); v++) {
			for (int w : g.adj(v)) {
				if (marked[w]) {
					cycle = new ArrayStack<>();
					cycle.push(v);
					cycle.push(w);
					cycle.push(v);
					return true;
				}
				marked[w] = true;
			}
			// 恢复邻接表中元素标记，如果没有恢复，就会导致该元素在其他邻接表被认为是环，其实只是一个边
			for (int w : g.adj(v))
				marked[w] = false;
		}
		marked = null;
		return false;
	}

	public static void main(String[] args) {
        Graph G = new Graph(new In("../Algs_4/tinyCG.txt"));
        System.out.println(G);
        Cycle finder = new Cycle(G);
        if (finder.hasCycle()) {
            for (int v : finder.cycle()) {
                System.out.print(v + " ");
            }
            System.out.println();
        }
        else {
            System.out.println("Graph is acyclic");
        }
	}


}
