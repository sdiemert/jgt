package com.sdiemert.jgt;

import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class RuleTest {

    @Test
    public void testDetermineLHS() throws GraphException{
        Graph g = new Graph();

        Node n0 = new Node("n0");
        Node n1 = new Node("n1");
        Node n2 = new Node("n2");
        Edge e0 = new Edge(n0, n1, "a");
        Edge e1 = new Edge(n1, n2, "b");

        g.addNodes(n0, n1, n2);
        g.addEdges(e0, e1);

        ArrayList<Node> addNodes = new ArrayList<Node>();
        addNodes.add(n0);
        ArrayList<Edge> addEdges = new ArrayList<Edge>();
        addEdges.add(e0);

        Rule r = new Rule(g, addNodes, addEdges, null, null);

        assertNotNull(r.lhsGraph);
        assertEquals(2, r.lhsGraph.getNodes().size());
        assertEquals(1, r.lhsGraph.getEdges().size());
        assertEquals("n1", r.lhsGraph.getNodes().get(0).getLabel());
        assertEquals("n2", r.lhsGraph.getNodes().get(1).getLabel());
        assertEquals("b", r.lhsGraph.getEdges().get(0).getLabel());
    }

    @Test
    public void testDetermineLHSKeepsDelNodesAndEdges() throws GraphException{
        Graph g = new Graph();

        Node n0 = new Node("n0");
        Node n1 = new Node("n1");
        Node n2 = new Node("n2");
        Edge e0 = new Edge(n0, n1, "a");
        Edge e1 = new Edge(n1, n2, "b");

        g.addNodes(n0, n1, n2);
        g.addEdges(e0, e1);

        ArrayList<Node> delNodes = new ArrayList<Node>();
        delNodes.add(n0);
        ArrayList<Edge> delEdges = new ArrayList<Edge>();
        delEdges.add(e0);

        Rule r = new Rule(g, null, null, delNodes, delEdges);

        assertNotNull(r.lhsGraph);
        assertEquals(3, r.lhsGraph.getNodes().size());
        assertEquals(2, r.lhsGraph.getEdges().size());
    }

}