import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Graph {
//Constantes:
	private static double INITIAL_PHEROMONE = 0.1;
	
	
//Classes dos obejtos do Grafo:
	private class Node {

		public String nome;
		public int marca;
		public List<Edge> edges;
		public Node p;
		public int d;

		
		public Node(String nome) {
			this.nome = nome;
			this.marca = 0;
			edges = new ArrayList<Edge>();
			p = null;
			d = 0;
		}

		public void addEdge(Edge n) {
			this.edges.add(n);
		}

	}
	
	private class Edge {
		public Node a;
		public Node b;
		public double dist;
		public double pheromone;
		
		public Edge(Node a, Node b, double dist) {
			this.a = a;
			this.b = b;
			this.dist = dist;
			this.pheromone = INITIAL_PHEROMONE;
		}
		
	}
	
	private class Ant {
		public String name;
		public Node position;
	}
	
	
	
	
//Atributos do Grafo
	private List<Node> nodes;
	private List<Edge> edges;
	
//Construtor
	public Graph () {
		this.nodes = new ArrayList<Node>();
		this.edges = new ArrayList<Edge>();
	}

//Operações básicas do grafo
	public void add(String a, String b, double dist) {
		if(!existNode(a)) {
			nodes.add(new Node(a));
		}
		if(!existNode(b)) {
			nodes.add(new Node(b));
		}
		Node nodeA = getNodeByName(a);
		Node nodeB = getNodeByName(b);
		Edge edge = new Edge(nodeA, nodeB, dist);
		nodeA.addEdge(edge);
		nodeB.addEdge(edge);
		this.edges.add(edge);
	}

	public boolean existNode(String name) {
		for(Node n: nodes) {
			if(n.nome.equals(name)) {
				return true;
			}
		}
		return false;
	}

	public Node getNodeByName(String nome) {
		for(Node n: nodes) {
			if(n.nome.equals(nome)) {
				return n;
			}
		}
		return null;
	}
	
}