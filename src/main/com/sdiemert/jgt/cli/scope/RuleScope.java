package com.sdiemert.jgt.cli.scope;

import com.sdiemert.jgt.core.*;

import java.util.ArrayList;

public class RuleScope extends Scope {

    Scope parent;
    String sym;

    GraphScope graphScope;
    ArrayList<Node> addNodes;
    ArrayList<Edge> addEdges;
    ArrayList<Node> delNodes;
    ArrayList<Edge> delEdges;

    public RuleScope(Scope parent, String sym){
        this.parent = parent;
        this.sym = sym;

        // defer rule creation until later....here we just build up the components we will need.

        // we maintain a graph scope here - this scope should never be accessible outside of the rule scope.
        // i.e., it should look to the user like there is no graph scope here.
        graphScope = new GraphScope(this, "ruleGraph");
        addNodes = new ArrayList<Node>();
        addEdges = new ArrayList<Edge>();
        delNodes = new ArrayList<Node>();
        delEdges = new ArrayList<Edge>();
    }

    public RuleScope(Scope parent, Rule rule){
        this.sym = rule.getId();
        this.graphScope = new GraphScope(this, rule.getRuleGraph());
        this.addNodes = rule.getAddNodes();
        this.addEdges = rule.getAddEdges();
        this.delNodes = rule.getDelNodes();
        this.delEdges = rule.getDelEdges();
        this.parent = parent;
    }

    public String scopeAsString(){
        return parent.scopeAsString() + "." + sym;
    }

    public Scope exit() throws GraphException, ScopeException {

        Rule r = new Rule(
                this.sym, this.graphScope.graph,
                this.addNodes, this.addEdges, this.delNodes, this.delEdges);

        this.parent.add(this.sym, r);

        return this.parent;
    }

    public String show(String k) throws ScopeException{
        return this.graphScope.show(k);
    }

    public String show() throws ScopeException{

        StringBuilder sb = new StringBuilder();

        sb.append(this.graphScope.show());

        sb.append("add nodes: ");
        if(this.addNodes.size() == 0){
            sb.append("0\n");
        }else{
            sb.append("\n");
            for(Node k : this.addNodes){
                sb.append("\t");
                sb.append(k.getId());
                sb.append(" ");
            }
            sb.append("\n");
        }

        sb.append("add edges: ");
        if(this.addEdges.size() == 0){
            sb.append("0\n");
        }else{
            for(Edge k : this.addEdges){
                sb.append(k.getId());
                sb.append(" ");
            }
            sb.append("\n");
        }

        sb.append("del nodes: ");
        if(this.delNodes.size() == 0){
            sb.append("0\n");
        }else{
            for(Node k : this.delNodes){
                sb.append(k.getId());
                sb.append(" ");
            }
            sb.append("\n");
        }

        sb.append("del edges: ");
        if(this.delEdges.size() == 0){
            sb.append("0\n");
        }else{
            for(Edge k : this.delEdges){
                sb.append(k.getId());
                sb.append(" ");
            }
            sb.append("\n");
        }

        return sb.toString();

    }

    public Node addNode(String sym, String label, String data, String adj) throws ScopeException {

        Node n = graphScope.addNode(sym, label, data);

        if(adj != null){
            if(adj.equals("add")){
                this.addNodes.add(n);
            }else if(adj.equals("del")){
                this.delNodes.add(n);
            }else{
                throw new ScopeException("Unknown adjective '"+adj+"' for new node command");
            }
        }
        return n;
    }

    public Edge addEdge(String sym, String src, String tar, String label, String adj) throws ScopeException, GraphException {

        Edge e = graphScope.addEdge(sym, src, tar, label);

        if(adj != null){
            if(adj.equals("add")){
                this.addEdges.add(e);
            }else if(adj.equals("del")){
                this.delEdges.add(e);
            }else{
                throw new ScopeException("Unknown adjective '"+adj+"' for new node command");
            }
        }
        return e;
    }
}
