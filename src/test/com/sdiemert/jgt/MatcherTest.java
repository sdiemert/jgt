package com.sdiemert.jgt;

import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.*;

public class MatcherTest {

    @Test
    public void testUnifyNodeLabels(){

        Node ng0 = new Node("A");
        Node ng1 = new Node("B");
        Node nh0 = new Node("C");
        Node nh1 = new Node("B");

        Graph g = new Graph();
        Graph h = new Graph();

        g.addNodes(ng0, ng1);
        h.addNodes(nh0, nh1);

        Matcher m = new Matcher();

        m.reset();

        m.unifyNodeLabels(g, h);

        assertEquals(3, m.nodeLabels.size());
        assertTrue(m.nodeLabels.contains("A"));
        assertTrue(m.nodeLabels.contains("B"));
        assertTrue(m.nodeLabels.contains("C"));
    }

    @Test
    public void testUnifyNodeData(){

        Node ng0 = new Node<IntNodeData>("A", new IntNodeData(0));
        Node ng1 = new Node<IntNodeData>("B", new IntNodeData(1));
        Node nh0 = new Node<IntNodeData>("C", new IntNodeData(2));
        Node nh1 = new Node<IntNodeData>("B", new IntNodeData(1));

        Graph g = new Graph();
        Graph h = new Graph();

        g.addNodes(ng0, ng1);
        h.addNodes(nh0, nh1);

        Matcher m = new Matcher();

        m.reset();

        m.unifyNodeData(g, h);

        assertEquals(3, m.nodeData.size());
        assertTrue(m.nodeData.contains(new IntNodeData(0)));
        assertTrue(m.nodeData.contains(new IntNodeData(1)));
        assertTrue(m.nodeData.contains(new IntNodeData(2)));
    }

    @Test
    public void testUnifyEdgeLabels() throws GraphException{

        Node ng0 = new Node();
        Node ng1 = new Node();
        Node nh0 = new Node();
        Node nh1 = new Node();

        Edge eg0 = new Edge(ng0, ng1, "e");
        Edge eg1 = new Edge(ng0, ng1, "f");
        Edge eh0 = new Edge(nh0, nh1, "f");

        Graph g = new Graph();
        Graph h = new Graph();

        g.addNodes(ng0, ng1);
        h.addNodes(nh0, nh1);
        g.addEdges(eg0, eg1);
        h.addEdges(eh0);

        Matcher m = new Matcher();

        m.reset();

        m.unifyEdgeLabels(g, h);

        assertEquals(2, m.edgeLabels.size());
        assertTrue(m.edgeLabels.contains("e"));
        assertTrue(m.edgeLabels.contains("f"));
    }

    @Test
    public void testDetermineBitLengths() throws GraphException{
        Node ng0 = new Node<IntNodeData>("A", new IntNodeData(0));
        Node ng1 = new Node<IntNodeData>("B", new IntNodeData(1));

        Node nh0 = new Node<IntNodeData>("A", new IntNodeData(2));
        Node nh1 = new Node<IntNodeData>("B", new IntNodeData(3));
        Node nh2 = new Node<IntNodeData>("C", new IntNodeData(4));


        Edge eh0 = new Edge(nh0, nh1, "f");

        Graph g = new Graph();
        Graph h = new Graph();

        g.addNodes(ng0, ng1);
        h.addNodes(nh0, nh1, nh2);
        h.addEdges(eh0);

        Matcher m = new Matcher();

        m.reset();

        m.unifyLabels(g, h);

        m.determineBitLengths(g, h);

        assertEquals(1, m.gNodeBVSize); // 2 nodes -> 2 indexes -> 1 bit
        assertEquals(1, m.gEdgeBVSize); // 0 edges -> 0 indexes -> 1 bit
        assertEquals(1, m.hEdgeBVSize); // 1 edge -> 1 index -> 1 bit
        assertEquals(2, m.hNodeBVSize); // 3 nodes -> 3 indexes -> 2 bits

        assertEquals(2, m.nodeLabelBVSize);
        assertEquals(1, m.edgeLabelBVSize);
        assertEquals(3, m.nodeDataBVSize);

    }

    @Test
    public void testNumberOfBits(){
        assertEquals(0, Matcher.numberOfBits(0));
        assertEquals(1, Matcher.numberOfBits(1));
        assertEquals(2, Matcher.numberOfBits(2));
        assertEquals(3, Matcher.numberOfBits(4));
        assertEquals(2, Matcher.numberOfBits(3));
        assertEquals(3, Matcher.numberOfBits(5));
        assertEquals(3, Matcher.numberOfBits(5));
    }


