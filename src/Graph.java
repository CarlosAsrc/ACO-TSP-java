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

		@Override
		public String toString() {
			return "Node [nome=" + nome + ", marca=" + marca + ", edges=" + edges + ", p=" + p + ", d=" + d + "]";
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
		
		public double getProbability(Node n) {
			if(this.a.equals(n) && this.a.marca==1) {return this.probabilityPercentageAB;}
			if(this.b.marca==1) {return this.probabilityPercentageBA;}
			return -1;
		}

		@Override
		public String toString() {
			return "Edge [a=" + a.toString() + ", b=" + b.toString() + ", dist=" + dist + ", feromon=" + feromon + ", inverse_distance="
					+ inverse_distance + ", inverse_distance_feromon=" + inverse_distance_feromon + ", probabilityAB="
					+ probabilityAB + ", probabilityBA=" + probabilityBA + ", probabilityPercentageAB="
					+ probabilityPercentageAB + ", probabilityPercentageBA=" + probabilityPercentageBA + "]";
		}


		
		
		
	}
	
	private class Ant {
		public String name;
		public Node position;
		public List<Edge> LastWay;
		
		public Ant(String name, Node position) {
			this.name = name;
			this.position = position;
			LastWay = new ArrayList<>();
		}
		
		public void move(Edge edge) {
			if(this.position.equals(edge.a)) {this.position = edge.a;}
			else {this.position = edge.b;}
			this.position.marca = 1;
		}
		
		public double getLastWayDist() {
			double result = 0;
			for(Edge edge: LastWay) {
				result = result + edge.dist;
			}
			return result;
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
			edge.inverse_distance_feromon = format(edge.feromon * edge.inverse_distance);
			edge.probabilityAB = format(edge.inverse_distance_feromon / edge.a.getSumInverseDistFeromon());
			edge.probabilityBA = format(edge.inverse_distance_feromon / edge.b.getSumInverseDistFeromon());
			edge.probabilityPercentageAB = edge.probabilityAB * 100;
			edge.probabilityPercentageBA = edge.probabilityBA * 100;
		}
	}
	
	public void markOff() {
		for(Node node: nodes) {
			node.marca = 0;
		}
	}
	
	public void iteration(int iterations) {
		double higherProbability = 0;
		double currentProbability = 0;
		Edge next = null;
		for(int i=1; i!=iterations; i++) {
			for(Ant ant: this.ants) {
				higherProbability = 0;
				currentProbability = 0;
				next = null;
				ant.LastWay.clear();
				markOff();
				for(int j=0; j<edges.size(); j++) {
					for(Edge edge: ant.position.edges) {
						if(higherProbability > (currentProbability = edge.getProbability(ant.position)) ){ 
							higherProbability = currentProbability;
							next = edge;
						}
					}
					ant.move(next);
					ant.LastWay.add(next);
				}
			}
			updateFeromon();
			updateProbabilities();
		}
	}
	
	
	public void updateFeromon() {
		for(Edge edge: edges) {
			edge.feromon = (1-EVAPORATION) * edge.feromon;
			for(Ant ant: ants) {
				for(Edge visitedEdge: ant.LastWay) {
					if(edge.equals(visitedEdge)) {
						edge.feromon = edge.feromon + FEROMONAL_UPDATE_COEFFICIENT/ant.getLastWayDist();
					}
				}
			}
		}
	}
	
	public static Double format(Double n) {
		return Double.valueOf((format.format(n).replaceAll(",", ".")));
	}
	
}