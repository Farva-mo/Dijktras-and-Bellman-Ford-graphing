/*
Mohammad Anisi
7/20/2025
*/

import java.util.Comparator;
import java.util.HashMap;
/* A directed graph with nodes and edges. We are not going to allow
 * duplicate edges.
 */
import java.io.File;
import java.util.PriorityQueue;
import java.util.Scanner;
//to write out to a file
import java.io.PrintWriter;
import java.io.IOException;

public class Graph {
    /*
     * A node. Contains a name, a payload, and edges.
     */
    private class Node {
        public String name;
        public Object payload;

        //adding new variables
        public int distance = Integer.MAX_VALUE;
        public String parent = null;
        public boolean visited = false;

        // Map of edges, from name to weight.
        private HashMap<String, Integer> edges;
        /*
         * Connect this node to node to. Returns false if to is not in the
         * graph, or if they were already connected; returns true if they
         * are successfully connected.
         */
        public boolean connect(String to, Integer weight) {
            if (!nodes.containsKey(to)) {
                return false;
            }
            Integer result = edges.put(to, weight);
            return (result == null);
        }
        /*
         * Disconnect from node to. Returns true if it disconnects them,
         * false if they weren't connected.
         */
        public boolean disconnect(String to) {
            return (edges.remove(to) != null);
        }
        /*
         * Check whether we have an edge to node to.
         */
        public boolean connected(String to) {
            return edges.containsKey(to);
        }
        /*
         * Return the weight of the edge to node to, or null if they're
         * not connected.
         */
        public Integer edgeWeight(String to) {
            return edges.get(to);
        }
        /*
         * Change our data value.
         */
        public void changeNodeValue(Object value) {
            payload = value;
        }
        /*
         * Print our name, value and connections.
         */
        public void printNode() {
            System.out.printf("Node %s with value %s.\n", name,
                    payload.toString());
            for (String to : edges.keySet()) {
                Integer weight = edges.get(to);
                System.out.printf(" Connected to %s at weight %d.\n", to, weight);
            }
        }
        /*
         * Constructor. Needs the index name and the payload object.
         * If this is a simple graph, the payload can be null.
         */
        public Node(String newName, Object newPayload) {
            edges = new HashMap<String, Integer>();
            name = newName;
            payload = newPayload;
        }
    }
    /*
     * The collection for the nodes. We index the nodes by name.
     */
    HashMap<String, Node> nodes = new HashMap<String, Node>();
    /*
     * Check whether we have an edge between two nodes.
     */
    public boolean connected(String from, String to) {
        if (!nodes.containsKey(from) || !nodes.containsKey(to)) {
            return false;
        }
        return nodes.get(from).connected(to);
    }
    /*
     * Return the weight of the edge between two nodes, or null if they're
     * not connected.
     */
    public Integer edgeWeight(String from, String to) {
        if (!nodes.containsKey(from) || !nodes.containsKey(to)) {
            return null;
        }
        return nodes.get(from).edgeWeight(to);
    }
    /*
     * Connect nodes from and to. Returns false if either node is not in the graph,
     * or if they were already connected; returns true if they are successfully
     * connected.
     */
    public boolean connect(String from, String to, Integer weight) {
        if (!nodes.containsKey(from) || !nodes.containsKey(to)) {
            return false;
        }
        return nodes.get(from).connect(to, weight);
    }
    /*
     * Disconnect nodes from and to. Returns true if it disconnects them,
     * false if they weren't connected.
     */
    public boolean disconnect(String from, String to) {
        if (!nodes.containsKey(from) || !nodes.containsKey(to)) {
            return false;
        }
        return nodes.get(from).disconnect(to);
    }
    /*
     * Create a new node with associated data value. Returns true if the node is
     * successfully created, false if a node with that name already existed.
     */
    public boolean addNode(String name, Object value) {
        if (nodes.containsKey(name)) {
            return false;
        }
        Node n = new Node(name, value);
        nodes.put(name, n);
        return true;
    }
    // Returns true if and only if we have the named node.
    public boolean hasNode(String name) {
        return nodes.containsKey(name);
    }
    /*
     * Returns the data value of the named node, or null if there is no node
     * with that name.
     */
    public Object nodeValue(String name) {
        if (!nodes.containsKey(name)) {
            return null;
        }
        return nodes.get(name).payload;
    }
    /*
     * Change the data value of a node in a graph. Returns true if the value is
     * successfully changed, false if the node doesn't exist.
     */
    public boolean changeNodeValue(String name, Object value) {
        if (!nodes.containsKey(name)) {
            return false;
        }
        nodes.get(name).changeNodeValue(value);
        return true;
    }
    /*
     * Print the name, value and connections of a given node.
     */
    public void printNode(String name) {
        if (!nodes.containsKey(name)) {
            System.out.printf("No node named %s.\n", name);
            return;
        }
        nodes.get(name).printNode();
    }

    public static void DijkstrasAlgo(String startNode, int nodesSum, Graph g,PrintWriter wr){

        // manually set the starting node distance to 0 because it's the starting point
        g.nodes.get(startNode).distance = 0;
        g.nodes.get(startNode).parent = "-1";

        // PriorityQueue that allows me to put the smallest distance first
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(n -> n.distance));
        pq.add(g.nodes.get(startNode));

