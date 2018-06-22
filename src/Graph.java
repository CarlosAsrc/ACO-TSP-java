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

		
		public Node(String name) {
			this.nome = name;
			this.marca = 0;
			edges = new ArrayList<Edge>();
			p = null;
			d = 0;
		}

		public String getName() {
			return nome;
		}

		public int getMarca() {
			return marca;
		}

		public List<Edge> getEdges() {
			return edges;
		}

		public Node getP() {
			return p;
		}

		public int getD() {
			return d;
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

		public Node getA() {
			return a;
		}

		public Node getB() {
			return b;
		}

		public double getDist() {
			return dist;
		}

		public double getPheromone() {
			return pheromone;
		}

		@Override
		public String toString() {
			return "Edge [a=" + a + ", b=" + b + ", dist=" + dist + ", pheromone=" + pheromone + "]";
		}
		
		
	}
	
	private class Ant {
		public String name;
		public Node position;
		
		public Ant(String name, Node position) {
			this.name = name;
			this.position = position;
		}
		
		public boolean move(Node target) {
			for(Edge edge: position.edges) {
				if(target.getName().equals(edge.getA()) || target.getName().equals(edge.getB())) {
					this.position = target;
					return true;
				}				
			}
			return false;
		}
	}
	
	
	
	
//Atributos do Grafo
	private List<Node> nodes;
	private List<Edge> edges;
	private List<Ant> ants;
	
//Construtor
	public Graph () {
		this.nodes = new ArrayList<Node>();
		this.edges = new ArrayList<Edge>();
		this.ants = new ArrayList<Ant>();
	}

//Operações básicas do grafo
	public void add(String a, String b, double dist) {
		if(!existNode(a)) {
			Node newNode = new Node(a);
			Ant newAnt = new Ant("ant"+ants.size()+1, newNode);
			ants.add(newAnt);
			nodes.add(newNode);
		}
		if(!existNode(b)) {
			Node newNode = new Node(b);
			Ant newAnt = new Ant("ant"+ants.size()+1, newNode);
			ants.add(newAnt);
			nodes.add(newNode);
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
	
	public void printEdges() {
		for(Edge edge: edges) {
			System.out.println(edge.toString());
		}
	}
	
}