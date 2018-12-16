package com.sdiemert.jgt.fs;

import com.sdiemert.jgt.core.*;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.activation.UnsupportedDataTypeException;

import static org.junit.Assert.*;

public class JSONFileWriterTest {

    JSONFileWriter fw;

    @Before
    public void setUp() throws Exception {
        fw = new JSONFileWriter();
    }

    @After
    public void tearDown() throws Exception {
        fw = null;
    }

    @Test
    public void ruleToJSON() throws UnsupportedDataTypeException, GraphException{
        Node n1 = new Node("n1","A");
        Node n2 = new Node("n2","B");
        Edge e = new Edge("e1", n1, n2, "e");
        Graph g = new Graph("g1");
        g.addNodes(n1, n2);
        g.addEdge(e);

        Rule r = new Rule(g, null, null, null, null, null, null);

        r.setId("r1");

        JSONObject jR = fw.ruleToJSON(r);

        assertNotNull(jR);
        assertEquals("r1", jR.getString("id"));
        assertEquals("g1", jR.getJSONObject("graph").getString("id"));
        assertEquals(0, jR.getJSONArray("addNodes").length());
        assertEquals(0, jR.getJSONArray("addEdges").length());
        assertEquals(0, jR.getJSONArray("delNodes").length());
        assertEquals(0, jR.getJSONArray("delEdges").length());
    }

    @Test
    public void graphToJSON() throws GraphException, UnsupportedDataTypeException{
        Node n1 = new Node("n1","A");
        Node n2 = new Node("n2","B");
        Edge e = new Edge("e1", n1, n2, "e");
        Graph g = new Graph("g1");
        g.addNodes(n1, n2);
        g.addEdge(e);

        JSONObject j = fw.graphToJSON(g);

        assertNotNull(j);
        assertEquals(2, j.getJSONArray("nodes").length());
        assertEquals(1, j.getJSONArray("edges").length());
        assertEquals("g1", j.getString("id"));
    }

    @Test
    public void nodeToJSON() throws UnsupportedDataTypeException {

        Node n = new Node("n1", "A");
        JSONObject j = fw.nodeToJSON(n);

        assertNotNull(j);
        assertEquals(j.getString("id"), "n1");
        assertEquals(j.getString("label"), "A");

    }

    @Test
    public void nodeToJSONWithIntData() throws UnsupportedDataTypeException {

        Node<IntNodeData> n = new Node<IntNodeData>("n1", "A", new IntNodeData(1));
        JSONObject j = fw.nodeToJSON(n);

        assertNotNull(j);
        assertEquals("n1", j.getString("id"));
        assertEquals("A", j.getString("label"));
        assertEquals(1, j.getInt("data"));

    }

    @Test
    public void nodeToJSONWithStringData() throws UnsupportedDataTypeException {

        Node<StringNodeData> n = new Node<StringNodeData>("n1", "A", new StringNodeData("foo"));
        JSONObject j = fw.nodeToJSON(n);

        assertNotNull(j);
        assertEquals("n1", j.getString("id"));
        assertEquals("A", j.getString("label"));
        assertEquals("foo", j.getString("data"));

    }


    @Test
    public void edgeToJSON() {
        Node n1 = new Node("n1","A");
        Node n2 = new Node("n2","B");
        Edge e = new Edge("e1", n1, n2, "e");

        JSONObject j = fw.edgeToJSON(e);

        assertNotNull(j);
        assertEquals("e", j.getString("label"));
        assertEquals("n1", j.getString("src"));
        assertEquals("n2", j.getString("tar"));
        assertEquals("e1", j.getString("id"));
    }
}