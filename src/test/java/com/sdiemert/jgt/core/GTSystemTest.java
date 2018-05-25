package com.sdiemert.jgt.core;

import com.sdiemert.jgt.core.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

public class GTSystemTest {

    @Test
    public void testCandidatesWithMultipleMatches() throws GraphException {

        Node hn0 = new Node("A");
        Node hn1 = new Node("B");
        Node hn2 = new Node("C");
        Edge he0 = new Edge(hn0, hn1, "e");
        Edge he1 = new Edge(hn0, hn1, "e");
        Edge he2 = new Edge(hn0, hn1, "e");
        Graph host = new Graph();
        host.addNodes(hn0, hn1, hn2);
        host.addEdges(he0, he1, he2);

        Node r1n0 = new Node("A");
        Graph r1Graph = new Graph();
        r1Graph.addNode(r1n0);
        Rule r1 = new Rule(r1Graph, null, null, null, null);

        Node r2n0 = new Node("A");
        Node r2n1 = new Node("B");
        Edge r2e0 = new Edge(r2n0, r2n1, "e");
        Graph r2Graph = new Graph();
        r2Graph.addNodes(r2n0, r2n1);
        r2Graph.addEdges(r2e0);
        Rule r2 = new Rule(r1Graph, null, null, null, null);

        GTSystem gts = new GTSystem();
        gts.addRule(r1);
        gts.addRule(r2);

        assertEquals(3, host.getNodes().size());
        assertEquals(3, host.getEdges().size());

        HashMap<Rule, Morphism> map = gts.candidates(host);

        assertNotNull(map);
        assertEquals(2, map.keySet().size());

        assertNotNull(map.get(r1));
        assertNotNull(map.get(r2));

        // host should not have changed.
        assertEquals(3, host.getNodes().size());
        assertEquals(3, host.getEdges().size());

    }


    @Test
    public void testCandidatesWithNoMatches() throws GraphException{

        Node hn0 = new Node("A");
        Node hn1 = new Node("B");
        Node hn2 = new Node("C");
        Edge he0 = new Edge(hn0, hn1, "e");
        Edge he1 = new Edge(hn0, hn1, "e");
        Edge he2 = new Edge(hn0, hn1, "e");
        Graph host = new Graph();
        host.addNodes(hn0, hn1, hn2);
        host.addEdges(he0, he1, he2);

        Node r1n0 = new Node("Z");
        Graph r1Graph = new Graph();
        r1Graph.addNode(r1n0);
        Rule r1 = new Rule(r1Graph, null, null, null, null);

        Node r2n0 = new Node("X");
        Node r2n1 = new Node("Y");
        Edge r2e0 = new Edge(r2n0, r2n1, "e");
        Graph r2Graph = new Graph();
        r2Graph.addNodes(r2n0, r2n1);
        r2Graph.addEdges(r2e0);
        Rule r2 = new Rule(r1Graph, null, null, null, null);

        GTSystem gts = new GTSystem();
        gts.addRule(r1);
        gts.addRule(r2);

        HashMap<Rule, Morphism> map = gts.candidates(host);

        assertNotNull(map);
        assertEquals(0, map.size());

    }

    @Test
    public void testApply() throws GraphException {

        Node hn0 = new Node("A");
        Node hn1 = new Node("B");
        Node hn2 = new Node("C");
        Edge he0 = new Edge(hn0, hn1, "e");
        Edge he1 = new Edge(hn0, hn1, "e");
        Edge he2 = new Edge(hn0, hn1, "e");
        Graph host = new Graph();
        host.addNodes(hn0, hn1, hn2);
        host.addEdges(he0, he1, he2);

        Node r1n0 = new Node("A");
        Graph r1Graph = new Graph();
        r1Graph.addNode(r1n0);
        ArrayList<Node> r1DelNodes = new ArrayList<Node>();
        r1DelNodes.add(r1n0);
        Rule r1 = new Rule(r1Graph, null, null, r1DelNodes, null);

        Node r2n0 = new Node("A");
        Node r2n1 = new Node("B");
        Edge r2e0 = new Edge(r2n0, r2n1, "e");
        Graph r2Graph = new Graph();
        r2Graph.addNodes(r2n0, r2n1);
        r2Graph.addEdges(r2e0);
        ArrayList<Node> r2DelNodes = new ArrayList<Node>();
        r2DelNodes.add(r2n0);
        r2DelNodes.add(r2n1);
        Rule r2 = new Rule(r2Graph, null, null, r2DelNodes, null);

        GTSystem gts = new GTSystem();
        gts.addRule(r1);
        gts.addRule(r2);

        RuleApplication app = gts.apply(host);

        assertNotNull(app);
        assertTrue(host.getNodes().size() < 3);
        assertTrue(host.getEdges().size() < 3);
        assertTrue(app.getRule() == r1 || app.getRule() == r2);

        if(app.getRule() == r1){
            assertTrue(!host.getUniqueNodeLabels().contains("A"));
        }else if(app.getRule() == r2){
            assertTrue(!host.getUniqueNodeLabels().contains("A"));
            assertTrue(!host.getUniqueNodeLabels().contains("B"));
        }else{
            fail();
        }

    }

}