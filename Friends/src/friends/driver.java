package friends;
import java.io.*;
import java.util.*;

class driver {
	
	public static void main(String args[]) throws IOException {
		Graph graph = makeGraph("conntest6.txt");
		System.out.println(Friends.connectors(graph));	
		}
	
	
	public static Graph makeGraph(String file) throws FileNotFoundException {
		Scanner sc = new Scanner(new File(file));
		Graph graph = new Graph(sc);
		return graph;
	}
}
