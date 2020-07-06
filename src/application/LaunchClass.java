package application;

import graph.CapGraph;
import graph.graph;

import java.util.Random;


public class LaunchClass {
	
	public LaunchClass() {
		super();
	}
	
	public graph getGraph(String text) {
		graph graph = new CapGraph();
        graphloader.GraphLoader.loadGraph(graph,text);
		return graph;
	}

}
