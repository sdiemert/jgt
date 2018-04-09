package com.sdiemert.jgt;

import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

public class ConditionTest {

    @Test
    public void testCondition() throws GraphException{
        Graph g = new Graph();
        Node n0 = new Node("A");
        Node n1 = new Node("B");
        Node n2 = new Node("C");
        Edge e0 = new Edge(n0, n1, "a");
        Edge e1 = new Edge(n1, n2, "b");
        g.addNodes(n0, n1, n2);
        g.addEdges(e0, e1);
        Graph cGraph = new Graph();
        Node cn = new Node("A");
        cGraph.addNodes(cn);
        Condition c = new Condition(cGraph);
        Morphism morph = c.applies(g);
        assertNotNull(morph);
    }

    @Test
    public void testConditionNoMatch() throws GraphException{
        Graph g = new Graph();
        Node n0 = new Node("A");
        Node n1 = new Node("B");
        Node n2 = new Node("C");
        Edge e0 = new Edge(n0, n1, "a");
        Edge e1 = new Edge(n1, n2, "b");
        g.addNodes(n0, n1, n2);
        g.addEdges(e0, e1);
        Graph cGraph = new Graph();
        Node cn = new Node("Z");
        cGraph.addNodes(cn);
        Condition c = new Condition(cGraph);
        Morphism morph = c.applies(g);
        assertNull(morph);
    }

}