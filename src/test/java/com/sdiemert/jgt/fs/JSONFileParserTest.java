package com.sdiemert.jgt.fs;

import com.sdiemert.jgt.core.*;
import org.json.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.*;

public class JSONFileParserTest {

    JSONFileParser fp = null;

    @Before
    public void before(){
        fp = new JSONFileParser();
    }

    @After
    public void after(){
        fp = null;
    }

    @Test
    public void loadGraphFromFile() throws IOException, GraphException {

        Graph g = fp.loadGraph("src/test/resources/graph.json");

        assertNotNull(g);
        assertEquals(3, g.getNodes().size());
        assertEquals(3, g.getEdges().size());
    }

    @Test(expected=IOException.class)
    public void loadGraphFromFileNoFile() throws IOException, GraphException {
        fp.loadGraph("NOT A FILE");
    }

    @Test
    public void loadGraphFromJSONObj() throws GraphException {

        JSONObject jN1 = new JSONObject("{  \"id\" : \"n1\", \"label\" : \"A\"}");
        JSONObject jN2 = new JSONObject("{  \"id\" : \"n2\", \"label\" : \"B\"}");
        JSONObject jE = new JSONObject(
                "{  \"id\" : \"e1\", \"src\" : \"n1\", \"tar\" : \"n2\", \"label\" : \"e\" }");

        JSONArray nodeArray = new JSONArray(Arrays.asList(jN1, jN2));
        JSONArray edgeArray = new JSONArray(Arrays.asList(jE));

        JSONObject jG = new JSONObject();
        jG.put("nodes", nodeArray);
        jG.put("edges", edgeArray);
        jG.put("id", "g1");

        Graph g = fp.loadGraph(jG);

        assertNotNull(g);
        assertEquals(2, g.getNodes().size());
        assertEquals(1, g.getEdges().size());

    }

    @Test(expected=JSONException.class)
    public void loadGraphFromJSONObjWithoutGraphId() throws GraphException {

        JSONObject jN1 = new JSONObject("{  \"id\" : \"n1\", \"label\" : \"A\"}");
        JSONObject jN2 = new JSONObject("{  \"id\" : \"n2\", \"label\" : \"B\"}");
        JSONObject jE = new JSONObject(
                "{  \"id\" : \"e1\", \"src\" : \"n1\", \"tar\" : \"n2\", \"label\" : \"e\" }");

        JSONArray nodeArray = new JSONArray(Arrays.asList(jN1, jN2));
        JSONArray edgeArray = new JSONArray(Arrays.asList(jE));

        JSONObject jG = new JSONObject();
        jG.put("nodes", nodeArray);
        jG.put("edges", edgeArray);

        Graph g = fp.loadGraph(jG);

    }

    @Test(expected=JSONException.class)
    public void loadGraphFromJSONObjWithoutNodes() throws GraphException {

        JSONObject jE = new JSONObject(
                "{  \"id\" : \"e1\", \"src\" : \"n1\", \"tar\" : \"n2\", \"label\" : \"e\" }");


        JSONArray edgeArray = new JSONArray(Arrays.asList(jE));

        JSONObject jG = new JSONObject();

        jG.put("edges", edgeArray);
        jG.put("id", "g1");

        fp.loadGraph(jG);

    }

    @Test(expected=JSONException.class)
    public void loadGraphFromJSONObjectWithoutedges() throws GraphException {

        JSONObject jN1 = new JSONObject("{  \"id\" : \"n1\", \"label\" : \"A\"}");
        JSONObject jN2 = new JSONObject("{  \"id\" : \"n2\", \"label\" : \"B\"}");

        JSONArray nodeArray = new JSONArray(Arrays.asList(jN1, jN2));

        JSONObject jG = new JSONObject();
        jG.put("nodes", nodeArray);
        jG.put("id", "g1");

        fp.loadGraph(jG);

    }

