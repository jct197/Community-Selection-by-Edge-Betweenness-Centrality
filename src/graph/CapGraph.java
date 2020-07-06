/**
 * 
 */
package graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Stack;

/**
 * @author John C. Thomas.
 */
public class CapGraph implements graph {
	private ArrayList<Emap> edges;
	private ArrayList<Vmap> vertices;
	private HashSet<Vmap> Vlayout;
	private HashMap<Emap,Integer> Elayout;
	private int numVertices;
	private int numEdges;

	public CapGraph(){
		Vlayout = new HashSet<>();
		Elayout = new HashMap<>();
		edges = new ArrayList<Emap>();
		vertices = new ArrayList<Vmap>();
		numVertices = 0;
		numEdges = 0;
	}

	public class Vmap {
		private int vertex;
        public Vmap(int vertex){
			this.vertex = vertex;
		}
        public int getVertex(){
			return this.vertex;
		}
	}

	public class Emap {
		private int startNode;
		private int endNode;

		public Emap(int startNode, int endNode){
			this.startNode = startNode;
			this.endNode = endNode;
		}

	    public int getStartNode(){
			return this.startNode;
		}
		public int getEndNode(){
			return this.endNode;
		}
	}

	@Override
	public void addVertex(int num) {
		if (!Vlayout.contains(num)){
			Vmap out = new Vmap(num);
			vertices.add(out);
			Vlayout.add(out);
			numVertices++;
		}
		else {
			return;
		}
	}

	@Override
	public void addEdge(int from, int to) {
		if (!Elayout.containsKey(from) || (!Elayout.containsKey(to))){
			Emap out = new Emap(from,to);
            numEdges++;
			Elayout.put(out,numEdges);
			edges.add(out);
		}
	}

	public int getNumVertices(){
		return numVertices;
	}
	public int getNumEdges(){
		return numEdges;
	}

	public ArrayList<Vmap> getVertices(){
	    return vertices;
    }
    public ArrayList<Emap> getEdges() {
        return edges;
    }

	public void getAllEdges() {
        System.out.print("[");
        for (int i=0;i<numEdges-1;i++){
            System.out.print(edges.get(i).getStartNode() + " " + edges.get(i).getEndNode() + ", ");
        }
        System.out.print(edges.get(numEdges-1).getStartNode() + " " + edges.get(numEdges-1).getEndNode());
        System.out.print("]");
        System.out.println();
    }

    public HashSet<Integer> getNodes() {
	    HashSet<Integer> out = new HashSet<>();
        for (int i = 0;i<numVertices;i++){
            if (!out.contains(vertices.get(i).getVertex())){
                out.add(vertices.get(i).getVertex());
            }
        }
        return out;
    }

    //get neighbors for all nodes
    public HashMap<Integer, HashSet<Integer>> getNeighbors() {
        HashMap<Integer, HashSet<Integer>> out = new HashMap<>();
        for (int i = 0;i<numEdges;i++){
            if (!out.containsKey(edges.get(i).getStartNode())){
                HashSet<Integer> out1 = new HashSet<>();
                out1.add(edges.get(i).getEndNode());
                out.put(edges.get(i).getStartNode(),out1);
            }
            if (out.containsKey(edges.get(i).getStartNode())){
                HashSet<Integer> temp = out.get(edges.get(i).getStartNode());
                temp.add(edges.get(i).getEndNode());
                out.put(edges.get(i).getStartNode(),temp);
            }
        }
        return out;
    }

	@Override
	public HashMap<Integer, HashSet<Integer>> exportGraph() {
        HashMap<Integer, HashSet<Integer>> out = new HashMap<>();
        for (Vmap j : getVertices()){
            HashSet<Integer> temp2 = new HashSet<>();
            for (int i = 0;i<numEdges;i++){
                if (!out.containsKey(edges.get(i).getStartNode())){
                    HashSet<Integer> out1 = new HashSet<>();
                    out1.add(edges.get(i).getEndNode());
                    out.put(edges.get(i).getStartNode(),out1);
                }
                if (out.containsKey(edges.get(i).getStartNode())){
                    HashSet<Integer> temp = out.get(edges.get(i).getStartNode());
                    temp.add(edges.get(i).getEndNode());
                    out.put(edges.get(i).getStartNode(),temp);
                }
            }
        }
        return out;
	}

