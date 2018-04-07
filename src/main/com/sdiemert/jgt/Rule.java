package com.sdiemert.jgt;

import java.util.ArrayList;
import java.util.List;

public class Rule {

    Graph ruleGraph;
    Graph lhsGraph;

    List<Node> addNodes;
    List<Edge> addEdges;
    List<Node> delNodes;
    List<Edge> delEdges;

    public Rule(Graph g, List<Node> addNodes, List<Edge> addEdges, List<Node> delNodes, List<Edge> delEdges) throws GraphException {

        this.ruleGraph  = g;

        this.addNodes = addNodes != null ? addNodes : new ArrayList<Node>();
        this.addEdges = addEdges != null ? addEdges : new ArrayList<Edge>();

        this.delNodes = delNodes != null ? delNodes : new ArrayList<Node>();
        this.delEdges = delEdges != null ? delEdges : new ArrayList<Edge>();

        // TODO: Implement NACs.

        // determine the LHS once, otherwise we would have to do it every time the rule is applied.
        this.determineLHS();

    }

    protected void determineLHS() throws GraphException {

        List<Node> lhsNodes = new ArrayList<Node>();
        List<Edge> lhsEdges = new ArrayList<Edge>();

        // get only those nodes that are not in the addNodes.
        for (Node n : this.ruleGraph.getNodes()) {
            if (!this.addNodes.contains(n)) {
                lhsNodes.add(n);
            }
        }

        for(Edge e : this.ruleGraph.getEdges()){
            if(!this.addEdges.contains(e)){
                lhsEdges.add(e);
            }
        }

        lhsGraph = ruleGraph.subgraph(lhsNodes, lhsEdges);
    }
}
