package com.sdiemert.jgt.cli.scope;

import com.sdiemert.jgt.core.*;

import java.util.HashMap;

public class GraphScope extends Scope {

    String sym;
    Scope parent;

    Graph graph;

    HashMap<String, Node> scopeNodes;
    HashMap<String, Edge> scopeEdges;

    public GraphScope(Scope parent, String sym){
        this.sym = sym;
        this.parent = parent;
        this.graph = new Graph();
        this.scopeNodes = new HashMap<String, Node>();
        this.scopeEdges = new HashMap<String, Edge>();
    }

    public String scopeAsString(){
        return sym;
    }

    public void addNode(String sym, Node n){
        this.graph.addNode(n);
        this.scopeNodes.put(sym, n);
    }

    public void addNode(String sym, String label, String data){

        Node n;

        if(data != null){
            try{

                IntNodeData d = new IntNodeData(Integer.parseInt(data));
                n = new Node<IntNodeData>(label, d);

            }catch(NumberFormatException e){
                StringNodeData d = new StringNodeData(data);
                n = new Node<StringNodeData>(label, d);
            }
        }else{
             n = new Node(label);
        }

        this.addNode(sym, n);

    }

    public void addEdge(String sym, Edge e) throws GraphException {
        this.graph.addEdge(e);
        this.scopeEdges.put(sym, e);
    }

    public void addEdge(String sym, String src, String tar, String label) throws GraphException{

        if(!this.scopeNodes.containsKey(src) || !this.scopeNodes.containsKey(tar)){
            throw new GraphException("Failed to add edge, both source and target must already exist.");
        }

        this.addEdge(sym, new Edge(this.scopeNodes.get(src), this.scopeNodes.get(tar), label));

    }

    public String show(){

        StringBuilder sb = new StringBuilder();

        sb.append("Nodes: ");

        if(this.scopeNodes.size() > 0) {
            sb.append("\n");
            for (String k : this.scopeNodes.keySet()) {
                sb.append("\t");
                sb.append(k);
                sb.append(" : ");
                sb.append("Node ");
                sb.append(scopeNodes.get(k).getLabel());
                if (scopeNodes.get(k).getData() != null) {
                    sb.append(" ");
                    sb.append(scopeNodes.get(k).getData());
                }
                sb.append("\n");
            }
        }else{
            sb.append("None\n");
        }

        sb.append("Edges: ");

        if(this.scopeEdges.size() > 0) {
            sb.append("\n");
            for (String k : this.scopeEdges.keySet()) {
                sb.append("\t");
                sb.append(k);
                sb.append(" : ");
                sb.append("Edge ");
                sb.append(getSymByNode(scopeEdges.get(k).getSrc()));
                sb.append(" ");
                sb.append(getSymByNode(scopeEdges.get(k).getTar()));
                sb.append(" ");
                sb.append(scopeEdges.get(k).getLabel());
                sb.append("\n");
            }
        }else{
            sb.append("None\n");
        }

        return sb.toString();

    }

    private String getSymByNode(Node n){
        for(String k : this.scopeNodes.keySet()){
            if(this.scopeNodes.get(k) == n){
                return k;
            }
        }
        return null;
    }

    public Scope exit() throws ScopeException{
        this.parent.add(sym, this.graph);
        return this.parent;
    }

}
