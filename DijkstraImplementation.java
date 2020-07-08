import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;

public class DijkstraImplementation {

    //Keeps track of all the serial numbers and their related vertices
    static HashMap<Integer, Vertex> vertices;
    //Keeps track of all the unvisited nodes added after conclusion of setting weights
    static PriorityQueue<Vertex> unvisited = new PriorityQueue<>(1, new Vertex(-1));

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
            vertices = new HashMap<>();
            for (int i = 0; i < noOfVertices; i++) {
                Vertex vertex = new Vertex(i+1);
                vertices.put(i+1, vertex);
            }
            sourceVertex = Integer.parseInt(reader.readLine());
            numberOfEdges = Integer.parseInt(reader.readLine());
            for (int i = 0; i < numberOfEdges; i++) {
                String[] edgeInfo = reader.readLine().split(" ");
                int vertexSerial1 = Integer.parseInt(edgeInfo[0]);
                int vertexSerial2 = Integer.parseInt(edgeInfo[1]);
                int weight = Integer.parseInt(edgeInfo[2]);
                Vertex vertex1 = vertices.get(vertexSerial1);
                Vertex vertex2 = vertices.get(vertexSerial2);
                Vertex.createEdge(vertex1, vertex2, weight);
            }
            vertices.get(sourceVertex).setParent(-1);
            vertices.get(sourceVertex).setCost(-1);
            unvisited.add(vertices.get(sourceVertex));
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
        Vertex current;
        //while unvisited is not empty
        while(!unvisited.isEmpty()) {
            current = unvisited.poll();
            while(current.visited) {
                current = unvisited.poll();
                if(current == null) break;
            }
            if(current == null) break;
            //for each neighbor
            for(Vertex neighbor : current.weights.keySet()) {
                //calculate cost through current
                int cost = current.getCost() == -1 ? current.getEdgeWeight(neighbor) 
                : current.cost + current.getEdgeWeight(neighbor);
                //if calculated cost < current cost
                if(cost < neighbor.cost) {
                    //replace cost and parent
                    neighbor.setCost(cost);
                    neighbor.setParent(current.serial);
                }
                //add all neighbors to the priority queue
                if(neighbor.visited == false) {
                    unvisited.add(neighbor);
                }
            }
            //mark as visited
            current.visited = true;
            //the queue will set current to the least cost, lowest serial neighboy
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
            writer.write(vertices.size() + "");
            for (Vertex v : vertices.values()) {
                writer.newLine();
                writer.write(v.getSerial() + " " + v.getCost() + " " + v.getParent());
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class Vertex implements Comparator<Vertex> {

    // The number associated with this vertex
    int serial;
    //Map containing an integer w.r.t the vertex serial and the value being
    //the vertex object
    HashMap<Integer, Vertex> edges = new HashMap<>();
    //Map containing the vertex being travelled to and the weights
    //associated with traversing that edge
    HashMap<Vertex, Integer> weights = new HashMap<>();
    // the node preceding is parent, cost is how much it costs to get there
    int parent = 0, cost = Integer.MAX_VALUE; // 0 = unvisited, max int represents infinity

    boolean visited = false;


    public Vertex(int serial) {
        this.serial = serial;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getSerial() {
        return this.serial;
    }

    /**
     * 
     * @param v The vertex to the edge
     * @return the weight of the edge associated with the vertex, if it does not exist, then the integer is max value
     */
    public int getEdgeWeight(Vertex v) {
        if(v == null) {
            return Integer.MAX_VALUE;
        } else {
            return weights.get(v);
        }
    }

    /**
     * 
     * @param serial The serial number of the vertex we are trying to travel to
     * @return Null if the vertex does not exist, the vertex at the end of the edge if it does
     */
    public Vertex getVertexAtEdge(int serial) {
        return edges.get(serial);
    }


    public int getParent() {
        return this.parent;
    }

    public int getCost() {
        return this.cost;
    }

    /**
     * Creates a bidirectional edge between two vertices
     * @param a A vertex
     * @param b The other vertex
     * @param weight The weight associated with the edge
     */
    public static void createEdge(Vertex a, Vertex b, int weight) {
        a.edges.put(b.serial, b);
        b.edges.put(a.serial, a);
        a.weights.put(b, weight);
        b.weights.put(a, weight);
    }

    @Override
    public String toString() {
        return "Vertex: " + this.serial + "| Visited: " + this.visited;
    }

    @Override
    public int compare(Vertex arg0, Vertex arg1) {
        if(arg0.cost - arg1.cost == 0)
            return arg0.serial - arg1.serial;
        else
            return arg0.cost - arg1.cost;
    }
}