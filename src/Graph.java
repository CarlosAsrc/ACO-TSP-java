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



////-------------------------------------TRABALHO:
//	public void menorCaminho (String origem, String destino) {
//		Node inicial = getNodeByName(origem);
//		Node terminal = getNodeByName(destino);
//		String resposta = "";
//
//		dijkstra(inicial);
//
//		ArrayList<String> caminho = new ArrayList<>();
//
//		Node aux = terminal;
//		String conta = "";
//		while(aux.nome != inicial.nome) {
//			for(String key: aux.irmaos.keySet()) {
//				if(aux.irmaos.get(key).nome.equals(aux.p.nome)) {
//					conta = key;
//				}
//			}
//			caminho.add(conta+" ("+aux.p.nome +" "+aux.nome + ")");
//
//			aux = aux.p;
//		}
//		Collections.reverse(caminho);
//		caminho.forEach(n -> {System.out.println(n);});
//	}
//
//
//
//	public void dijkstra(Node inicial) {
//		nodes.forEach(w -> {
//			w.d = Integer.MAX_VALUE;
//			w.p = null;
//			w.marca=0;
//		});
//		inicial.d = 0;
//
//		Node u;
//		while( (u = min()) != null) {
//			u.marca=1;
//			for(Node w: u.irmaos.values()) {
//				if( (u.d + 1) < w.d) {
//					w.d = u.d + 1;
//					w.p = u;
//				}
//			}
//		}
//	}
//
//
//	public Node min() {
//		int menor = Integer.MAX_VALUE;
//		Node resp = null;
//		for(Node n: nodes) {
//			if(n.marca==0 && n.d < menor){
//				menor = n.d;
//				resp = n;
//			}
//		};
//		return resp;
//	}
//
//
//
//
//
//
////---------------------------------------------EXTRAS:
//	public void caminhoProfundidade () {
//		Node inicio = getNodeByName("Luis");
//		System.out.println(inicio.nome);
//		nodes.forEach(n -> {n.marca = 0;});
//		inicio.marca = 1;
//		ArrayList<Node> fila = transformaEmArrayList(inicio.irmaos);
//		//fila.forEach(n->{System.out.println("|"+n.nome+"|");});
//		while(!fila.isEmpty()) {
//			//fila.forEach(n->{System.out.print("|"+n.nome+"|");});
//			Node primeiro = fila.remove(fila.size()-1);
//			primeiro.marca=1;
//			System.out.println(primeiro.nome);
//			for(Node n: primeiro.irmaos.values()) {
//				if(n.marca == 0){
//					fila.add(n);
//					n.marca=1;
//				}
//			}
//
//		}
//	}
//
//	public ArrayList<Node> transformaEmArrayList(HashMap<String, Node> vizinhos) {
//		ArrayList<Node> resp = new ArrayList();
//		for(Node v: vizinhos.values()) {
//			resp.add(v);
//		}
//		return resp;
//	}
//
//
//	public int quantosCaminhos (Node a, Node b) {
//		if(a.nome.equals(b.nome)){return 1;}
//		a.marca=1;
//		int acc=0;
//		for(Node n: a.irmaos.values()) {
//			if(n.marca==0){
//				acc= acc+quantosCaminhos(n, b);
//
//			}
//		}
//		a.marca=0;
//		return acc;
//	}
//}