package com.sdiemert.jgt.fs;

import com.sdiemert.jgt.core.*;

import org.json.*;

import java.io.IOException;
import java.util.ArrayList;

public class JSONFileParser extends FileParser {

    /**
     * Loads a Graph from a json file.
     * @param filePath the path to the file.
     *
     * @return a Graph object corresponding to the contents of the file.
     *
     * @throws IOException if the file cannot be found/opened.
     * @throws GraphException if the graph in the file is invalid.
     * @throws JSONException if the JSON in the file is not valid or does not match the schema.
     */
    public Graph loadGraph(String filePath) throws IOException, GraphException, JSONException{
        return loadGraph(fromFile(filePath));
    }

    /**
     * Loads a GTSystem from a json file at the designated path.
     * @param filePath the path to the json file.
     * @return a GTSystem
     * @throws IOException if the file is not found/opened.
     * @throws JSONException if the file contains invalid JSON or the JSON does not match the expected schema.
     * @throws GraphException if a rule/condition graph in the file is invalid.
     */
    public GTSystem loadSystem(String filePath) throws IOException, JSONException, GraphException, RuleException {
        return loadSystem(fromFile(filePath));
    }

    Graph loadGraph(JSONObject obj) throws GraphException, JSONException {

        // {
        //      id : "string",
        //      nodes : [
        //          { ... }, { ... }, { ...} ],
        //      edges : [
        //          { ... }, { ... }, { ...} ]
        // }

        String id = obj.getString("id");

        Graph g = new Graph(id);

        JSONArray nodes = obj.getJSONArray("nodes");
        JSONArray edges = obj.getJSONArray("edges");

        for(int i = 0; i < nodes.length(); i++) {
            g.addNode(loadNode((JSONObject) nodes.get(i)));
        }

        for(int i = 0; i < edges.length(); i++){
            g.addEdge(loadEdge((JSONObject) edges.get(i), g));
        }

        return g;
    }

    Node loadNode(JSONObject obj) throws JSONException{

        // { id : "xxxx", label : "xxxx", data : string|int }

        if(obj.has("data")){

            try{
                int d = obj.getInt("data");
                Node<IntNodeData> n = new Node<IntNodeData>(obj.getString("id"), obj.getString("label"));
                n.setData(new IntNodeData(d));
                return n;
            }catch(JSONException e){
                String d = obj.getString("data");
                Node<StringNodeData> n = new Node<StringNodeData>(obj.getString("id"), obj.getString("label"));
                n.setData(new StringNodeData(d));
                return n;
            }

        }else{

            return new Node<IntNodeData>(obj.getString("id"), obj.getString("label"));

        }

    }

    Edge loadEdge(JSONObject obj, Graph g) throws GraphException, JSONException {

        // { id : "xxx", src : "node-id", tar : "node-id", label : "string" }

        Node src = g.getNode(obj.getString("src"));
        Node tar = g.getNode(obj.getString("tar"));

        if(src == null || tar == null) throw new GraphException("src and tar must be in nodes.");

        return new Edge(obj.getString("id"), src, tar, obj.getString("label"));

    }

    GTSystem loadSystem(JSONObject obj) throws GraphException, RuleException {

        // { "id" : "xxx", "rules" : [ {...} ], "conditions" : [ {...} ] }

        GTSystem gts = new GTSystem(obj.getString("id"));

        for(Object r : obj.getJSONArray("rules")){
            gts.addRule(loadRule((JSONObject) r));
        }

        return gts;
    }

    Rule loadRule(JSONObject obj) throws JSONException, GraphException, RuleException{

        // {
        //   "id" : "xxx", "graph" : { ... },
        //   "addNodes" : ["x",...],
        //   "addEdges" : ["x",...],
        //   "delNodes" : ["x",...],
        //   "delEdges" : ["x",...]
        // }

        String id = obj.getString("id");
        Graph ruleGraph = loadGraph(obj.getJSONObject("graph"));

        Node tmpNode = null;
        Edge tmpEdge = null;
        String elemId = null;

        ArrayList<Node> addNodes = new ArrayList<Node>();

        for(Object o : obj.getJSONArray("addNodes")){
            elemId = (String)o;
            tmpNode = ruleGraph.getNode(elemId);
            if(tmpNode != null){
                addNodes.add(tmpNode);
            }else{
                throw new RuleException("all nodes in addNodes must be in rule Graph");
            }
        }

        ArrayList<Node> delNodes = new ArrayList<Node>();
        for(Object o : obj.getJSONArray("delNodes")){
            elemId = (String)o;
            tmpNode = ruleGraph.getNode(elemId);
            if(tmpNode != null){
                delNodes.add(tmpNode);
            }else{
                throw new RuleException("all nodes in delNodes must be in rule Graph");
            }
        }

        ArrayList<Edge> addEdges = new ArrayList<Edge>();
        for(Object o : obj.getJSONArray("addEdges")){
            elemId = (String)o;
            tmpEdge = ruleGraph.getEdge(elemId);
            if(tmpEdge != null){
                addEdges.add(tmpEdge);
            }else{
                throw new RuleException("all nodes in addEdges must be in rule Graph");
            }
        }

        ArrayList<Edge> delEdges = new ArrayList<Edge>();
        for(Object o : obj.getJSONArray("delEdges")){
            elemId = (String)o;
            tmpEdge = ruleGraph.getEdge(elemId);
            if(tmpEdge != null){
                delEdges.add(tmpEdge);
            }else{
                throw new RuleException("all nodes in delEdges must be in rule Graph");
            }
        }

        Rule r = new Rule(ruleGraph, addNodes, addEdges, delNodes, delEdges, null, null);

        r.setId(id);

        return r;

    }

}
