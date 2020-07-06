package graph;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public interface graph {
    /* Creates a vertex with the given number. */
    public void addVertex(int num);
    
    /* Creates an edge from the first vertex to the second. */
    public void addEdge(int from, int to);

    /* Return the graph's connections.  */
    public HashMap<Integer, HashSet<Integer>> exportGraph();

} 