        while (!pq.isEmpty()) {

            //if visited already continue
            Node current = pq.poll();
            if (current.visited)
                continue;
            current.visited = true;

            //for loop grabbing the neighbors of the current node
            for (String neighborNode : current.edges.keySet()) {

                //pointer of the current neighbor
                Node neighbor = g.nodes.get(neighborNode);
                int weight = current.edges.get(neighborNode);
                int newDist = current.distance + weight;

                //compares distance to see if it's smaller, if so it replaces it
                if (newDist < neighbor.distance) {
                    neighbor.distance = newDist;
                    neighbor.parent = current.name;
                    pq.add(neighbor);
                }
            }
        }

        //setting distance of starting node to -1 because it required in output
        g.nodes.get(startNode).distance = -1;

        wr.println(nodesSum);
        //printing the results
        for (int i = 1; i <= nodesSum; i++) {
            Node n = g.nodes.get(String.valueOf(i));
            wr.printf("%s %s %s\n", n.name, n.distance, n.parent);
        }
    }

    //makes a directed graph become an undirected graph
   public static void addUndirectedEdge(String from, String to, int weight, Graph g) {
        g.nodes.get(from).edges.put(to, weight);
        g.nodes.get(to).edges.put(from, weight);
    }

    public static void BellmanAlgo (String StartNode, int nodesSum, Graph g,PrintWriter wr){

        //resetting nodes data
        for (int i = 0; i < nodesSum; i++) {
            String nodeName = String.valueOf(i);
            if (g.nodes.containsKey(nodeName)) {
                g.nodes.get(nodeName).distance = Integer.MAX_VALUE;
                g.nodes.get(nodeName).parent = null;
                g.nodes.get(nodeName).visited = false;
            }
        }
        // manually setting parents and distance of starting node to 0
        g.nodes.get(StartNode).distance = 0;
        g.nodes.get(StartNode).parent = "0";
        //for loop of how many iterations will happen
        for(int i=0; i< nodesSum-1;i++){

            //for loop going through all nodes
            for(String currentNodeName : g.nodes.keySet()){
                Node currentNode = g.nodes.get(currentNodeName);

                //forces to start at starting node for the first iteration
                if(currentNode.distance == Integer.MAX_VALUE)
                    continue;

                //looping through all edges of the current node
                for(String neighborName : currentNode.edges.keySet()){

                    //grab the node, and the edge.
                    Node neighborNode = g.nodes.get(neighborName);
                    int weight = currentNode.edges.get(neighborName);

                    //change distance if its less
                    if(currentNode.distance + weight < neighborNode.distance){
                        neighborNode.distance = currentNode.distance + weight;
                        neighborNode.parent = currentNode.name;
                    }
                }
            }
        }

        wr.println(nodesSum);
        //printing the results
        for (int i = 1; i <= nodesSum; i++) {
            Node n = g.nodes.get(String.valueOf(i));
            wr.printf("%s %s %s\n", n.name, n.distance, n.parent);
        }
    }


    public static void main(String[] args) throws Exception {
        // initialize graph and open file to read from
        Graph g = new Graph();
        Scanner sc = new Scanner(new File("cop3503-asn2-input.txt"));
        //cop3503-asn2-input


        //read the first three inputs
        int nodesSum = sc.nextInt();
        String startingNode = String.valueOf(sc.nextInt());
        int edgesNum = sc.nextInt();

        //creating number of nodes that'll be used
        for(int i=0; i<= nodesSum; i++){
            g.addNode(String.valueOf(i), 0);

        }

        //adding the name of the nodes with edge and to where
        for(int i=0; i<edgesNum; i++){
            String from = String.valueOf(sc.nextInt());
            int to = sc.nextInt();
            int weight = sc.nextInt();
            addUndirectedEdge(from, String.valueOf(to), weight,g);

            g.connect(String.valueOf(from), String.valueOf(to), weight);
        }

        //printing out to file
        try {
            PrintWriter wr = new PrintWriter("cop3503-asn2-output-Anisi-Mohammad.txt");

            //function 1
            wr.println("Dijkstra\n");
            DijkstrasAlgo(startingNode,nodesSum,g,wr);

            //function 2
            wr.println("\nBellman-Ford\n");
            BellmanAlgo(startingNode,nodesSum,g,wr);

            wr.close();
        }catch (IOException e){
            e.printStackTrace();
        }

        //closing file
        sc.close();

       /* g.addNode("node1", 3);
        g.addNode("node2", "three");
        g.addNode("node3", 3.0);
        g.addNode("node4", (long) 3);
        g.connect("node1", "node2", 1);
        g.connect("node2", "node3", 2);
        g.connect("node3", "node4", 3);
        g.printNode("node1");
        g.printNode("node2");
        g.printNode("node3");
        g.printNode("node4");
        System.out.printf("1 to 2? %b weight %d\n",
                g.connected("node1", "node2"),
                g.edgeWeight("node1", "node2"));
        System.out.printf("1 to 3? %b weight %d\n",
                g.connected("node1", "node3"),
                g.edgeWeight("node1", "node3"));
        System.out.printf("1 to 4? %b weight %d\n",
                g.connected("node1", "node4"),
                g.edgeWeight("node1", "node4"));
        System.out.printf("2 to 1? %b weight %d\n",
                g.connected("node2", "node1"),
                g.edgeWeight("node2", "node1"));
        System.out.printf("2 to 3? %b weight %d\n",
                g.connected("node2", "node3"),
                g.edgeWeight("node2", "node3"));
        System.out.printf("2 to 4? %b weight %d\n",
                g.connected("node2", "node4"),
                g.edgeWeight("node2", "node4"));

        */


    }
}
