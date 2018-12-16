package com.sdiemert.jgt.analysis;

import com.sdiemert.jgt.core.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static com.sdiemert.jgt.analysis.VerificationResult.VerificationResultType.FAIL_ADD_DELETE_OVERLAP;
import static com.sdiemert.jgt.analysis.VerificationResult.VerificationResultType.PASS;
import static org.junit.Assert.*;

public class RuleVerifierTest {

    private Graph rg;
    private Node rN0, rN1, rN2;
    private Edge rE0, rE1;
    private Rule rule;

    @Before
    public void setUp() throws Exception {
        rg = new Graph();
        rN0 = new Node();
        rN1 = new Node();
        rN2 = new Node();
        rE0 = new Edge(rN0, rN1);
        rE1 = new Edge(rN1, rN2);
        rg.addNodes(rN0, rN1, rN2);
        rg.addEdges(rE0, rE1);
    }

    @After
    public void tearDown() throws Exception {
        rg = null;
        rN0 = null;
        rN1 = null;
        rN2 = null;
        rE0 = null;
        rE1 = null;
        rule = null;
    }

    @Test
    public void check() throws GraphException{

        RuleVerifier rv = new RuleVerifier();

        rule = new Rule(rg, new ArrayList<Node>(Arrays.asList(rN0)), new ArrayList<Edge>(Arrays.asList(rE0)), null, null, null, null);

        VerificationResult v = rv.check(rule);

        assertNotNull(v);
        assertEquals(PASS, v.getResult());
        assertNotNull(v.getMessage());
    }

    @Test
    public void addAndDeleteNodesNotOverlap() throws GraphException {

        RuleVerifier rv = new RuleVerifier();

        rule = new Rule(rg, new ArrayList<Node>(Arrays.asList(rN0)), new ArrayList<Edge>(Arrays.asList(rE0)), new ArrayList<Node>(Arrays.asList(rN1, rN2)), null, null, null);

        // here we artificially create an overlapping node in both add and delete nodes.
        rule.getAddNodes().set(0, rN1);

        VerificationResult ret = rv.addAndDeleteNodesNotOverlap(rule);

        assertNotNull(ret);
        assertNotNull(ret.getMessage());
        assertNotNull(ret.getEffectedNodes());
        assertEquals(FAIL_ADD_DELETE_OVERLAP, ret.getResult());
        assertEquals(1, ret.getEffectedNodes().size());
        assertEquals(rN1, ret.getEffectedNodes().get(0));

    }

    @Test
    public void addAndDeleteEdgesNotOverlap() throws GraphException {

        RuleVerifier rv = new RuleVerifier();

        rule = new Rule(rg,
                new ArrayList<Node>(Arrays.asList(rN0)),
                new ArrayList<Edge>(Arrays.asList(rE0)),
                new ArrayList<Node>(Arrays.asList(rN1, rN2)),
                new ArrayList<Edge>(Arrays.asList(rE1)), null, null);

        // here we artificially create an overlapping node in both add and delete nodes.
        rule.getAddEdges().set(0, rE1);

        VerificationResult ret = rv.addAndDeleteEdgesNotOverlap(rule);

        assertNotNull(ret);
        assertNotNull(ret.getMessage());
        assertNotNull(ret.getEffectedEdges());
        assertEquals(FAIL_ADD_DELETE_OVERLAP, ret.getResult());
        assertEquals(1, ret.getEffectedEdges().size());
        assertEquals(rE1, ret.getEffectedEdges().get(0));

    }
}