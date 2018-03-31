package com.sdiemert.jgt;

import java.util.ArrayList;
import java.util.List;

public class Graph {

    private ArrayList<Node> nodes;
    private ArrayList<Edge> edges;

    public Graph(){
        this.nodes = new ArrayList<Node>();
        this.edges = new ArrayList<Edge>();
    }

    public void addNode(Node n){
        this.nodes.add(n);
    }

    public void addEdge(Edge e){
        this.edges.add(e);
    }

    public List<Node> getNodes(){
        return this.nodes;
    }
    public List<Edge> getEdges(){
        return this.edges;
    }


}
