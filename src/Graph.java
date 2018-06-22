import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Graph {
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
		
		public double getSumInverseDistFeromon() {
			double result=0;
			for(Edge edge: edges) {
				result = result+edge.inverse_distance_feromon;
			}
			return result;
		}

	}
	
	private class Edge {
		public Node a;
		public Node b;
		public double dist;
		public double feromon;
		public double inverse_distance;
		public double inverse_distance_feromon;
		public double probabilityAB;
		public double probabilityBA;
		public double probabilityPercentageAB;
		public double probabilityPercentageBA;
		
		public Edge(Node a, Node b, double dist) {
			this.a = a;
			this.b = b;
			this.dist = dist;
			this.feromon = INITIAL_FEROMON;
			this.inverse_distance = format(1/dist);
			this.inverse_distance_feromon = format(this.feromon * this.inverse_distance);
			this.probabilityAB = 0;
			this.probabilityBA = 0;
			this.probabilityPercentageAB = 0;
			this.probabilityPercentageBA = 0;
//			this.probabilityAB = this.inverse_distance_feromon / a.getSumInverseDistFeromon();
//			this.probabilityBA = this.inverse_distance_feromon / b.getSumInverseDistFeromon();
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
			return feromon;
		}

		@Override
		public String toString() {
			return "Edge [a=" + a + ", b=" + b + ", dist=" + dist + ", feromon=" + feromon + ", inverse_distance="
					+ inverse_distance + ", inverse_distance_feromon=" + inverse_distance_feromon + ", probabilityAB="
					+ probabilityAB + ", probabilityBA=" + probabilityBA + ", probabilityPercentageAB="
					+ probabilityPercentageAB + ", probabilityPercentageBA=" + probabilityPercentageBA + "]";
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
	
	
	
	
//Atributos e constantes do Grafo
	private static DecimalFormat format = new DecimalFormat("#.###");
	private static double FEROMONE_INFLUENCE = 1;
	private static double DISTANCE_INFLUENCE = 1;
	private static double EVAPORATION = 0.01;
	private static double INITIAL_FEROMON = 0.1;
	private static double FEROMONAL_UPDATE_COEFFICIENT = 10;
	
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
	
	public void printRoutes() {
		for(Edge edge: edges) {
			System.out.println(edge.probabilityAB);
		}
		System.out.println("------------------");
		for(Edge edge: edges) {
			System.out.println(edge.probabilityBA);
		}
	}
	
	
	public void updateProbabilities() {
		
		for(Edge edge: edges) {
			edge.probabilityAB = format(edge.inverse_distance_feromon / edge.a.getSumInverseDistFeromon());
			edge.probabilityBA = format(edge.inverse_distance_feromon / edge.b.getSumInverseDistFeromon());
			edge.probabilityPercentageAB = edge.probabilityAB * 100;
			edge.probabilityPercentageBA = edge.probabilityBA * 100;
		}
	}
	
	public void iteration() {
		
	}
	
	public static Double format(Double n) {
		return Double.valueOf((format.format(n).replaceAll(",", ".")));
	}
	
}