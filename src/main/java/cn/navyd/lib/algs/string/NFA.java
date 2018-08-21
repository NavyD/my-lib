package cn.navyd.lib.algs.string;

import cn.navyd.lib.algs.graph.Digraph;
import cn.navyd.lib.algs.graph.DirectedDFS;
import cn.navyd.lib.algs.util.BagV;
import cn.navyd.lib.algs.util.In;
import cn.navyd.lib.algs.util.ArrayStack;

/**
 * NFA非确定有限状态自动机：
 * <p>
 * 思想：将正则表达式构造为一个NFA。需要读取到所有文本并达到接受状态
 * 关键在于匹配转换和3-转换。将3-转换变为有向图。运行时匹配状态
 * <p>复杂度：长度为m的正则表达式和长度n的文本最坏mn成正比
 * 构造时时间空间与m成正比
 * @author Navy D
 * @date 20171011145757
 */
public class NFA {
	// 正则表达式的char数组也表示了(存在字母表)匹配转换
	private char[] re;
	// 有向图表示所有的∈-转换如：0表示的(存在∈-转换到1，即0->1其中表示re[0],re[1]的关系
	private Digraph g;
	// 正则表达式的长度
	private int m;

	/**
	 * 初始化一个指定正则表达式的NFA
	 * @param regexp
	 */
	public NFA(String regexp) {
		re = regexp.toCharArray();
		m = re.length;
		g = new Digraph(m+1);
		init();
	}

	/**
	 * 构造正则表达式对应的NFA的∈-转换的图
	 * @author Navy D
	 * @date 20171011120802
	 */
	private void init() {
		ArrayStack<Integer> ops = new ArrayStack<>();
		for (int i = 0; i < m; i++) {
			int lp = i;
			// 将所有左括号和 | 压入栈
			if (re[i] == '(' || re[i] == '|')
				ops.push(i);
			// 遇到右括号就弹出左括号或|
			else if (re[i] == ')') {
				int or = ops.pop();
				if (re[or] == '|') {
					// 对应的左括号
					lp = ops.pop();
					// 添加(->B一条非匹配转换 如(c|d*)  (->d  |->)
					g.addEdge(lp, or + 1);
					// 添加|->)的转换
					g.addEdge(or, i);
				} else// 是左括号
					lp = or;
			}
			// 对应的字符后一位使*就添加这个元素i->i+1,i+1->i两条转换，如果是()*这样，就使用左括号连接
			if (i < m - 1 && re[i + 1] == '*') {
				g.addEdge(lp, i + 1);
				g.addEdge(i + 1, lp);
			}
			// 通常的状态转换
			if (re[i] == '(' || re[i] == '*' || re[i] == ')')
				g.addEdge(i, i + 1);
		}

	}

	/**
	 * 如果指定的文本能被NFA识别就返回true
	 * 模拟NFA的运行。对应当前txt的字符匹配后取得状态集合，再使用这个集合
	 * 取得能通过3-转换到达的所有集合，最后如果这个集合中包含接受状态就匹配通过
	 * @param txt
	 * @return
	 * @author Navy D
	 * @date 20171011145012
	 */
	public boolean recognizes(String txt) {
		BagV<Integer> pc = new BagV<>();
		DirectedDFS dfs = new DirectedDFS(g, 0);
		// 初始化取得起点0由∈-转换能到达的所有顶点状态
		for (int v = 0; v < g.getV(); v++)
			if (dfs.marked(v))
				pc.add(v);
		// 匹配当前字符(不是状态)后能够到达所有状态的集合(匹配装换，不是3-装换)
		BagV<Integer> match = null;
		// 计算txt可能达到的所有NFA状态
		for (int i = 0; i < txt.length(); i++) {
			match = new BagV<>();
			// 保存匹配txt[i]字符后能到达的状态
			for (int v : pc)
				if (v < m)
					if (re[v] == txt.charAt(i) || re[v] == '.')
						// 保存正则匹配后一个状态如a对应状态2和6，匹配后保存3,7
						match.add(v + 1);
			// 保存匹配后的状态集合通过3-装换能到达的集合 如匹配a后的3,7状态能通过∈-转换到达2347状态
			pc = new BagV<>();
			// 多点状态可达
			dfs = new DirectedDFS(g, match);
			for (int v = 0; v < g.getV(); v++)
				if (dfs.marked(v))
					pc.add(v);
		}
		// 如果匹配的状态集合中包含接受状态(含最后的顶点)表示成功匹配
		for (int v : pc)
			if (v == m)
				return true;
		return false;
	}

	public static void main(String[] args) {
		String s = "(A*B|AC)D";
		String regexp = "(.*" + s + ".*)";
		NFA nfa = new NFA(regexp);

		String txt1 = "ABCCBD";
		System.out.println(nfa.recognizes(txt1));


		In in = new In("./src/priv/cn/navyd/algs_4/NFA.java");
		while (!in.isEmpty()) {
			String txt = in.readLine();
			if (nfa.recognizes(txt))
				System.out.println(txt);
		}

	}

}
