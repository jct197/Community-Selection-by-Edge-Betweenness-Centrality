package graphloader;

/** 
 * A class that represents a text document
 * @author John C. Thomas
 */
import graph.graph;
import java.util.*;

public class GraphLoader {
	/**
	 * Loads graph with data GUI text area.
	 */
	private String text;

	public GraphLoader(String text) {this.text = text;}

	public String getText() {return this.text;}

	public static graph loadGraph(graph g, String filename) {
		Set<Integer> seen = new HashSet<Integer>();

		Scanner sc;
		try {
			sc = new Scanner(filename);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		// Iterate over the lines in the file, adding new
		// vertices as they are found and connecting them with edges.
		while (sc.hasNextInt()) {
			int v1 = sc.nextInt();
			int v2 = sc.nextInt();
			if (!seen.contains(v1)) {
				g.addVertex(v1);
				seen.add(v1);
			}
			if (!seen.contains(v2)) {
				g.addVertex(v2);
				seen.add(v2);
			}
			g.addEdge(v1, v2);
		}

		sc.close();

		return g;
	}

}