package com.sdiemert.jgt.fs;

import com.sdiemert.jgt.core.*;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.activation.UnsupportedDataTypeException;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JSONRoundTripTest {

    private JSONFileWriter fw;
    private JSONFileParser fp;

    @Before
    public void setUp() throws Exception {
        fw = new JSONFileWriter();
        fp = new JSONFileParser();
    }

    @After
    public void tearDown() throws Exception {
        fw = null;
        fp = null;
    }

    @Test
    public void graphRoundTrip() throws GraphException, IOException {

        String PATH = "test-fixtures/graph-roundtrip.json";

        Node n1 = new Node("n1","A");
        Node n2 = new Node("n2","B");
        Edge e = new Edge("e1", n1, n2, "e");
        Graph g = new Graph("g1");
        g.addNodes(n1, n2);
        g.addEdge(e);

        fw.writeGraph(g, PATH);

        Graph out = fp.loadGraph(PATH);

        assertNotNull(out);
        assertEquals("g1", out.getId());
        assertEquals(2, out.getNodes().size());
        assertEquals(1, out.getEdges().size());
    }

    @Test
    public void systemRoundTrip() throws GraphException, IOException, RuleException, JSONException {

        String PATH = "test-fixtures/system-roundtrip.json";

        Node n1 = new Node("n1","A");
        Node n2 = new Node("n2","B");
        Edge e = new Edge("e1", n1, n2, "e");
        Graph g = new Graph("g1");
        g.addNodes(n1, n2);
        g.addEdge(e);

        Rule r = new Rule("r1", g, null, null, null, null);

        GTSystem s = new GTSystem("s1");

        s.addRule(r);

        fw.writeSystem(s, PATH);

        GTSystem out = fp.loadSystem(PATH);

        assertNotNull(out);
        assertEquals("s1", out.getId());
        assertEquals(1, out.getRules().size());
        assertEquals("r1", out.getRules().get(0).getId());

    }

}