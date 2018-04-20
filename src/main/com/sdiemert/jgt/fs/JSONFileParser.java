package com.sdiemert.jgt.fs;

import com.sdiemert.jgt.core.*;

import org.json.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class JSONFileParser implements FileParser {

    public Graph loadGraph(String filePath) throws IOException, GraphException{
        return loadGraph(fromFile(filePath));
    }

    Graph loadGraph(JSONObject obj) throws GraphException {

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

    public GTSystem loadSystem(String filePath) throws FileNotFoundException{

        return null;
    }

    protected JSONObject fromFile(String path) throws IOException {
        File f = new File(path);
        String c = FileUtils.readFileToString(f, "utf-8");
        return new JSONObject(c);
    }

}
