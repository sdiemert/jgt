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
        this.graph = new Graph(sym);
        this.scopeNodes = new HashMap<String, Node>();
        this.scopeEdges = new HashMap<String, Edge>();
    }

    public GraphScope(Scope parent, Graph g){
        this.sym = g.getId();
        this.parent = parent;
        this.graph = g;
        this.scopeNodes = new HashMap<String, Node>();
        this.scopeEdges = new HashMap<String, Edge>();

        for(Node n : g.getNodes()){
            scopeNodes.put(n.getId(), n);
        }

        for(Edge e : g.getEdges()){
            scopeEdges.put(e.getId(), e);
        }
    }

    public String scopeAsString(){
        return sym;
    }

    public Node addNode(String sym, Node n){
        this.graph.addNode(n);
        this.scopeNodes.put(sym, n);
        return n;
    }

    public Node addNode(String sym, String label, String data){

        Node n;

        if(data != null){
            try{

                IntNodeData d = new IntNodeData(Integer.parseInt(data));
                n = new Node<IntNodeData>(sym, label, d);

            }catch(NumberFormatException e){
                StringNodeData d = new StringNodeData(data);
                n = new Node<StringNodeData>(sym, label, d);
            }
        }else{
             n = new Node(sym, label);
        }

        return this.addNode(sym, n);

    }

    public Edge addEdge(String sym, Edge e) throws GraphException {
        this.graph.addEdge(e);
        this.scopeEdges.put(sym, e);
        return e;
    }

    public Edge addEdge(String sym, String src, String tar, String label) throws GraphException{

        if(!this.scopeNodes.containsKey(src) || !this.scopeNodes.containsKey(tar)){
            throw new GraphException("Failed to add edge, both source and target must already exist.");
        }

        return this.addEdge(sym, new Edge(sym, this.scopeNodes.get(src), this.scopeNodes.get(tar), label));

    }

    public String show(String k) throws ScopeException{

        if(this.getScopeEdges().containsKey(k)){
            Edge e = this.getScopeEdges().get(k);
            return e.getId()+" : Edge "+e.getSrc().getId()+" "+e.getTar().getId()+" "+e.getLabel();
        }else if(this.getScopeNodes().containsKey(k)){
            Node n = this.getScopeNodes().get(k);
            String s = n.getId() + " : Node "+n.getLabel();
            if(n.getData() != null){
                s += " "+n.getData().toString();
            }
            return s;
        }else{
            throw new ScopeException("Cannot find '"+k+"'");
        }

    }

    public String show() throws ScopeException {

        StringBuilder sb = new StringBuilder();

        sb.append("Nodes: ");

        if(this.scopeNodes.size() > 0) {
            sb.append("\n");
            for (String k : this.scopeNodes.keySet()) {
                sb.append("\t");
                sb.append(this.show(k));
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
                sb.append(this.show(k));
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

    public Graph getGraph() {
        return graph;
    }

    public HashMap<String, Node> getScopeNodes() {
        return scopeNodes;
    }

    public HashMap<String, Edge> getScopeEdges() {
        return scopeEdges;
    }
}
