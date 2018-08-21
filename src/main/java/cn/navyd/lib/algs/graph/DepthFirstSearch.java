package cn.navyd.lib.algs.graph;

import cn.navyd.lib.algs.util.In;

/**
 * 深度优先搜索算法：<br>
 * 性能：连通所有顶点的时间与顶点的度数之和成正比<br>
 * 注意：深搜每条边访问两次，即每个顶点需要访问两次，第二次访问时已经被标记
 * 深搜的路径是度数的两倍
 * @author Navy D
 * @date 20170901165720
 */
public class DepthFirstSearch {
    // 标记已经搜索过的顶点
    private boolean[] marked;
    // 以s为起点的连通分量的元素个数
    private int count;

    /**
     * 在图中计算起点s连通的顶点
     * @param g
     * @param s
     */
    public DepthFirstSearch(Graph g, int s) {
        marked = new boolean[g.getV()];
        validateVertex(s);
        dfs(g, s);
    }
    /**
     * 过程：
     * 1.从图中顶点s出发，先访问s
     * 2.选择一个与s相邻且没访问过的节点w，再从w出发递归，直到所有与s邻接的顶点都访问过为止
     * 由于bag链表采用后进先出的方式，若一个顶点邻接多个顶点，已经访问过该顶点后，下一个邻接点
     * 必然不会再访问上一个邻接点，除非不在有邻接点，返回
     * @param g
     * @param v
     * @author Navy D
     * @date 20170901165752
     */
    private void dfs(Graph g, int v) {
        //访问标记v
        marked[v] = true;
        //s与v是连通的++
        count++;
        //以v邻接的节点为起点，递归标记
        for (int w : g.adj(v))
            if (!marked[w])
                dfs(g, w);
    }

    /**
     * 如果存在起点s到顶点v的一条路径就返回true
     * @param v
     * @return
     * @author Navy D
     * @date 20170920180317
     */
    public boolean marked(int v) {
        validateVertex(v);
        return marked[v];
    }

    /**
     * 返回起点s连通顶点的数量
     * @return
     * @author Navy D
     * @date 20170920180416
     */
    public int count() {
        return count;
    }

    private void validateVertex(int v) {
        if (v < 0 || v >= marked.length)
            throw new IllegalArgumentException("vertex " + v + " is not between 0 and " + (marked.length-1));
    }

    public static void main(String[] args) {
        Graph g = new Graph(new In("../Algs_4/tinyCG.txt"));
        for (int s = 0; s < g.getV(); s++) {
            DepthFirstSearch search = new DepthFirstSearch(g, s);
             for (int v = 0; v < g.getV(); v++) {
                 if (search.marked(v))
                    System.out.print(v + " ");

            }
            System.out.println();
            if (search.count() != g.getV())
                System.out.println("NOT connected");
            else
                System.out.println("connected");
        }

    }

}
