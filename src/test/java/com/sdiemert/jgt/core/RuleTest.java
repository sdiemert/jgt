package com.sdiemert.jgt.core;

import com.sdiemert.jgt.core.*;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

public class RuleTest {

    @Test
    public void testDetermineLHS() throws GraphException {
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

        assertNotNull(r.matchGraph);
        assertEquals(2, r.matchGraph.getNodes().size());
        assertEquals(1, r.matchGraph.getEdges().size());
        assertEquals("n1", r.matchGraph.getNodes().get(0).getLabel());
        assertEquals("n2", r.matchGraph.getNodes().get(1).getLabel());
        assertEquals("b", r.matchGraph.getEdges().get(0).getLabel());
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

        assertNotNull(r.matchGraph);
        assertEquals(3, r.matchGraph.getNodes().size());
        assertEquals(2, r.matchGraph.getEdges().size());
    }

    @Test
    public void testRuleApplyAdditions() throws GraphException {
        Graph ruleGraph = new Graph();
        Node rn0 = new Node("A");
        Node rn1 = new Node("B");
        Node rn2 = new Node("C");
        Edge re0 = new Edge(rn0, rn1, "e");
        Edge re1 = new Edge(rn1, rn2, "e");
        Edge re2 = new Edge(rn2, rn0, "e");
        ruleGraph.addNodes(rn0, rn1, rn2);
        ruleGraph.addEdges(re0, re1, re2);

        ArrayList<Node> addNodes = new ArrayList<Node>();
        addNodes.add(rn2);
        ArrayList<Edge> addEdges = new ArrayList<Edge>();
        addEdges.add(re1);
        addEdges.add(re2);

        Rule r = new Rule(ruleGraph, addNodes, addEdges, null, null);

        Graph host = new Graph();
        Node hn0 = new Node("A");
        Node hn1 = new Node("B");
        Edge he0 = new Edge(hn0, hn1, "e");
        host.addNodes(hn0, hn1);
        host.addEdges(he0);

        boolean ret = r.apply(host);

        assertTrue(ret);
        assertEquals(3, host.getEdges().size());
        assertEquals(3, host.getNodes().size());
        assertEquals("A", host.getNodes().get(0).getLabel());
        assertEquals("B", host.getNodes().get(1).getLabel());
        assertEquals("C", host.getNodes().get(2).getLabel());
    }

    @Test
    public void testRuleApplyDeletes() throws GraphException {
        Graph ruleGraph = new Graph();
        Node rn0 = new Node("A");
        Node rn1 = new Node("B");
        Node rn2 = new Node("C");
        Edge re0 = new Edge(rn0, rn1, "e");
        Edge re1 = new Edge(rn1, rn2, "e");
        Edge re2 = new Edge(rn2, rn0, "e");
        ruleGraph.addNodes(rn0, rn1, rn2);
        ruleGraph.addEdges(re0, re1, re2);

        ArrayList<Node> delNodes = new ArrayList<Node>();
        delNodes.add(rn2);
        ArrayList<Edge> delEdges = new ArrayList<Edge>();
        delEdges.add(re1);
        delEdges.add(re2);

        Rule r = new Rule(ruleGraph, null, null, delNodes, delEdges);

        Graph host = new Graph();
        Node hn0 = new Node("A");
        Node hn1 = new Node("B");
        Node hn2 = new Node("C");
        Edge he0 = new Edge(hn0, hn1, "e");
        Edge he1 = new Edge(hn1, hn2, "e");
        Edge he2 = new Edge(hn2, hn0, "e");
        host.addNodes(hn0, hn1, hn2);
        host.addEdges(he0, he1, he2);

        boolean ret = r.apply(host);

        assertTrue(ret);
        assertEquals(1, host.getEdges().size());
        assertEquals(2, host.getNodes().size());
        assertEquals("A", host.getNodes().get(0).getLabel());
        assertEquals("B", host.getNodes().get(1).getLabel());
    }