	//print out all neighbors
    public void printAllNeighbors() {
        HashMap<Integer, HashSet<Integer>> out = new HashMap<>();
        for (int i = 0;i<numEdges;i++){
            if (!out.containsKey(edges.get(i).getStartNode())){
                HashSet<Integer> out1 = new HashSet<>();
                out1.add(edges.get(i).getEndNode());
                out.put(edges.get(i).getStartNode(),out1);
            }
            if (out.containsKey(edges.get(i).getStartNode())){
                HashSet<Integer> temp = out.get(edges.get(i).getStartNode());
                temp.add(edges.get(i).getEndNode());
                out.put(edges.get(i).getStartNode(),temp);
            }
        }
        for (int i: out.keySet()){
            System.out.print("[");
            System.out.print(i + ": ");
            Integer[] temp = out.get(i).toArray(new Integer[out.get(i).size()]);
            for (int j = 0;j<temp.length-1;j++){
                System.out.print(temp[j]+", ");
            }
            System.out.print(temp[temp.length-1]);
            System.out.print("]");
            System.out.println();
        }
    }

    //breadth-first search
    public HashMap<Integer, List<Integer>> bfs(HashMap<Integer, HashSet<Integer>> next, HashSet<Integer> vnode) {
        HashMap<Integer, HashSet<Integer>> neighbors = next;
        HashSet<Integer> vertex = vnode;
        HashMap<Integer, List<Integer>> rootmap = new HashMap<>();
        for (int i : vertex){
            List<Integer> out = new ArrayList<>();
            bfsvisit(i,neighbors,out,vertex);
            rootmap.put(i, out);
        }
        return rootmap;
    }

    //bfs visit in breadth-first search class
    public void bfsvisit(int v, HashMap<Integer, HashSet<Integer>> in,List<Integer> out,HashSet<Integer> vertex){
        Stack<Integer> q = new Stack<>();
        HashSet<Integer> visited = new HashSet<>();
        q.add(v);
        visited.add(v);
        while(!q.isEmpty()){
            int next = q.pop();
            if (in.get(next)!=null) {
                for (int i : in.get(next)) {
                    if (!visited.contains(i)) {
                        q.add(i);
                        visited.add(i);
                        out.add(i);
                    }
                }
            }
        }
    }

    //get edges for betweenness calculation
    public HashMap<List<Integer>,Integer> getbetweenset(){
	    HashMap<List<Integer>,Integer> betweenEdge = new HashMap<>();
        for (int i=0;i<numVertices;i++) {
            List<Integer> temp = new ArrayList<>();
            temp.add(vertices.get(i).getVertex());
            betweenEdge.put(temp,0);
        }
        for (int i=0;i<numEdges;i++){
            List<Integer> temp = new ArrayList<>();
            temp.add(edges.get(i).getStartNode());
            temp.add(edges.get(i).getEndNode());
            betweenEdge.put(temp,0);
        }
        return betweenEdge;
    }

    //calculate shortest path & accummulate edges to determine edge betweenness
    public HashMap<List<Integer>,Integer> edge_betweenness(){
        HashMap<Integer,HashSet<Integer>> neighbors = getNeighbors();
        HashMap<List<Integer>,Integer> betweenEdge = getbetweenset();
        HashSet<Integer> vertex = getNodes();
        for (int i : vertex){
            List<Integer> S = new ArrayList<>();
            HashMap<Integer,List<Integer>> P = new HashMap<>();
            HashMap<Integer,Integer> sigma = new HashMap<>();
            singleshortestpath(i, vertex, neighbors, S, P, sigma);
            accumulate_edges(i,S,P,sigma,betweenEdge);
        }
        for (int i : vertex){
            List<Integer> temp = new ArrayList<>();
            temp.add(i);
            betweenEdge.remove(temp);
        }
        return betweenEdge;
    }

