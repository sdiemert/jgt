package com.sdiemert.jgt.fs;

import com.sdiemert.jgt.core.*;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.activation.UnsupportedDataTypeException;
import java.io.IOException;

public class JSONFileWriter extends FileWriter {

    /**
     * Writes the provided Graph to a JSON file.
     * @param g the Graph object to write.
     * @param path the path to write the object to, will overwrite existing files at this location.
     * @throws IOException if the file cannot be opened or written.
     */
    public void writeGraph(Graph g, String path) throws IOException{
        toFile(path, graphToJSON(g).toString());
    }

    /**
     * Writes the provided GTSystem to a JSON file.
     * @param s the GTSystem to write.
     * @param path the path to write the object to, will overwrite existing files.
     * @throws IOException if the file cannot be opened or written.
     */
    public void writeSystem(GTSystem s, String path) throws IOException{

        JSONObject jS = new JSONObject();
        jS.put("id", s.getId());

        JSONArray rules = new JSONArray();

        for(Rule r : s.getRules()){
            rules.put(ruleToJSON(r));
        }

        jS.put("rules", rules);

        toFile(path, jS.toString());
    }

    JSONObject ruleToJSON(Rule r) throws UnsupportedDataTypeException {
        JSONObject obj = new JSONObject();
        obj.put("id", r.getId());
        obj.put("graph", graphToJSON(r.getRuleGraph()));

        JSONArray arr;

        arr = new JSONArray();
        for(Node n : r.getAddNodes()){
            arr.put(n.getId());
        }
        obj.put("addNodes", arr);

        arr = new JSONArray();
        for(Edge e : r.getAddEdges()){
            arr.put(e.getId());
        }
        obj.put("addEdges", arr);

        arr = new JSONArray();
        for(Node n : r.getDelNodes()){
            arr.put(n.getId());
        }
        obj.put("delNodes", arr);

        arr = new JSONArray();
        for(Edge e : r.getDelEdges()){
            arr.put(e.getId());
        }
        obj.put("delEdges", arr);

        return obj;
    }

    JSONObject graphToJSON(Graph g) throws UnsupportedDataTypeException {

        JSONObject jG = new JSONObject();

        jG.put("id", g.getId());

        JSONArray nodes = new JSONArray();
        JSONArray edges = new JSONArray();

        for(Node n : g.getNodes()){
            nodes.put(nodeToJSON(n));
        }

        for(Edge e : g.getEdges()){
            edges.put(edgeToJSON(e));
        }

        jG.put("nodes", nodes);
        jG.put("edges", edges);

        return jG;
    }

    JSONObject nodeToJSON(Node n) throws  UnsupportedDataTypeException {
        JSONObject j = new JSONObject();
        j.put("id", n.getId());
        j.put("label", n.getLabel());

        if(n.getData() != null){

            if(n.getData() instanceof IntNodeData){
                j.put("data", ((IntNodeData) n.getData()).getVal());
            }else if(n.getData() instanceof StringNodeData){
                j.put("data", ((StringNodeData) n.getData()).getVal());
            }else{
                throw new UnsupportedDataTypeException("Cannot write data value to file");
            }
        }

        return j;
    }

    JSONObject edgeToJSON(Edge e){
        JSONObject j = new JSONObject();
        j.put("id", e.getId());
        j.put("src", e.getSrc().getId());
        j.put("tar", e.getTar().getId());
        j.put("label", e.getLabel());
        return j;
    }


}