    @Test
    public void testRuleApplyDeletesAndAdditions() throws GraphException {
        Graph ruleGraph = new Graph();
        Node rn0 = new Node("A");
        Node rn1 = new Node("B");
        Node rn2 = new Node("C");
        Node rn3 = new Node("D");
        Edge re0 = new Edge(rn0, rn1, "e");
        Edge re1 = new Edge(rn1, rn2, "e");
        Edge re2 = new Edge(rn2, rn0, "e");
        Edge re3 = new Edge(rn1, rn3, "f");
        Edge re4 = new Edge(rn3, rn0, "f");
        ruleGraph.addNodes(rn0, rn1, rn2, rn3);
        ruleGraph.addEdges(re0, re1, re2, re3, re4);

        ArrayList<Node> delNodes = new ArrayList<Node>();
        ArrayList<Edge> delEdges = new ArrayList<Edge>();
        ArrayList<Node> addNodes = new ArrayList<Node>();
        ArrayList<Edge> addEdges = new ArrayList<Edge>();
        delNodes.add(rn2);
        delEdges.add(re1);
        delEdges.add(re2);
        addNodes.add(rn3);
        addEdges.add(re3);
        addEdges.add(re4);

        Rule r = new Rule(ruleGraph, addNodes, addEdges, delNodes, delEdges);

        Graph host = new Graph();
        Node hn0 = new Node("A");
        Node hn1 = new Node("B");
        Node hn2 = new Node("C");
        Edge he0 = new Edge(hn0, hn1, "e");
        Edge he1 = new Edge(hn1, hn2, "e");
        Edge he2 = new Edge(hn2, hn0, "e");
        host.addNodes(hn0, hn1, hn2);
        host.addEdges(he0, he1, he2);

        boolean ret = r.apply(host);

        assertTrue(ret);
        assertEquals(3, host.getEdges().size());
        assertEquals(3, host.getNodes().size());
        assertEquals("A", host.getNodes().get(0).getLabel());
        assertEquals("B", host.getNodes().get(1).getLabel());
        assertEquals("D", host.getNodes().get(2).getLabel());
        assertTrue(host.getUniqueEdgeLabels().contains("e"));
        assertTrue(host.getUniqueEdgeLabels().contains("f"));
    }

    @Test
    public void testRuleDoesNotApply() throws GraphException {
        Graph ruleGraph = new Graph();
        Node rn0 = new Node("A");
        Node rn1 = new Node("FOO");
        Edge re0 = new Edge(rn0, rn1, "e");
        ruleGraph.addNodes(rn0, rn1);
        ruleGraph.addEdges(re0);

        ArrayList<Node> delNodes = new ArrayList<Node>();
        ArrayList<Edge> delEdges = new ArrayList<Edge>();
        ArrayList<Node> addNodes = new ArrayList<Node>();
        ArrayList<Edge> addEdges = new ArrayList<Edge>();
        delNodes.add(rn1);

        Rule r = new Rule(ruleGraph, addNodes, addEdges, delNodes, delEdges);

        Graph host = new Graph();
        Node hn0 = new Node("A");
        Node hn1 = new Node("B");
        Node hn2 = new Node("C");
        Edge he0 = new Edge(hn0, hn1, "e");
        Edge he1 = new Edge(hn1, hn2, "e");
        Edge he2 = new Edge(hn2, hn0, "e");
        host.addNodes(hn0, hn1, hn2);
        host.addEdges(he0, he1, he2);

        boolean ret = r.apply(host);

        assertFalse(ret);
    }

    @Test
    public void testRuleDeletesOtherEdgesAttachedToDeleteNodes() throws GraphException {
        Graph ruleGraph = new Graph();
        Node rn0 = new Node("A");
        Node rn1 = new Node("B");
        Edge re0 = new Edge(rn0, rn1, "e");
        ruleGraph.addNodes(rn0, rn1);
        ruleGraph.addEdges(re0);

        ArrayList<Node> delNodes = new ArrayList<Node>();
        ArrayList<Edge> delEdges = new ArrayList<Edge>();
        ArrayList<Node> addNodes = new ArrayList<Node>();
        ArrayList<Edge> addEdges = new ArrayList<Edge>();
        delNodes.add(rn1);

        Rule r = new Rule(ruleGraph, addNodes, addEdges, delNodes, delEdges);

        Graph host = new Graph();
        Node hn0 = new Node("A");
        Node hn1 = new Node("B");
        Node hn2 = new Node("C");
        Edge he0 = new Edge(hn0, hn1, "e");
        Edge he1 = new Edge(hn1, hn2, "e");
        Edge he2 = new Edge(hn2, hn0, "e");
        host.addNodes(hn0, hn1, hn2);
        host.addEdges(he0, he1, he2);

        boolean ret = r.apply(host);

        assertTrue(ret);
        assertEquals(2, host.getNodes().size());
        assertEquals(1, host.getEdges().size());
    }

}