    @Test
    public void testFindMorphismNoMatch() throws GraphException{
        Graph g = new Graph();
        Graph h = new Graph();

        Node ng0 = new Node("A");
        Node ng1 = new Node("B");
        Edge eg0 = new Edge(ng0, ng1, "f");
        g.addNodes(ng0, ng1);
        g.addEdges(eg0);

        Node nh0 = new Node("A");
        Node nh1 = new Node("A");
        Node nh2 = new Node("A");
        Edge eh0 = new Edge(nh0, nh1, "f");
        Edge eh1 = new Edge(nh1, nh2, "e");
        Edge eh2 = new Edge(nh2, nh0, "e");
        h.addNodes(nh0, nh1, nh2);
        h.addEdges(eh0, eh1, eh2);

        Matcher m = new Matcher();

        m.reset();

        Morphism ret = m.findMorphism(g, h);

        assertNull(ret);
    }

    @Test
    public void testFindMorphism() throws GraphException{
        Graph g = new Graph();
        Graph h = new Graph();

        Node ng0 = new Node("A");
        Node ng1 = new Node("B");
        Edge eg0 = new Edge(ng0, ng1, "f");
        g.addNodes(ng0, ng1);
        g.addEdges(eg0);

        Node nh0 = new Node("A");
        Node nh1 = new Node("B");
        Node nh2 = new Node("A");
        Edge eh0 = new Edge(nh0, nh1, "f");
        Edge eh1 = new Edge(nh1, nh2, "e");
        Edge eh2 = new Edge(nh2, nh0, "e");
        h.addNodes(nh0, nh1, nh2);
        h.addEdges(eh0, eh1, eh2);

        Matcher m = new Matcher();

        m.reset();

        Morphism ret = m.findMorphism(g, h);

        assertNotNull(ret);

        assertEquals(0, ret.mapNode(0));
        assertEquals(1, ret.mapNode(1));
        assertEquals(0, ret.mapEdge(0));
    }

    @Test
    public void testFindMorphismBiggerMatchGraphNodes() throws GraphException{
        Graph g = new Graph();
        Graph h = new Graph();

        Node ng0 = new Node("A");
        Node ng1 = new Node("B");
        Edge eg0 = new Edge(ng0, ng1, "f");
        g.addNodes(ng0, ng1);
        g.addEdges(eg0);

        Node nh0 = new Node("A");
        h.addNodes(nh0);

        Matcher m = new Matcher();

        m.reset();

        Morphism ret = m.findMorphism(g, h);

        assertNull(ret);
    }

    @Test
    public void testFindMorphismBiggerMatchGraphEdges() throws GraphException{
        Graph g = new Graph();
        Graph h = new Graph();

        Node ng0 = new Node("A");
        Node ng1 = new Node("B");
        Edge eg0 = new Edge(ng0, ng1, "f");
        g.addNodes(ng0, ng1);
        g.addEdges(eg0);

        Node nh0 = new Node("A");
        Node nh1 = new Node("B");
        h.addNodes(nh0, nh1);

        Matcher m = new Matcher();

        m.reset();

        Morphism ret = m.findMorphism(g, h);

        assertNull(ret);
    }

    @Test
    public void testFindMorphismWithSingletonMatchGraph() {

        Graph g = new Graph();
        g.addNodes(new Node("A"));

        Graph h = new Graph();
        h.addNodes(new Node("B"), new Node("A"));

        Matcher m = new Matcher();

        Morphism morph = m.findMorphism(g, h);

        assertNotNull(morph);
        assertEquals(1, morph.mapNode(0));

    }

    @Test
    public void testFindMorphismOnLargerGraphs() throws GraphException{

        String[][] G = {
                {null, "A", null, null, null},
                {null, null, "A", null, null},
                {null, null, null, "A", null},
                {null, null, null, null, "A"},
                {"A", null, null, null, null},
        };

        String[] Glabels = {"n", "n", "n", "n", "n"};

        String[][] H = {
                {null, null, null, null, "A", null, null},
                {"A", null, "A", null, null, "B", null},
                {null, null, null, "A", null, null, "A"},
                {"A", null, null, null, "A", null, null},
                {null, "A", null, null, null, "A", null},
                {null, "A", "B", null, null, null, null},
                {null, "B", null, null, null, null, null},
        };

        String[] Hlabels = {"n","n","n","n","n","n","n"};

        GraphBuilder b = new GraphBuilder();

        Graph g = b.fromMatrix(G, Glabels, 5);
        Graph h = b.fromMatrix(H, Hlabels, 7);

        Matcher m = new Matcher();

        Morphism morph = m.findMorphism(g, h);

        assertNotNull(morph);
    }

}