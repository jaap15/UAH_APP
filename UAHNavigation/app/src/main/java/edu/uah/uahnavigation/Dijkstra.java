package edu.uah.uahnavigation;
import java.util.*;


/**
 * Created by Jairo on 3/27/2017.
 */

/**
 *
 * @author Michael Levet
 * http://www.dreamincode.net/forums/topic/386358-dijkstras-algorithm/
 */

public class Dijkstra {

    private Graph graph;
    private String initialVertexLabel;
    private HashMap<String, String> predecessors;
    private HashMap<String, Integer> distances;
    private PriorityQueue<Vertex> availableVertices;
    private HashSet<Vertex> visitedVertices;


    /**
     * This constructor initializes this Dijkstra object and executes
     * Dijkstra's algorithm on the graph given the specified initialVertexLabel.
     * After the algorithm terminates, the shortest a-b paths and the corresponding
     * distances will be available for all vertices b in the graph.
     *
     * @param graph The Graph to traverse
     * @param initialVertexLabel The starting Vertex label
     * @throws IllegalArgumentException If the specified initial vertex is not in the Graph
     */
    public Dijkstra(Graph graph, String initialVertexLabel){
        this.graph = graph;
        Set<String> vertexKeys = this.graph.vertexKeys();

        if(!vertexKeys.contains(initialVertexLabel)){
            throw new IllegalArgumentException("The graph must contain the initial vertex.");
        }

        this.initialVertexLabel = initialVertexLabel;
        this.predecessors = new HashMap<String, String>();
        this.distances = new HashMap<String, Integer>();
        this.availableVertices = new PriorityQueue<Vertex>(vertexKeys.size(), new Comparator<Vertex>(){

            public int compare(Vertex one, Vertex two){
                int weightOne = Dijkstra.this.distances.get(one.getLabel());
                int weightTwo = Dijkstra.this.distances.get(two.getLabel());
                return weightOne - weightTwo;
            }
        });

        this.visitedVertices = new HashSet<Vertex>();

        //for each Vertex in the graph
        //assume it has distance infinity denoted by Integer.MAX_VALUE
        for(String key: vertexKeys){
            this.predecessors.put(key, null);
            this.distances.put(key, Integer.MAX_VALUE);
        }


        //the distance from the initial vertex to itself is 0
        this.distances.put(initialVertexLabel, 0);

        //and seed initialVertex's neighbors
        Vertex initialVertex = this.graph.getVertex(initialVertexLabel);
        ArrayList<Edge> initialVertexNeighbors = initialVertex.getNeighbors();
        for(Edge e : initialVertexNeighbors){
            Vertex other = e.getNeighbor(initialVertex);
            this.predecessors.put(other.getLabel(), initialVertexLabel);
            this.distances.put(other.getLabel(), e.getWeight());
            this.availableVertices.add(other);
        }

        this.visitedVertices.add(initialVertex);

        //now apply Dijkstra's algorithm to the Graph
        processGraph();

    }

    /**
     * This method applies Dijkstra's algorithm to the graph using the Vertex
     * specified by initialVertexLabel as the starting point.
     *
     * @post The shortest a-b paths as specified by Dijkstra's algorithm and
     *       their distances are available
     */
    private void processGraph(){

        //as long as there are Edges to process
        while(this.availableVertices.size() > 0){

            //pick the cheapest vertex
            Vertex next = this.availableVertices.poll();
            int distanceToNext = this.distances.get(next.getLabel());

            //and for each available neighbor of the chosen vertex
            List<Edge> nextNeighbors = next.getNeighbors();
            for(Edge e: nextNeighbors){
                Vertex other = e.getNeighbor(next);
                if(this.visitedVertices.contains(other)){
                    continue;
                }

                //we check if a shorter path exists
                //and update to indicate a new shortest found path
                //in the graph
                int currentWeight = this.distances.get(other.getLabel());
                int newWeight = distanceToNext + e.getWeight();

                if(newWeight < currentWeight){
                    this.predecessors.put(other.getLabel(), next.getLabel());
                    this.distances.put(other.getLabel(), newWeight);
                    this.availableVertices.remove(other);
                    this.availableVertices.add(other);
                }

            }

            // finally, mark the selected vertex as visited
            // so we don't revisit it
            this.visitedVertices.add(next);
        }
    }


    /**
     *
     * @param destinationLabel The Vertex whose shortest path from the initial Vertex is desired
     * @return LinkedList<Vertex> A sequence of Vertex objects starting at the
     *         initial Vertex and terminating at the Vertex specified by destinationLabel.
     *         The path is the shortest path specified by Dijkstra's algorithm.
     */
    public List<Vertex> getPathTo(String destinationLabel){
        LinkedList<Vertex> path = new LinkedList<Vertex>();
        path.add(graph.getVertex(destinationLabel));

        while(!destinationLabel.equals(this.initialVertexLabel)){
            Vertex predecessor = graph.getVertex(this.predecessors.get(destinationLabel));
            destinationLabel = predecessor.getLabel();
            path.add(0, predecessor);
        }
        return path;
    }


    /**
     *
     * @param destinationLabel The Vertex to determine the distance from the initial Vertex
     * @return int The distance from the initial Vertex to the Vertex specified by destinationLabel
     */
    public int getDistanceTo(String destinationLabel){
        return this.distances.get(destinationLabel);
    }
}
