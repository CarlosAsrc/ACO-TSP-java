import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class App {

	public static Graph graph = new Graph();
	public static Scanner in = new Scanner (System.in);

	public static void main(String [] args) {
		menu();
	}

	public static void menu() {
		System.out.println("Informe o nome do arquivo:");
		String arq = in.nextLine();
		read(arq);
		graph.printEdges();
	}

	public static void read(String arq) {
		Path path = Paths.get(arq);
		try(Scanner reader = new Scanner(Files.newBufferedReader(path, Charset.forName("utf-8")))) {
			String nodeA, nodeB, dist;
			String line;
			String lineSplitted[] = new String[3];
			while( !(line = reader.nextLine()).equals("end")) {
				lineSplitted = line.split(" ");
				nodeA = lineSplitted[0];
				nodeB = lineSplitted[1];
				dist = lineSplitted[2];
				graph.add(nodeA, nodeB, Double.parseDouble(dist));
			}

		}
		catch(IOException e) {
			System.out.println("Arquivo não encontrado!");
		}
	}
}