    @Test
    public void loadNodeWithoutData() {
        JSONObject j = new JSONObject("{  \"id\" : \"n1\", \"label\" : \"A\"}");
        Node n = fp.loadNode(j);
        assertNotNull(n);
        assertEquals("n1", n.getId());
        assertEquals("A", n.getLabel());
        assertNull(n.getData());
    }

    @Test
    public void loadNodeWithIntData() {
        JSONObject j = new JSONObject("{  \"id\" : \"n1\", \"label\" : \"A\", \"data\":1}");
        Node n = fp.loadNode(j);
        assertNotNull(n);
        assertEquals("n1", n.getId());
        assertEquals("A", n.getLabel());
        assertEquals(new IntNodeData(1), n.getData());
    }

    @Test
    public void loadNodeWithStringData() {
        JSONObject j = new JSONObject("{  \"id\" : \"n1\", \"label\" : \"A\", \"data\":\"foo\"}");
        Node n = fp.loadNode(j);
        assertNotNull(n);
        assertEquals("n1", n.getId());
        assertEquals("A", n.getLabel());
        assertEquals(new StringNodeData("foo"), n.getData());
    }

    @Test(expected=JSONException.class)
    public void loadNodeWithoutId() {
        JSONObject j = new JSONObject("{ \"label\" : \"A\" }");
        Node n = fp.loadNode(j);
    }

    @Test(expected=JSONException.class)
    public void loadNodeWithoutLabel() {
        JSONObject j = new JSONObject("{ \"id\" : \"n1\" }");
        Node n = fp.loadNode(j);
    }

    @Test
    public void loadEdge() throws GraphException {
        JSONObject jE = new JSONObject(
                "{  \"id\" : \"e1\", \"src\" : \"n1\", \"tar\" : \"n2\", \"label\" : \"A\" }");

        Graph g = new Graph();
        g.addNode(new Node("n1", "A"));
        g.addNode(new Node("n2", "A"));

        Edge e = fp.loadEdge(jE, g);

        assertNotNull(e);
        assertEquals("e1", e.getId());
        assertEquals("A", e.getLabel());
        assertEquals("n1", e.getSrc().getId());
        assertEquals("n2", e.getTar().getId());

    }

    @Test(expected=GraphException.class)
    public void loadEdgeSrcNotInGraph() throws GraphException {
        JSONObject jE = new JSONObject(
                "{  \"id\" : \"e1\", \"src\" : \"n1\", \"tar\" : \"n2\", \"label\" : \"A\" }");
        Graph g = new Graph();
        g.addNode(new Node("n2", "A"));
        fp.loadEdge(jE, g);
    }

    @Test(expected=GraphException.class)
    public void loadEdgeTarNotInGraph() throws GraphException {
        JSONObject jE = new JSONObject(
                "{  \"id\" : \"e1\", \"src\" : \"n1\", \"tar\" : \"n2\", \"label\" : \"A\" }");
        Graph g = new Graph();
        g.addNode(new Node("n1", "A"));
        fp.loadEdge(jE, g);
    }

    @Test(expected=JSONException.class)
    public void loadEdgeNoId() throws GraphException {
        JSONObject jE = new JSONObject(
                "{  \"src\" : \"n1\", \"tar\" : \"n2\", \"label\" : \"A\" }");
        Graph g = new Graph();
        g.addNode(new Node("n1", "A"));
        g.addNode(new Node("n2", "A"));
        fp.loadEdge(jE, g);
    }


    @Test(expected=JSONException.class)
    public void loadEdgeNoLabel() throws GraphException {
        JSONObject jE = new JSONObject(
                "{  \"src\" : \"n1\", \"tar\" : \"n2\", \"id\" : \"A\" }");
        Graph g = new Graph();
        g.addNode(new Node("n1", "A"));
        g.addNode(new Node("n2", "A"));
        fp.loadEdge(jE, g);
    }

    @Test(expected=JSONException.class)
    public void loadEdgeNoSrc() throws GraphException {
        JSONObject jE = new JSONObject(
                "{  \"label\" : \"A\", \"tar\" : \"n2\", \"id\" : \"e1\" }");
        Graph g = new Graph();
        g.addNode(new Node("n1", "A"));
        g.addNode(new Node("n2", "A"));
        fp.loadEdge(jE, g);
    }

