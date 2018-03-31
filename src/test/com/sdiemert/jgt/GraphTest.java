package com.sdiemert.jgt;

import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GraphTest {

    @Test
    public void testConstructShouldProduceEmptyGraph(){
        Graph g = new Graph();
        assertEquals(0, g.getNodes().size());
        assertEquals(0, g.getEdges().size());
        assertNotNull(g);
        assertNotNull(g.getId());
    }

    @Test
    public void testConstructNode(){
        Node n = new Node();
        assertNotNull(n);
        assertNotNull(n.getId());
    }

    @Test
    public void testConstructEdge(){
        Node n1 = new Node();
        Node n2 = new Node();
        Edge e = new Edge(n1, n2);
        assertNotNull(e);
        assertEquals(n1.getId(), e.getSrc().getId());
        assertEquals(n2.getId(), e.getTar().getId());
    }

    @Test
    public void testAddNode(){
        Node n1 = new Node();
        Graph g = new Graph();
        g.addNode(n1);
        assertEquals(1, g.getNodes().size());
        assertEquals(n1.getId(), g.getNodes().get(0).getId());
        assertEquals(0, g.getEdges().size());
    }

    @Test
    public void testAddNodes(){
        Node n1 = new Node();
        Node n2 = new Node();
        Node n3 = new Node();
        Graph g = new Graph();

        g.addNodes(n1, n2, n3);

        assertEquals(3, g.getNodes().size());
        assertEquals(n1.getId(), g.getNodes().get(0).getId());
        assertEquals(n2.getId(), g.getNodes().get(1).getId());
        assertEquals(n3.getId(), g.getNodes().get(2).getId());
        assertEquals(0, g.getEdges().size());
    }

    @Test
    public void testAddNodesNoNodes(){
        Graph g = new Graph();
        g.addNodes();
        assertEquals(0, g.getNodes().size());
        assertEquals(0, g.getEdges().size());
    }

    @Test
    public void testAddEdgeNormal() throws GraphException {
        Node n1 = new Node();
        Node n2 = new Node();
        Edge e = new Edge(n1, n2);
        Graph g = new Graph();
        g.addNodes(n1, n2);
        g.addEdge(e);
        assertEquals(1, g.getEdges().size());
        assertEquals(2, g.getNodes().size());
        assertEquals(e.getId(), g.getEdges().get(0).getId());
    }

    @Test
    public void testAddEdges() throws GraphException{
        Node n1 = new Node();
        Node n2 = new Node();
        Edge e0 = new Edge(n1, n2);
        Edge e1 = new Edge(n1, n2);
        Graph g = new Graph();
        g.addNodes(n1, n2);
        g.addEdges(e0, e1);

        assertEquals(2, g.getEdges().size());
        assertEquals(e0.getId(), g.getEdges().get(0).getId());
        assertEquals(e1.getId(), g.getEdges().get(1).getId());
    }

    @Test(expected=GraphException.class)
    public void testAddEdgeNoNodes() throws GraphException{
        Edge e = new Edge(new Node(), new Node());
        Graph g = new Graph();
        g.addEdge(e);
    }

    @Test(expected=GraphException.class)
    public void testAddEdgeOnlyValidSrc() throws GraphException{
        Node n1 = new Node();
        Edge e = new Edge(n1, new Node());
        Graph g = new Graph();
        g.addNodes(n1);
        g.addEdge(e);
    }

    @Test(expected=GraphException.class)
    public void testAddEdgeOnlyValidTar() throws GraphException{
        Node n2 = new Node();
        Edge e = new Edge(new Node(), n2);
        Graph g = new Graph();
        g.addNodes(n2);
        g.addEdge(e);
    }

    @Test
    public void testDeleteEdgeByObject() throws GraphException{
        Node n1 = new Node();
        Node n2 = new Node();
        Edge e = new Edge(n1, n2);
        Graph g = new Graph();

        g.addNodes(n1, n2);
        g.addEdge(e);

        assertEquals(1, g.getEdges().size());
        assertEquals(2, g.getNodes().size());

        assertTrue(g.deleteEdge(e));

        assertEquals(0, g.getEdges().size());
        assertEquals(2, g.getNodes().size());
    }

    @Test
    public void testDeleteEdgeByIndex() throws GraphException{
        Node n1 = new Node();
        Node n2 = new Node();
        Edge e = new Edge(n1, n2);
        Graph g = new Graph();

        g.addNodes(n1, n2);
        g.addEdge(e);

        assertEquals(1, g.getEdges().size());
        assertEquals(2, g.getNodes().size());

        assertTrue(g.deleteEdge(0));

        assertEquals(0, g.getEdges().size());
        assertEquals(2, g.getNodes().size());
    }

    @Test
    public void testDeleteEdgeByUUID() throws GraphException{
        Node n1 = new Node();
        Node n2 = new Node();
        Edge e = new Edge(n1, n2);
        Graph g = new Graph();

        g.addNodes(n1, n2);
        g.addEdge(e);

        assertEquals(1, g.getEdges().size());
        assertEquals(2, g.getNodes().size());

        assertTrue(g.deleteEdge(e.getId()));

        assertEquals(0, g.getEdges().size());
        assertEquals(2, g.getNodes().size());
    }

    @Test
    public void testDeleteEdgeWithoutValidEdge() throws GraphException{
        Node n1 = new Node();
        Node n2 = new Node();
        Edge e = new Edge(n1, n2);
        Edge e2 = new Edge(n1, n2);
        Graph g = new Graph();

        g.addNodes(n1, n2);
        g.addEdge(e);

        assertEquals(1, g.getEdges().size());
        assertEquals(2, g.getNodes().size());

        assertFalse(g.deleteEdge(e2));

        // Unchanged graph.
        assertEquals(1, g.getEdges().size());
        assertEquals(2, g.getNodes().size());
    }

    @Test
    public void testDeleteEdgeWithoutValidEdgeUUID() throws GraphException{
        Node n1 = new Node();
        Node n2 = new Node();
        Edge e = new Edge(n1, n2);
        Edge e2 = new Edge(n1, n2);
        Graph g = new Graph();

        g.addNodes(n1, n2);
        g.addEdge(e);

        assertEquals(1, g.getEdges().size());
        assertEquals(2, g.getNodes().size());

        assertFalse(g.deleteEdge(e2.getId()));

        // Unchanged graph.
        assertEquals(1, g.getEdges().size());
        assertEquals(2, g.getNodes().size());
    }

    @Test
    public void testDeleteEdgeWithoutValidIndex() throws GraphException{
        Node n1 = new Node();
        Node n2 = new Node();
        Edge e = new Edge(n1, n2);
        Graph g = new Graph();

        g.addNodes(n1, n2);
        g.addEdge(e);

        assertEquals(1, g.getEdges().size());
        assertEquals(2, g.getNodes().size());

        assertFalse(g.deleteEdge(1));
        assertFalse(g.deleteEdge(-1));

        // Unchanged graph.
        assertEquals(1, g.getEdges().size());
        assertEquals(2, g.getNodes().size());
    }

}