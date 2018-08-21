package cn.navyd.lib.algs.graph;

import cn.navyd.lib.algs.util.In;
import cn.navyd.lib.algs.util.ArrayStack;

/**
 * 无权有向图是否含有环：<br>
 * 思想：
 * 对于环的存在，深搜在栈中维护一个有向路径,一旦在栈中找到一个边的终点w也在栈中时，就表示找到一个环<br>
 * 该类只寻找一个有向环，对于含多个环的图是能找到任意的一个环
 * @author Navy D
 * @date 20170906204011
 */
public class DirectedCycle {
	// 将搜索过的顶点标记
	private boolean[] marked;
	// 保存每个顶点所属的路径索引
	private int[] edgeTo;
	// 保存环的路径
	private ArrayStack<Integer> cycle;
	// 标记递归栈上的顶点
	private boolean[] onStack;

	/**
	 * 确定一个有向图是否含有环，如果是，则找到其中一个环
	 * @param g
	 */
	public DirectedCycle(Digraph g) {
		marked = new boolean[g.getV()];
		edgeTo = new int[g.getV()];
		onStack = new boolean[g.getV()];
		for (int i = 0; i < g.getV(); i++)
			if (!marked[i])
				dfs(g, i);

		assert check();
	}

	/**
	 * 深搜寻找有向环：
	 * 相对标准的深搜，添加了一个onStack数组表示某个顶点是否在栈中
	 * 只要深搜时当前顶点的下一个顶点在栈中，表示这是一个有向图
	 * @param g
	 * @param v
	 * @author Navy D
	 * @date 20170906204725
	 */
	private void dfs(Digraph g, int v) {
		marked[v] = true;
		// 将当前栈中顶点标记
		onStack[v] = true;
		for (int w : g.adj(v)) {
			// 当v有多个出度时，已经找到环时直接返回
			if (hasCycle())
				return ;
			// 递归搜索
			if (!marked[w]) {
				edgeTo[w] = v;
				dfs(g, w);
			}
			// 如果当前顶点的下一个邻接点已经在栈中
			else if (onStack[w]) {
				cycle = new ArrayStack<>();
				// 使用栈保存环路径
				for (int x = v; x != w; x = edgeTo[x])
					cycle.push(x);
				// 保存起点
				cycle.push(w);
				// 保存当前顶点，是使为环：v->w->x->v
				cycle.push(v);
			}
		}
		// 顶点完成遍历后退出栈
		onStack[v] = false;
	}

	/**
	 * 如果图含有有向环就返回true
	 * @return
	 * @author Navy D
	 * @date 20170906205112
	 */
	public boolean hasCycle() {
		return cycle != null;
	}

	/**
	 * 如果存在有向环就遍历所有顶点，否则返回null
	 * @return
	 * @author Navy D
	 * @date 20170906205017
	 */
	public Iterable<Integer> cycle() {
		return cycle;
	}

	/**
	 * 验证一个环是否合理:
	 * 1.开始顶点与最后顶点是否同一个值
	 * @return
	 * @author Navy D
	 * @date 20170916223107
	 */
	private boolean check() {
		if (hasCycle()) {
			// 环的开始顶点
			int first = -1,
					// 环的最后顶点
					last = -1;
			for (int v : cycle()) {
				if (first == -1)
					first = v;
				// 更新最后的顶点
				last = v;
			}
			// 如果开始顶点和最后顶点不是一个就不是一个环
			if (first != last) {
				System.err.printf("cycle begins with %d and ends with %d\n", first, last);
				return false;
			}
		}

		return true;
	}

	public static void main(String[] args) {
		In in = new In("./tinyDG.txt");
		Digraph g = new Digraph(in);
		DirectedCycle finder = new DirectedCycle(g);
		if (finder.hasCycle()) {
			for (int v : finder.cycle())
				System.out.print(v + " ");
			System.out.println();
		} else
			System.out.println("no directed cycle");


	}

}