    @Test(expected=JSONException.class)
    public void loadEdgeNoTar() throws GraphException {
        JSONObject jE = new JSONObject(
                "{  \"label\" : \"A\", \"src\" : \"n2\", \"id\" : \"e1\" }");
        Graph g = new Graph();
        g.addNode(new Node("n1", "A"));
        g.addNode(new Node("n2", "A"));
        fp.loadEdge(jE, g);
    }

    @Test
    public void loadRule() throws GraphException, RuleException, JSONException{

        JSONObject jN1 = new JSONObject("{  \"id\" : \"n1\", \"label\" : \"A\"}");
        JSONObject jN2 = new JSONObject("{  \"id\" : \"n2\", \"label\" : \"B\"}");
        JSONObject jE = new JSONObject(
                "{  \"id\" : \"e1\", \"src\" : \"n1\", \"tar\" : \"n2\", \"label\" : \"e\" }");

        JSONArray nodeArray = new JSONArray(Arrays.asList(jN1, jN2));
        JSONArray edgeArray = new JSONArray(Arrays.asList(jE));

        JSONObject jG = new JSONObject();
        jG.put("nodes", nodeArray);
        jG.put("edges", edgeArray);
        jG.put("id", "g1");

        JSONObject jR = new JSONObject();
        jR.put("id", "r1");
        jR.put("graph", jG);
        jR.put("addNodes", new JSONArray());
        jR.put("addEdges", new JSONArray());
        jR.put("delNodes", new JSONArray());
        jR.put("delEdges", new JSONArray());

        Rule r = fp.loadRule(jR);

        assertNotNull(r);
        assertEquals("r1", r.getId());
        assertEquals(2, r.getRuleGraph().getNodes().size());
        assertEquals(1, r.getRuleGraph().getEdges().size());
    }

    @Test
    public void loadRuleWithAdds() throws GraphException, RuleException, JSONException{

        JSONObject jN1 = new JSONObject("{  \"id\" : \"n1\", \"label\" : \"A\"}");
        JSONObject jN2 = new JSONObject("{  \"id\" : \"n2\", \"label\" : \"B\"}");
        JSONObject jE = new JSONObject(
                "{  \"id\" : \"e1\", \"src\" : \"n1\", \"tar\" : \"n2\", \"label\" : \"e\" }");

        JSONArray nodeArray = new JSONArray(Arrays.asList(jN1, jN2));
        JSONArray edgeArray = new JSONArray(Arrays.asList(jE));

        JSONObject jG = new JSONObject();
        jG.put("nodes", nodeArray);
        jG.put("edges", edgeArray);
        jG.put("id", "g1");

        JSONArray addNodes = new JSONArray(Arrays.asList("n1"));
        JSONArray addEdges = new JSONArray(Arrays.asList("e1"));

        JSONObject jR = new JSONObject();
        jR.put("id", "r1");
        jR.put("graph", jG);
        jR.put("addNodes", addNodes);
        jR.put("addEdges", addEdges);
        jR.put("delNodes", new JSONArray());
        jR.put("delEdges", new JSONArray());

        Rule r = fp.loadRule(jR);

        assertNotNull(r);
        assertEquals(1, r.getAddEdges().size());
        assertEquals(1, r.getAddNodes().size());

    }

