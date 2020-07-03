import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class DijkstraImplementation {

    public static void main(String[] args) {
        File inFile = new File("cop3503-asn2-input.txt");
        File outFile = new File("cop3503-asn2-output-chitty-nicholas.txt");
        try {
            outFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(inFile));
        } catch (FileNotFoundException e) {
            System.out.println("Could not find input file.");
            System.exit(0);
        }
        try {
            int noOfVertices = Integer.parseInt(reader.readLine());
            ArrayList<Vertex> vertices = new ArrayList<>(noOfVertices + 1);
            int sourceVertex = Integer.parseInt(reader.readLine());
            int numberOfEdges = Integer.parseInt(reader.readLine());
            for(int i = 0; i < noOfVertices; i++) {
                Vertex vertex = new Vertex(i+1, noOfVertices);
                vertices.add(i+1, vertex);
            }
            for(int i = 0; i < numberOfEdges; i++) {
                String[] edgeInfo = reader.readLine().split(" ");
                int vertexSerial1 = Integer.parseInt(edgeInfo[0]);
                int vertexSerial2 = Integer.parseInt(edgeInfo[1]);
                int weight = Integer.parseInt(edgeInfo[2]);
                Vertex vertex1 = vertices.get(vertexSerial1);
                Vertex vertex2 = vertices.get(vertexSerial2);
                vertex1.createEdge(vertexSerial2, weight);
                vertex2.createEdge(vertexSerial1, weight);
            }
            vertices.get(sourceVertex).setParent(0);
            vertices.get(sourceVertex).setCost(0);
        } catch (NumberFormatException | IOException e) {
            e.printStackTrace();
        }
    }
}

class Vertex {
    
    //The number associated with this vertex
    int serial;
    //The weight associated with traveling to the vertex with the index as the serial number 
    int[] edges;
    //the node preceding is parent, cost is how much it costs to get there
    int parent = -1, cost = Integer.MAX_VALUE; //-1 = unvisited, max int represents infinity
    
    public Vertex(int serial, int numberOfVertices) {
        this.serial = serial;
        this.edges = new int[numberOfVertices+1];
    }

    public void createEdge(int to, int weight) {
        this.edges[to] = weight;
    }
    
    public void setParent(int parent) {
        this.parent = parent;
    }

    public void setCost(int cost) {
        this.cost = cost;  
    }

    public int getWeightTo(int to) {
        return this.edges[to];
    }

    public int getSerial() {
        return this.serial;
    }

    public int getParent() {
        return this.parent;
    }

    public int getCost() {
        return this.cost;
    }
}