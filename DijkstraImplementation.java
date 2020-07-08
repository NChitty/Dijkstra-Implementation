import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.TreeMap;

public class DijkstraImplementation {

    static TreeMap<Integer, Vertex> vertices;

    public static void main(String[] args) {
        File inFile = new File("cop3503-asn2-input.txt");
        File outFile = new File("cop3503-asn2-output-chitty-nicholas.txt");
        int sourceVertex = readFile(inFile);
        doDijkstra(sourceVertex, vertices.size() - 1);
        writeFile(outFile);
    }

    /**
     * 
     * @param input the file to read the input from
     * @return the serial number of the source vertex
     */
    public static int readFile(File input) {
        int sourceVertex = -1, noOfVertices, numberOfEdges;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(input));
        } catch (FileNotFoundException e) {
            System.out.println("Could not find input file.");
            System.exit(0);
        }
        try {
            noOfVertices = Integer.parseInt(reader.readLine());
            vertices = new TreeMap<>();
            sourceVertex = Integer.parseInt(reader.readLine());
            numberOfEdges = Integer.parseInt(reader.readLine());
            for (int i = 0; i < noOfVertices; i++) {
                Vertex vertex = new Vertex(i+1, noOfVertices);
                vertices.put(i+1, vertex);
            }
            for (int i = 0; i < numberOfEdges; i++) {
                String[] edgeInfo = reader.readLine().split(" ");
                int vertexSerial1 = Integer.parseInt(edgeInfo[0]);
                int vertexSerial2 = Integer.parseInt(edgeInfo[1]);
                int weight = Integer.parseInt(edgeInfo[2]);
                Vertex vertex1 = vertices.get(vertexSerial1);
                Vertex vertex2 = vertices.get(vertexSerial2);
                vertex1.createEdge(vertexSerial2, weight);
                vertex2.createEdge(vertexSerial1, weight);
            }
            vertices.get(sourceVertex).setParent(-1);
            vertices.get(sourceVertex).setCost(-1);
        } catch (NumberFormatException | IOException e) {
            e.printStackTrace();
        }
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sourceVertex;
    }

    public static void doDijkstra(int source, int numberOfVertices) {
        Vertex current = vertices.get(source);
        
        while (it.hasNext()) {
            if(current.visited) continue;
            current.visited = true;
            // started at the source but iterator might look at it again
            for(int i = 0; i < current.edges.length; i++) {
                if(current.edges[i] == 0) continue; //there is no node
                if(vertices.get(i-1).getParent() == 0) { //unvisited case
                    vertices.get(i-1).setParent(current.getSerial());
                    vertices.get(i-1).setCost(current.edges[i]);
                } else if(!vertices.get(i-1).visited
                && vertices.get(i-1).getCost() > (current.getCost() + current.edges[i])) {
                    //case where the cost is lower going through this node way than the current path
                    vertices.get(i-1).setParent(current.getSerial());
                    vertices.get(i-1).setCost(current.getCost() + current.edges[i]);
                } 
            }
            current = it.next();
        }
    }

    public static void writeFile(File output) {
        if (!output.exists()) {
            try {
                output.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Creating the output file");
        }
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(output));
            writer.write(vertices.size() - 1);
            for (Vertex v : vertices) {
                writer.newLine();
                writer.write(v.getSerial() + " " + v.getCost() + " " + v.getParent());
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Vertex {

    // The number associated with this vertex
    int serial;
    // The weight associated with traveling to the vertex with the index as the
    // serial number
    int[] edges;
    // the node preceding is parent, cost is how much it costs to get there
    int parent = 0, cost = Integer.MAX_VALUE; // 0 = unvisited, max int represents infinity

    boolean visited = false;


    public Vertex(int serial, int numberOfVertices) {
        this.serial = serial;
        this.edges = new int[numberOfVertices + 1];
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