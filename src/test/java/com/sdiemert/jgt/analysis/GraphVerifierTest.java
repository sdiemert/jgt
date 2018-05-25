package com.sdiemert.jgt.analysis;

import com.sdiemert.jgt.core.Edge;
import com.sdiemert.jgt.core.Graph;
import com.sdiemert.jgt.core.GraphException;
import com.sdiemert.jgt.core.Node;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.sdiemert.jgt.analysis.VerificationResult.VerificationResultType.*;
import static org.junit.Assert.*;

public class GraphVerifierTest {

    Graph g;
    Node n0, n1;
    Edge e0;

    @Before
    public void setUp() throws Exception {
        g = new Graph();
        n0 = new Node();
        n1 = new Node();
        e0 = new Edge(n0, n1);
        g.addNodes(n0, n1);
        g.addEdges(e0);
    }

    @After
    public void tearDown() throws Exception {
        g = null;
        n0 = null;
        n1 = null;
        e0 = null;
    }

    @Test
    public void check() {
        GraphVerifier gv = new GraphVerifier();
        VerificationResult r = gv.checkGraphDanglingEdges(g);
        assertNotNull(r);
        assertFalse(r.isFail());
        assertNotNull(r.getMessage());
        assertEquals(PASS, r.getResult());
        assertTrue(r.getEffectedEdges() == null || r.getEffectedEdges().size() == 0);
        assertTrue(r.getEffectedNodes() == null || r.getEffectedNodes().size() == 0);
    }

    @Test
    public void checkDetectsADanglingEdge() {
        g.getNodes().remove(0);
        GraphVerifier gv = new GraphVerifier();
        VerificationResult r = gv.checkGraphDanglingEdges(g);
        assertNotNull(r);
        assertTrue(r.isFail());
        assertNotNull(r.getMessage());
        assertEquals(FAIL_INVALID_GRAPH, r.getResult());
    }

    @Test
    public void checkGraphDanglingEdgesNoSource() throws GraphException {
        g.getNodes().remove(0);
        GraphVerifier gv = new GraphVerifier();
        VerificationResult r = gv.checkGraphDanglingEdges(g);
        assertNotNull(r);
        assertNotNull(r.getMessage());
        assertNotNull(r.getEffectedEdges());
        assertTrue(r.isFail());
        assertEquals(FAIL_INVALID_GRAPH, r.getResult());
        assertEquals(1, r.getEffectedEdges().size());
        assertEquals(e0, r.getEffectedEdges().get(0));
    }

    @Test
    public void checkGraphDanglingEdgesNoTar() throws GraphException {
        g.getNodes().remove(1);
        GraphVerifier gv = new GraphVerifier();
        VerificationResult r = gv.checkGraphDanglingEdges(g);
        assertNotNull(r);
        assertNotNull(r.getMessage());
        assertNotNull(r.getEffectedEdges());
        assertTrue(r.isFail());
        assertEquals(FAIL_INVALID_GRAPH, r.getResult());
        assertEquals(1, r.getEffectedEdges().size());
        assertEquals(e0, r.getEffectedEdges().get(0));
    }

    @Test
    public void checkNoDuplicateNodes(){
        g.getNodes().add(n0);
        GraphVerifier gv = new GraphVerifier();
        VerificationResult r = gv.checkDuplicateNodes(g);
        assertNotNull(r);
        assertNotNull(r.getMessage());
        assertEquals(FAIL_DUPLICATE_ELEMENT_REFERENCE, r.getResult());
        assertNotNull(r.getEffectedNodes());
        assertEquals(1, r.getEffectedNodes().size());
        assertEquals(n0, r.getEffectedNodes().get(0));

    }


    @Test
    public void checkNoDuplicateEdges(){
        g.getEdges().add(e0);
        GraphVerifier gv = new GraphVerifier();
        VerificationResult r = gv.checkDuplicateEdges(g);
        assertNotNull(r);
        assertNotNull(r.getMessage());
        assertEquals(FAIL_DUPLICATE_ELEMENT_REFERENCE, r.getResult());
        assertNotNull(r.getEffectedEdges());
        assertEquals(1, r.getEffectedEdges().size());
        assertEquals(e0, r.getEffectedEdges().get(0));

    }

}