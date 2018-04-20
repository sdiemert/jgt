package com.sdiemert.jgt.core;

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

    public Graph(String id){
        this.nodes = new ArrayList<Node>();
        this.edges = new ArrayList<Edge>();
        this.id = id;
    }

    public Node getNode(String id){
        for(Node n : this.nodes){
            if(n.getId().equals(id)){
                return n;
            }
        }
        return null;
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

    /**
     * Deletes an edge from the graph using the Edge object as a reference.
     * @param e a reference to the Edge object to delete.
     * @return true if deleted, false otherwise (unchanged graph).
     */
    public boolean deleteEdge(Edge e){
        return this.edges.remove(e);
    }

    /**
     * Deletes an edge from the graph using the Edge's UUID as a reference.
     * @param s the UUID of the edge to delete.
     * @return true if deleted, false otherwise (unchanged graph).
     */
    public boolean deleteEdge(String s){
        for(Edge e : this.edges){
            if(e.getId().equals(s)){
                this.edges.remove(e);
                return true;
            }
        }
        return false;
    }

    /**
     * Deletes an edge from the graph using the Edge's index in the edge list.
     * @param i the index in the edge list of the graph.
     * @return true if deleted, false otherwise (unchanged graph).
     */
    protected boolean deleteEdge(int i){
        if(i >= 0 && i < this.edges.size()) {
            return this.deleteEdge(this.edges.get(i));
        }else{
            return false;
        }
    }

    /**
     * Removes a node from the graph by object reference. Any incident (source or target) edges
     * are also removed to maintain graph integrity.
     *
     * @param n a Node reference for the object to delete.
     * @return true if deleted, false otherwise (unchanged).
     */
    public boolean deleteNode(Node n){
        if(!this.nodes.contains(n)){
            return false;
        }
        for(Edge e : this.incident(n)){
            this.deleteEdge(e);
        }
        this.nodes.remove(n);
        return true;
    }

    /**
     * Removes a node from the graph by index in node list. Any incident (source or target) edges
     * are also removed to maintain graph integrity.
     *
     * @param i index of the node to delete in the node list.
     * @return true if deleted, false otherwise (unchanged).
     */
    public boolean deleteNode(int i){
        if(i >= 0 && i < this.nodes.size()){
            return this.deleteNode(this.nodes.get(i));
        }else{
            return false;
        }
    }

    /**
     * Removes a node from the graph by uuid string. Any incident (source or target) edges
     * are also removed to maintain graph integrity.
     *
     * @param id uuid of the node to delete.
     * @return true if deleted, false otherwise (unchanged).
     */
    public boolean deleteNode(String id){
        for(Node n : this.nodes){
            if(n.getId().equals(id)){
                return this.deleteNode(n);
            }
        }
        return false;
    }

    /**
     * Deletes multiple nodes from the graph. This method will silently *not*
     * delete nodes if they are not in the graph, i.e., deletes each node if it
     * exists in the graph, otherwise does not delete.
     *
     * @param N multi argument list of node references to delete.
     */
    public void deleteNodes(Node... N){
        for(Node n : N){
            this.deleteNode(n);
        }
    }

    /**
     * Finds all edges that are incident (either source or target)
     * to the given node.
     *
     * @param n the Node to find edges incident too.
     * @return a List of edges incident to the given node.
     */
    public List<Edge> incident(Node n){
        List<Edge> l = new ArrayList<Edge>();
        for(Edge e : this.edges){
            if(e.getSrc().equals(n) || e.getTar().equals(n)){
                l.add(e);
            }
        }
        return l;
    }

    /**
     * Finds all n1-(e)->n2 edges in the Graph.
     *
     * @param n1 the source node of the edge.
     * @param n2 the target node of the edge.
     * @return a List of edges between n1 and n2.
     */
    public List<Edge> adjacentList(Node n1, Node n2){
        List<Edge> l = new ArrayList<Edge>();
        for(Edge e : this.edges){
            if(e.getSrc().equals(n1) && e.getTar().equals(n2)){
                l.add(e);
            }
        }
        return l;
    }

    /**
     * Gets a list of all *unique* node labels/types in the graph.
     *
     * @return a List of unique node labels.
     */
    public List<String> getUniqueNodeLabels(){
        ArrayList<String> l = new ArrayList<String>();
        for(Node n : this.nodes){
            if(!l.contains(n.getLabel())){
                l.add(n.getLabel());
            }
        }
        return l;
    }

    /**
     * Gets a list of all *unique* edge labels/types in the graph.
     *
     * @return a List of unique edge labels.
     */
    public List<String> getUniqueEdgeLabels(){
        ArrayList<String> l = new ArrayList<String>();
        for(Edge e : this.edges){
            if(!l.contains(e.getLabel())){
                l.add(e.getLabel());
            }
        }
        return l;
    }

    public List<NodeData> getUniqueNodeData(){
        ArrayList<NodeData> l = new ArrayList<NodeData>();
        for(Node n : this.nodes){
            if(!l.contains(n.getData())){
                l.add(n.getData());
            }
        }
        return l;
    }

    /**
     * Creates a complete copy of this Graph made of new objects.
     * This may be a memory intensive operation on a large graph.
     *
     * @return a copy of this graph or null if the copy fails.
     */
    public Graph clone(){

        Graph g = new Graph();

        for(Node n : this.getNodes()){
            g.addNode(n.clone());
        }

        for(Edge e : this.getEdges()){

            try{

                g.addEdge(new Edge(
                        g.getNodes().get(this.nodes.indexOf(e.getSrc())),
                        g.getNodes().get(this.nodes.indexOf(e.getTar())),
                        e.getLabel()));

            }catch(GraphException ge){
                return null;
            }
        }

        return g;
    }

    /**
     * Creates a subgraph of the current graph. The resulting subgraph will
     * point to the same Node and Edge objects as the original graph, i.e., changes to
     * the subgraph will result in changes to the original graph.
     *
     * To make an independent subgraph invoke subgraph.clone().
     *
     * @param subNodes nodes from the original graph to keep in the subgraph.
     * @param subEdges edges from the original graph to keep in the subgraph, src and tar of these edges must be subNodes.
     *
     * @return a new Graph object that references the Node and Edge objects that comprise a subgraph of this graph.
     *
     * @throws GraphException if the subgraph cannot be generated from the provided parameters.
     */
    public Graph subgraph(List<Node> subNodes, List<Edge> subEdges) throws GraphException {

        Graph g = new Graph();

        for(Node n : subNodes){
            if(this.nodes.contains(n)){
                g.addNode(n);
            }else{
                throw new GraphException("subgraph Node is not part of main/host graph.");
            }
        }

        for(Edge e : subEdges){
            if(this.edges.contains(e)){
                g.addEdge(e);
            }else{
                throw new GraphException("subgraph Edge is not part of main/host graph.");
            }
        }

        return g;
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
