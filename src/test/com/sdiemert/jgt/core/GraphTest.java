package com.sdiemert.jgt.core;

import com.sdiemert.jgt.core.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.*;

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
        assertNull(n.getType());
    }

    @Test
    public void testConstructNodeWithType(){
        Node n = new Node("A");
        assertNotNull(n);
        assertNotNull(n.getId());
        assertNotNull(n.getType());
        assertNull(n.getData());
    }

    @Test
    public void testConstructNodeWithData(){
        Node<IntNodeData> n = new Node<IntNodeData>("A", new IntNodeData(1));
        assertNotNull(n);
        assertNotNull(n.getId());
        assertNotNull(n.getType());
        assertNotNull(n.getData());
        assertEquals(1, n.getData().getVal());
        assertEquals("A", n.getType());
    }

    @Test
    public void testConstructEdge(){
        Node n1 = new Node();
        Node n2 = new Node();
        Edge e = new Edge(n1, n2);
        assertNotNull(e);
        assertEquals(n1.getId(), e.getSrc().getId());
        assertEquals(n2.getId(), e.getTar().getId());
        assertNotNull(e.getLabel());
        assertEquals("NONE", e.getLabel());
    }

    @Test
    public void testConstructEdgeWithLabel(){
        Node n1 = new Node();
        Node n2 = new Node();
        Edge e = new Edge(n1, n2, "foo");
        assertNotNull(e);
        assertEquals(n1.getId(), e.getSrc().getId());
        assertEquals(n2.getId(), e.getTar().getId());
        assertNotNull(e.getLabel());
        assertEquals("foo", e.getLabel());
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

    @Test
    public void testAdjacentNormal() throws GraphException{
        Node n0 = new Node();
        Node n1 = new Node();
        Node n2 = new Node();
        Edge e0 = new Edge(n0, n1);
        Edge e1 = new Edge(n1, n2);

        Graph g = new Graph();

        g.addNodes(n0, n1, n2);
        g.addEdges(e0, e1);

        assertEquals(3, g.getNodes().size());
        assertEquals(2, g.getEdges().size());

        List<Edge> ret = g.adjacentList(n0, n1);

        assertEquals(1, ret.size());
        assertEquals(e0.getId(), ret.get(0).getId());

        assertEquals(3, g.getNodes().size());
        assertEquals(2, g.getEdges().size());

    }

    @Test
    public void testAdjacentWithDoubleEdge() throws GraphException{
        Node n0 = new Node();
        Node n1 = new Node();
        Node n2 = new Node();
        Edge e0 = new Edge(n0, n1);
        Edge e1 = new Edge(n0, n1);

        Graph g = new Graph();

        g.addNodes(n0, n1, n2);
        g.addEdges(e0, e1);

        assertEquals(3, g.getNodes().size());
        assertEquals(2, g.getEdges().size());

        List<Edge> ret = g.adjacentList(n0, n1);

        assertEquals(2, ret.size());
        assertEquals(e0.getId(), ret.get(0).getId());
        assertEquals(e1.getId(), ret.get(1).getId());

        assertEquals(3, g.getNodes().size());
        assertEquals(2, g.getEdges().size());

    }

    @Test
    public void testAdjacentWithNoEdge() throws GraphException{
        Node n0 = new Node();
        Node n1 = new Node();
        Node n2 = new Node();
        Edge e0 = new Edge(n0, n1);
        Edge e1 = new Edge(n0, n1);

        Graph g = new Graph();

        g.addNodes(n0, n1, n2);
        g.addEdges(e0, e1);

        assertEquals(3, g.getNodes().size());
        assertEquals(2, g.getEdges().size());

        List<Edge> ret = g.adjacentList(n1, n2);

        assertEquals(0, ret.size());

        assertEquals(3, g.getNodes().size());
        assertEquals(2, g.getEdges().size());

    }

    @Test
    public void testIncidentNormal() throws GraphException{
        Node n0 = new Node();
        Node n1 = new Node();
        Node n2 = new Node();
        Node n3 = new Node();
        Edge e0 = new Edge(n0, n1);
        Edge e1 = new Edge(n1, n2);
        Graph g = new Graph();
        g.addNodes(n0, n1, n2, n3);
        g.addEdges(e0, e1);

        assertEquals(4, g.getNodes().size());
        assertEquals(2, g.getEdges().size());

        assertEquals(1, g.incident(n0).size());
        assertEquals(2, g.incident(n1).size());
        assertEquals(1, g.incident(n2).size());
        assertEquals(0, g.incident(n3).size());

        assertEquals(4, g.getNodes().size());
        assertEquals(2, g.getEdges().size());
    }


    @Test
    public void testDeleteNodeNormal() throws GraphException{

        Node n0 = new Node();
        Node n1 = new Node();
        Node n2 = new Node();
        Node n3 = new Node();
        Node n4 = new Node();
        Edge e0 = new Edge(n0, n1);
        Edge e1 = new Edge(n1, n2);
        Edge e2 = new Edge(n2, n3);
        Graph g = new Graph();
        g.addNodes(n0, n1, n2, n3, n4);
        g.addEdges(e0, e1, e2);

        assertEquals(5, g.getNodes().size());
        assertEquals(3, g.getEdges().size());

        g.deleteNode(n4);

        assertEquals(4, g.getNodes().size());
        assertEquals(3, g.getEdges().size());

        g.deleteNode(n1);

        assertEquals(3, g.getNodes().size());
        assertEquals(1, g.getEdges().size());

        g.deleteNode(n3);

        assertEquals(2, g.getNodes().size());
        assertEquals(0, g.getEdges().size());

        g.deleteNodes(n0, n2);

        assertEquals(0, g.getNodes().size());
        assertEquals(0, g.getEdges().size());
    }

    @Test
    public void testDeleteNodeWithValidUUID(){
        Node n0 = new Node();
        Graph g = new Graph();
        g.addNode(n0);
        assertEquals(1, g.getNodes().size());
        assertTrue(g.deleteNode(n0.getId()));
        assertEquals(0, g.getNodes().size());
    }

    @Test
    public void testDeleteNodeWithInvalidUUID(){
        Node n0 = new Node();
        Node n1 = new Node();
        Graph g = new Graph();
        g.addNode(n0);
        assertEquals(1, g.getNodes().size());
        assertFalse(g.deleteNode(n1.getId()));
        assertEquals(1, g.getNodes().size());
    }

    @Test
    public void testDeleteNodeWithValidIndex(){
        Node n0 = new Node();
        Graph g = new Graph();
        g.addNode(n0);
        assertEquals(1, g.getNodes().size());
        assertTrue(g.deleteNode(0));
        assertEquals(0, g.getNodes().size());
    }

    @Test
    public void testDeleteNodeWithInvalidIndex(){
        Node n0 = new Node();
        Graph g = new Graph();
        g.addNode(n0);
        assertEquals(1, g.getNodes().size());
        assertFalse(g.deleteNode(1));
        assertFalse(g.deleteNode(-1));
        assertEquals(1, g.getNodes().size());
    }

    @Test
    public void testDeleteNodeInvalidNode(){
        Node n0 = new Node();
        Node n1 = new Node();
        Graph g = new Graph();
        g.addNode(n0);
        assertFalse(g.deleteNode(n1));
    }

    @Test
    public void testGetUniqueNodeLabelsShouldIncludeAllUniqueLabels(){

        Node n0 = new Node("A");
        Node n1 = new Node("B");
        Graph g = new Graph();
        g.addNodes(n0, n1);

        List<String> l = g.getUniqueNodeLabels();

        assertNotNull(l);
        assertEquals(2, l.size());
        assertTrue(l.contains("A"));
        assertTrue(l.contains("B"));
    }

    @Test
    public void testGetUniqueNodeLabelsShouldNotIncludeDuplicates(){

        Node n0 = new Node("A");
        Node n1 = new Node("A");
        Graph g = new Graph();
        g.addNodes(n0, n1);

        List<String> l = g.getUniqueNodeLabels();

        assertNotNull(l);
        assertEquals(1, l.size());
        assertTrue(l.contains("A"));
    }

    @Test
    public void testGetUniqueEdgeLabelsShouldIncludeAllUniqueLabels() throws GraphException{
        Node n0 = new Node();
        Node n1 = new Node();
        Node n2 = new Node();
        Edge e0 = new Edge(n0, n1, "e0");
        Edge e1 = new Edge(n1, n2, "e1");
        Graph g =  new Graph();

        g.addNodes(n0, n1, n2);
        g.addEdges(e0, e1);

        List<String> l = g.getUniqueEdgeLabels();

        assertNotNull(l);
        assertEquals(2, l.size());
        assertTrue(l.contains("e0"));
        assertTrue(l.contains("e1"));
    }

    @Test
    public void testGetUniqueEdgeLabelsShouldNotIncludeDuplicates() throws GraphException{
        Node n0 = new Node();
        Node n1 = new Node();
        Node n2 = new Node();
        Edge e0 = new Edge(n0, n1, "e0");
        Edge e1 = new Edge(n1, n2, "e0");
        Graph g =  new Graph();

        g.addNodes(n0, n1, n2);
        g.addEdges(e0, e1);

        List<String> l = g.getUniqueEdgeLabels();

        assertNotNull(l);
        assertEquals(1, l.size());
        assertTrue(l.contains("e0"));
    }

    @Test
    public void testGetUniqueNodeDataShouldIncludeAllUniqueVals(){
        Node n0 = new Node<IntNodeData>("A", new IntNodeData(0));
        Node n1 = new Node<IntNodeData>("A", new IntNodeData(1));
        Graph g = new Graph();
        g.addNodes(n0, n1);
        List<NodeData> l = g.getUniqueNodeData();
        assertNotNull(l);
        assertEquals(2, l.size());
        assertTrue(l.contains(new IntNodeData(0)));
        assertTrue(l.contains(new IntNodeData(1)));
    }

    @Test
    public void testGetUniqueNodeDataShouldNotHaveDuplicates(){
        Node n0 = new Node<IntNodeData>("A", new IntNodeData(0));
        Node n1 = new Node<IntNodeData>("A", new IntNodeData(0));
        Graph g = new Graph();
        g.addNodes(n0, n1);
        List<NodeData> l = g.getUniqueNodeData();
        assertNotNull(l);
        assertEquals(1, l.size());
        assertTrue(l.contains(new IntNodeData(0)));
    }

    @Test
    public void testGraphClone() throws GraphException {
        Node n0 = new Node("n0");
        Node n1 = new Node<IntNodeData>("n1", new IntNodeData(1));
        Node n2 = new Node();
        Edge e0 = new Edge(n0, n1, "e0");
        Edge e1 = new Edge(n0, n1);
        Graph g = new Graph();
        g.addNodes(n0, n1, n2);
        g.addEdges(e0, e1);

        Graph gp = g.clone();

        assertNotNull(gp);
        assertEquals(3, gp.getNodes().size());
        assertEquals(2, gp.getEdges().size());

        // make sure the id's are unique
        assertNotEquals(gp.getNodes().get(0).getId(), n0.getId());
        assertNotEquals(gp.getNodes().get(0).getId(), n1.getId());
        assertNotEquals(gp.getNodes().get(0).getId(), n2.getId());
        assertNotEquals(gp.getEdges().get(0).getId(), e0.getId());
        assertNotEquals(gp.getEdges().get(0).getId(), e1.getId());

        // should preserve ordering of nodes in lists.
        assertEquals(gp.getNodes().get(0).getLabel(), g.getNodes().get(0).getLabel());
        assertEquals(gp.getNodes().get(1).getLabel(), g.getNodes().get(1).getLabel());
        assertEquals(gp.getNodes().get(2).getLabel(), g.getNodes().get(2).getLabel());
        assertEquals(gp.getEdges().get(0).getLabel(), g.getEdges().get(0).getLabel());
        assertEquals(gp.getEdges().get(1).getLabel(), g.getEdges().get(1).getLabel());
    }

    @Test(expected = GraphException.class)
    public void testSubgraphBadNode() throws GraphException {

        Node n0 = new Node("n0");
        Node n1 = new Node("n1");
        Node n2 = new Node("n2");
        Node n3 = new Node("n3");
        Edge e0 = new Edge(n0, n1, "e0");
        Edge e1 = new Edge(n1, n2, "e1");

        Graph g = new Graph();
        g.addNodes(n0, n1, n2);
        g.addEdges(e0, e1);

        List<Node> subNodes = new ArrayList<Node>();
        subNodes.add(n0);
        subNodes.add(n1);
        subNodes.add(n3);

        List<Edge> subEdges = new ArrayList<Edge>();
        subEdges.add(e0);


        Graph s = g.subgraph(subNodes, subEdges);
    }

    @Test(expected = GraphException.class)
    public void testSubgraphBadEdge() throws GraphException {

        Node n0 = new Node("n0");
        Node n1 = new Node("n1");
        Node n2 = new Node("n2");
        Edge e0 = new Edge(n0, n1, "e0");
        Edge e1 = new Edge(n1, n2, "e1");

        Graph g = new Graph();
        g.addNodes(n0, n1, n2);
        g.addEdges(e0, e1);

        List<Node> subNodes = new ArrayList<Node>();
        subNodes.add(n0);
        subNodes.add(n1);

        List<Edge> subEdges = new ArrayList<Edge>();
        subEdges.add(e0);
        subEdges.add(e1);

        Graph s = g.subgraph(subNodes, subEdges);
    }

    @Test
    public void testSubgraph() throws GraphException {

        Node n0 = new Node("n0");
        Node n1 = new Node("n1");
        Node n2 = new Node("n2");
        Edge e0 = new Edge(n0, n1, "e0");
        Edge e1 = new Edge(n1, n2, "e1");

        Graph g = new Graph();
        g.addNodes(n0, n1, n2);
        g.addEdges(e0, e1);

        List<Node> subNodes = new ArrayList<Node>();
        subNodes.add(n0);
        subNodes.add(n1);

        List<Edge> subEdges = new ArrayList<Edge>();
        subEdges.add(e0);


        Graph s = g.subgraph(subNodes, subEdges);

        assertNotNull(s);
        assertEquals(2, s.getNodes().size());
        assertEquals(1, s.getEdges().size());
        assertEquals(g.getNodes().get(0), s.getNodes().get(0));
        assertEquals(g.getNodes().get(1), s.getNodes().get(1));
        assertEquals(g.getEdges().get(0), s.getEdges().get(0));

        assertTrue(g.getNodes().get(0) == s.getNodes().get(0));
    }

    @Test(expected = GraphException.class)
    public void testSubgraphEdgeNotInGraph() throws GraphException {

        Node n0 = new Node("n0");
        Node n1 = new Node("n1");
        Node n2 = new Node("n2");
        Edge e0 = new Edge(n0, n1, "e0");
        Edge e1 = new Edge(n1, n2, "e1");

        Edge e2 = new Edge(n0, n1);

        Graph g = new Graph();
        g.addNodes(n0, n1, n2);
        g.addEdges(e0, e1);

        List<Node> subNodes = new ArrayList<Node>();
        subNodes.add(n0);
        subNodes.add(n1);

        List<Edge> subEdges = new ArrayList<Edge>();
        subEdges.add(e0);
        subEdges.add(e2);

        Graph s = g.subgraph(subNodes, subEdges);
    }

}