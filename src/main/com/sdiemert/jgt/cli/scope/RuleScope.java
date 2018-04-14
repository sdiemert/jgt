package com.sdiemert.jgt.cli.scope;

import com.sdiemert.jgt.core.Edge;
import com.sdiemert.jgt.core.Graph;
import com.sdiemert.jgt.core.Node;
import com.sdiemert.jgt.core.Rule;

import java.util.ArrayList;

public class RuleScope extends Scope {

    Scope parent;
    String sym;

    Graph ruleGraph;
    ArrayList<Node> addNodes;
    ArrayList<Edge> addEdges;
    ArrayList<Node> delNodes;
    ArrayList<Edge> delEdges;

    public RuleScope(Scope parent, String sym){
        this.parent = parent;
        this.sym = sym;

        // defer rule creation until later....here we just build up the components we will need.
        ruleGraph = new Graph();
        addNodes = new ArrayList<Node>();
        addEdges = new ArrayList<Edge>();
        delNodes = new ArrayList<Node>();
        delEdges = new ArrayList<Edge>();
    }

    public String scopeAsString(){
        return parent.scopeAsString() + "." + sym;
    }

    public String show(){
        return "";
    }
}