    //iteratively remove edges and classify communities at each step
    public HashMap<Integer,List<Integer>> getCommunity(){
        HashMap<List<Integer>,Integer> betweenCommunity = edge_betweenness();
        HashSet<Integer> vertex = getNodes();
        HashMap<Integer,List<Integer>> community = new HashMap<>();
        HashMap<Integer,HashSet<Integer>> initialneighbors = getNeighbors();
        HashSet<List<Integer>> fcomponents = getCCs(initialneighbors);
        for (int i : vertex){
            community.put(i,new ArrayList<>());
        }
        int cnt = 0;
        for (List<Integer> out : fcomponents){
            for (int i : out) {
                List<Integer> temp = community.get(i);
                temp.add(cnt);
                community.put(i,temp);
            }
            cnt++;
        }
        int iter = 2;
        while(!betweenCommunity.isEmpty()) {
            int max = 0;
            for (int e : betweenCommunity.values()) {
                if (e > max) {
                    max = e;
                }
            }
            HashSet<List<Integer>> delete = new HashSet<>();
            for (List<Integer> j : betweenCommunity.keySet()) {
                if (betweenCommunity.get(j) == max) {
                    delete.add(j);
                }
            }
            betweenCommunity.keySet().removeAll(delete);
            HashMap<Integer, HashSet<Integer>> nextneighbors = neighborRecalc(betweenCommunity);
            HashSet<List<Integer>> connectecomponents = getCCs(nextneighbors);
            cnt = 0;
            for (List<Integer> out : connectecomponents){
                for (int i : out) {
                    List<Integer> temp = community.get(i);
                    temp.add(cnt);
                    community.put(i,temp);
                }
                cnt++;
            }
            for (int i : vertex){
                if(community.get(i).size()<iter){
                    List<Integer> temp = community.get(i);
                    temp.add(cnt);
                    community.put(i,temp);
                    cnt++;
                }
            }
            iter++;
        }
        return community;
    }

    //depth-first search visit to determine connected components
    public void dfsvisit(int v, HashSet<Integer> visited, Stack<Integer> finished2, HashMap<Integer, HashSet<Integer>> in,List<Integer> out,HashSet<List<Integer>> ans){
        visited.add(v);
        out.add(v);
        if (in.containsKey(v)) {
            for (int i : in.get(v)) {
                if (!visited.contains(i)) {
                    dfsvisit(i, visited, finished2, in,out,ans);
                }
            }
        }
        finished2.push(v);
        ans.add(out);
    }

    //determine connected components by DFS
    public HashSet<List<Integer>> getCCs(HashMap<Integer, HashSet<Integer>> neighbors){
        Stack<Integer> v = new Stack<>();
        Stack<Integer> finished = new Stack<>();
        HashSet<Integer> visited = new HashSet<>();
        for (int i : neighbors.keySet()){
            v.push(i);
        }
        HashSet<List<Integer>> ans = new HashSet<>();
        while(!v.isEmpty()){
            int o = v.pop();
            List<Integer> out = new ArrayList<>();
            if(!visited.contains(o)){
                dfsvisit(o,visited,finished,neighbors,out,ans);
            }
        }
        HashSet<List<Integer>> ccs = new HashSet<>();
        for (List<Integer> j:ans) {
            ccs.add(j);
        }
        return ccs;
    }