    @Test
    public void loadRuleWithDels() throws GraphException, RuleException, JSONException{

        JSONObject jN1 = new JSONObject("{  \"id\" : \"n1\", \"label\" : \"A\"}");
        JSONObject jN2 = new JSONObject("{  \"id\" : \"n2\", \"label\" : \"B\"}");
        JSONObject jE = new JSONObject(
                "{  \"id\" : \"e1\", \"src\" : \"n1\", \"tar\" : \"n2\", \"label\" : \"e\" }");

        JSONArray nodeArray = new JSONArray(Arrays.asList(jN1, jN2));
        JSONArray edgeArray = new JSONArray(Arrays.asList(jE));

        JSONObject jG = new JSONObject();
        jG.put("nodes", nodeArray);
        jG.put("edges", edgeArray);
        jG.put("id", "g1");

        JSONArray addNodes = new JSONArray(Arrays.asList("n1"));
        JSONArray addEdges = new JSONArray(Arrays.asList("e1"));

        JSONObject jR = new JSONObject();
        jR.put("id", "r1");
        jR.put("graph", jG);
        jR.put("delNodes", addNodes);
        jR.put("delEdges", addEdges);
        jR.put("addNodes", new JSONArray());
        jR.put("addEdges", new JSONArray());

        Rule r = fp.loadRule(jR);

        assertNotNull(r);
        assertEquals(1, r.getDelEdges().size());
        assertEquals(1, r.getDelNodes().size());

    }

    @Test(expected = RuleException.class)
    public void loadRuleInvalidDel() throws GraphException, RuleException, JSONException{

        JSONObject jN1 = new JSONObject("{  \"id\" : \"n1\", \"label\" : \"A\"}");
        JSONObject jN2 = new JSONObject("{  \"id\" : \"n2\", \"label\" : \"B\"}");
        JSONObject jE = new JSONObject(
                "{  \"id\" : \"e1\", \"src\" : \"n1\", \"tar\" : \"n2\", \"label\" : \"e\" }");

        JSONArray nodeArray = new JSONArray(Arrays.asList(jN1, jN2));
        JSONArray edgeArray = new JSONArray(Arrays.asList(jE));

        JSONObject jG = new JSONObject();
        jG.put("nodes", nodeArray);
        jG.put("edges", edgeArray);
        jG.put("id", "g1");

        JSONArray addNodes = new JSONArray(Arrays.asList("NOT A NODE"));
        JSONArray addEdges = new JSONArray(Arrays.asList("e1"));

        JSONObject jR = new JSONObject();
        jR.put("id", "r1");
        jR.put("graph", jG);
        jR.put("delNodes", addNodes);
        jR.put("delEdges", addEdges);
        jR.put("addNodes", new JSONArray());
        jR.put("addEdges", new JSONArray());

        fp.loadRule(jR);

    }

    @Test(expected = JSONException.class)
    public void loadRuleNoId() throws GraphException, RuleException, JSONException{

        JSONObject jN1 = new JSONObject("{  \"id\" : \"n1\", \"label\" : \"A\"}");
        JSONObject jN2 = new JSONObject("{  \"id\" : \"n2\", \"label\" : \"B\"}");
        JSONObject jE = new JSONObject(
                "{  \"id\" : \"e1\", \"src\" : \"n1\", \"tar\" : \"n2\", \"label\" : \"e\" }");

        JSONArray nodeArray = new JSONArray(Arrays.asList(jN1, jN2));
        JSONArray edgeArray = new JSONArray(Arrays.asList(jE));

        JSONObject jG = new JSONObject();
        jG.put("nodes", nodeArray);
        jG.put("edges", edgeArray);
        jG.put("id", "g1");

        JSONArray addNodes = new JSONArray(Arrays.asList("n1"));
        JSONArray addEdges = new JSONArray(Arrays.asList("e1"));

        JSONObject jR = new JSONObject();

        jR.put("graph", jG);
        jR.put("delNodes", addNodes);
        jR.put("delEdges", addEdges);
        jR.put("addNodes", new JSONArray());
        jR.put("addEdges", new JSONArray());

        fp.loadRule(jR);

    }

    @Test
    public void loadSystem() throws IOException, GraphException, RuleException, JSONException {
        GTSystem s = fp.loadSystem("src/test/resources/system.json");
        assertNotNull(s);
        assertEquals(1, s.getRules().size());
        assertEquals("s1", s.getId());
        assertEquals("r1", s.getRules().get(0).getId());
    }

    @Test(expected = IOException.class)
    public void loadSystemInvalidFile() throws IOException, GraphException, RuleException, JSONException{
        fp.loadSystem("NOT A VALID PATH");
    }
}
