package com.sdiemert.jgt;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Graph {

    private ArrayList<Node> nodes;
    private ArrayList<Edge> edges;
    private String id;

    public Graph(){
        this.nodes = new ArrayList<Node>();
        this.edges = new ArrayList<Edge>();
        this.id = "graph-"+UUID.randomUUID().toString();
    }

    /**
     * Adds a new node to the graph.
     *
     * @param n the node object to add.
     */
    public void addNode(Node n){
        this.nodes.add(n);
    }

    /**
     * Adds multiple new Nodes to the graph.
     *
     * @param N variable number of Node objects to add to the graph.
     */
    public void addNodes(Node... N){
        for(Node n : N){
            this.addNode(n);
        }
    }

    /**
     * Adds a new edge object to the graph. The source
     * and target nodes of hte edge must already exist
     * in the nodes list.
     *
     * @param e the Edge to add to the graph
     *
     * @throws GraphException if edge src and tar are not already in the graph.
     */
    public void addEdge(Edge e) throws GraphException{

        if(this.nodes.contains(e.getSrc()) && this.nodes.contains(e.getTar())){
            this.edges.add(e);
        }else{
            throw new GraphException("src and tar nodes must both be in graph prior to adding edge");
        }
    }

    /**
     * Adds multiple edge objects to the graph.
     *
     * @param E variable number of Edge objects to add to the graph.
     * @throws GraphException if an edge's src and tar are not already in the graph.
     */
    public void addEdges(Edge... E) throws GraphException{
        for(Edge e : E){
            this.addEdge(e);
        }
    }

    // ---------- GETTERS AND SETTERS ----------

    public List<Node> getNodes(){
        return this.nodes;
    }

    public List<Edge> getEdges(){
        return this.edges;
    }

    public String getId() {
        return id;
    }
}