    //determine neighbors
    public HashMap<Integer, HashSet<Integer>> neighborRecalc(HashMap<List<Integer>,Integer> in) {
        HashMap<Integer, HashSet<Integer>> out = new HashMap<>();
        graph out2 = new CapGraph();
        HashSet<Integer> vertex = getNodes();
        for (int i : vertex){
            out2.addVertex(i);
        }
        for (List<Integer> edge : in.keySet()){
            out2.addEdge(edge.get(0),edge.get(1));
        }
        int j = ((CapGraph) out2).numEdges;
        for (int i = 0;i<((CapGraph) out2).numEdges;i++){
            if (!out.containsKey(((CapGraph) out2).edges.get(i).getStartNode())){
                HashSet<Integer> out1 = new HashSet<>();
                out1.add(((CapGraph) out2).edges.get(i).getEndNode());
                out.put(((CapGraph) out2).edges.get(i).getStartNode(),out1);

            }
            if (out.containsKey(((CapGraph) out2).edges.get(i).getStartNode())){
                HashSet<Integer> temp = out.get(((CapGraph) out2).edges.get(i).getStartNode());
                temp.add(((CapGraph) out2).edges.get(i).getEndNode());
                out.put(((CapGraph) out2).edges.get(i).getStartNode(),temp);
            }
        }
        return out;
    }

    //accumulate edges for edge betweenness
    public void accumulate_edges(int input, List<Integer> S, HashMap<Integer,List<Integer>> P,
                                 HashMap<Integer,Integer> sigma, HashMap<List<Integer>,Integer> betweenEdge) {
        HashMap<Integer, Integer> delta = new HashMap<>();
        Stack<Integer> q = new Stack<>();
        for (int i : S) {
            delta.put(i, 0);
            q.push(i);
        }
       while (!q.isEmpty()) {
            int w = q.pop();
            int coeff = (1 + delta.get(w)) / sigma.get(w);
            if (P.get(w) != null) {
                for (int v : P.get(w)) {
                    int c = (sigma.get(v) * coeff);
                    List<Integer> test = new ArrayList<>();
                    List<Integer> testi = new ArrayList<>();
                    test.add(v);
                    test.add(w);
                    testi.add(w);
                    testi.add(v);
                    if (!betweenEdge.containsKey(test)) {
                        int iin = betweenEdge.get(testi);
                        betweenEdge.remove(testi);
                        betweenEdge.put(testi, iin + c);
                    }
                    else {
                        int iin = betweenEdge.get(test);
                        betweenEdge.remove(test);
                        betweenEdge.put(test, iin + c);
                    }
                    int iin = delta.get(v);
                    delta.remove(v);
                    delta.put(v, iin + c);
                }
                if (w != input) {
                    if (betweenEdge.get(w) != null) {
                        List<Integer> temp = new ArrayList<>();
                        temp.add(w);
                        betweenEdge.remove(temp);
                        betweenEdge.put(temp, betweenEdge.get(w) + (delta.get(w)));
                    }
                }
            }
        }
    }

    //determine shortest path
    public void singleshortestpath(int input, HashSet<Integer> vertex,
                                   HashMap<Integer,HashSet<Integer>> neighbors, List<Integer> S, HashMap<Integer,List<Integer>> P,
                                   HashMap<Integer,Integer> sigma){
        Stack<Integer> q = new Stack<>();
        HashMap<Integer,Integer> D = new HashMap<>();
        for (int i : vertex){
            sigma.put(i,0);
            P.put(i,new ArrayList<>());
        }
        sigma.put(input,1);
        D.put(input,0);
        q.add(input);
        while(!q.isEmpty()){
            int v = q.remove(0);
            S.add(v);
            int dv = D.get(v);
            int sv = sigma.get(v);
            if(neighbors.get(v)!=null){
                for (int w : neighbors.get(v)){
                    if (!D.containsKey(w)){
                        q.add(w);
                        D.put(w,dv+1);
                    }
                    if (D.get(w)==(dv+1)){
                        sigma.put(w,sigma.get(w)+sv);
                        List<Integer> temp = P.get(w);
                        temp.add(v);
                        P.put(w,temp);
                    }
                }
            }
        }
    }
}
