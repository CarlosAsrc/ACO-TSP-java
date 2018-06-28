import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Graph {
//Classes dos obejtos do Grafo:
	private class Node {

		public String name;
		public int marca;
		public List<Edge> edges;

		
		public Node(String name) {
			this.name = name;
			this.marca = 0;
			edges = new ArrayList<Edge>();
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
			return "Node [name=" + name + ", marca=" + marca + ", edges=" + edges + "]";
		}
	}
	
	private class Edge {
		public Node a;
		public Node b;
		public String name;
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
			this.name = a.name+"<->"+b.name;
			this.dist = dist;
			this.feromon = INITIAL_FEROMON;
			this.inverse_distance = format(1/dist);
			this.inverse_distance_feromon = format(this.feromon * this.inverse_distance);
			this.probabilityAB = 0;
			this.probabilityBA = 0;
			this.probabilityPercentageAB = 0;
			this.probabilityPercentageBA = 0;
		}

		
		public double getProbability(Node n) {
			if(this.a.equals(n) && b.marca==1) {return this.probabilityPercentageAB;}
			if(a.marca==1) {return this.probabilityPercentageBA;}
			return -1;
		}

		@Override
		public String toString() {
			return "dist=" + dist;
		}
	}
	
	private class Ant {
		public String name;
		public Node initialPos;
		public Node position;
		public List<Edge> LastWay;
		
		public Ant(String name, Node position) {
			this.name = name;
			this.initialPos = position;
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

		@Override
		public String toString() {
			return "Ant [name=" + name + ", position=" + position + ", LastWay=" + LastWay + "]";
		}
		
	}
	
	
	
	
//Atributos e constantes do Grafo para fins de calculo.
	private static DecimalFormat format = new DecimalFormat("#.###");
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
			Ant newAnt = new Ant("ant"+(ants.size()+1), newNode);
			ants.add(newAnt);
			nodes.add(newNode);
		}
		if(!existNode(b)) {
			Node newNode = new Node(b);
			Ant newAnt = new Ant("ant"+(ants.size()+1), newNode);
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
			if(n.name.equals(name)) {
				return true;
			}
		}
		return false;
	}

	public Node getNodeByName(String nome) {
		for(Node n: nodes) {
			if(n.name.equals(nome)) {
				return n;
			}
		}
		return null;
	}
	
	

	
	//Desmarca os nodos percorridos após o término de um caminhamento.
	public void restartStateNodesAnts() {
		for(Node node: nodes) {
			node.marca = 0;
		}
		for(Ant ant: ants) {
			ant.position = ant.initialPos;
		}
		
	}
	
	//Dá início ao processo de otimização. Recebe o número de iterações a serem executadas. 
	public void iteration(int iterations) {
		double higherProbability = 0;
		double currentProbability = 0;
		updateProbabilities();
		Edge next = null;
		for(int i=1; i!=iterations; i++) {
			for(Ant ant: this.ants) {
				higherProbability = 0;
				currentProbability = 0;
				next = null;
				ant.LastWay.clear();
				restartStateNodesAnts();
				ant.position.marca = 2;
				for(int j=0; j<nodes.size(); j++) {
					for(Edge edge: ant.position.edges) {
						if(!ant.LastWay.contains(edge)) {
							if(higherProbability > (currentProbability = edge.getProbability(ant.position)) ){ 
								higherProbability = currentProbability;
								next = edge;
							}
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
	
	//Atualiza a quantidade de feromônio de cada aresta após a iteração.
	public void updateFeromon() {
		for(Edge edge: edges) {
			edge.feromon = (1-EVAPORATION) * edge.feromon;
			for(Ant ant: ants) {
				for(Edge visitedEdge: ant.LastWay) {
					if(edge.equals(visitedEdge)) {
						edge.feromon = format(edge.feromon + FEROMONAL_UPDATE_COEFFICIENT/ant.getLastWayDist());
					}
				}
			}
		}
	}
	
	//Atualiza para cada aresta a probabilidade de ser escolhida, tanto do nodo A para o nodo B, quanto o inverso.
	public void updateProbabilities() {
		for(Edge edge: edges) {
			edge.inverse_distance_feromon = format(edge.feromon * edge.inverse_distance);
			edge.probabilityAB = format(edge.inverse_distance_feromon / edge.a.getSumInverseDistFeromon());
			edge.probabilityBA = format(edge.inverse_distance_feromon / edge.b.getSumInverseDistFeromon());
			edge.probabilityPercentageAB = edge.probabilityAB * 100;
			edge.probabilityPercentageBA = edge.probabilityBA * 100;
		}
	}
	
	//MÉTODO PARA ARREDONDAR NÚMEROS EM 3 CASAS.
	public static Double format(Double n) {
		return Double.valueOf((format.format(n).replaceAll(",", ".")));
	}
	
//MÉTODOS AUXILIARES PARA EXIBIR DADOS:
	public void printAntsLastWays() {
		for(Ant ant: ants) {
			System.out.println("\nFormiga "+ant.name+": ");
			ant.LastWay.forEach( (n) -> {
				System.out.print(" |"+n.name+"|");
			});
		}
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
	
	public void printSolution() {
		
	}
	

	
}