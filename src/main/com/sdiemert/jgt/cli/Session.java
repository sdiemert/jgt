package com.sdiemert.jgt.cli;

import com.sdiemert.jgt.core.*;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Integer.parseInt;

public class Session {

    private static final int maxIdGen = 100;

    // references all items created by the session.
    HashMap<String, GTSystem> systems;
    HashMap<String, Rule> rules;
    HashMap<String, Graph> graphs;
    HashMap<String, Node> nodes;
    HashMap<String, Edge> edges;
    HashMap<String, Condition> conditions;

    public Session(){
        systems = new HashMap<String, GTSystem>();
        rules = new HashMap<String, Rule>();
        graphs = new HashMap<String, Graph>();
        nodes = new HashMap<String, Node>();
        edges = new HashMap<String, Edge>();
        conditions = new HashMap<String, Condition>();
    }

    /**
     * Creates a new GT System and stores in the systems map.
     *
     * @param id id to store store system as, will overwrite existing name.
     *
     * @return the id used to store the system.
     */
    public String newSystem(String id){
        GTSystem sys = new GTSystem();
        this.systems.put(id, sys);
        return id;
    }

    /**
     * Creates a new GTSystem and stores it in the systems map under a unique name.
     *
     * @return the unique system name given.
     */
    public String newSystem() throws Exception{
        int i = 0;
        String id = "system"+i;
        while(systems.keySet().contains(id)){
            if(i > maxIdGen) throw new Exception("Automatic GTSystem count reached.");
            i++;
            id = "system"+i;
        }
        return this.newSystem(id);
    }

    /**
     * Creates a new Graph object.
     *
     * The new object is assigned to a Rule or Condition if they the scope. Otherwise it
     * is assigned to the global scope.
     *
     * @param id an identifier for the graph, will overwrite an existing Graph with this id.
     *
     * @return the identifier for the graph.
     */
    public String newGraph(String id) {
        Graph g = new Graph();
        graphs.put(id, g);
        return id;
    }

    public String newGraph() throws Exception{
        int i = 0;
        String id = "graph"+i;
        while(graphs.keySet().contains(id)){
            if(i > maxIdGen) throw new Exception("Automatic Graph count reached.");
            i++;
            id = "graph"+i;
        }
        return newGraph(id);
    }

    public String newNode(String id, String label, String data){

        if(data != null){
            try{
                Node<IntNodeData> n = new Node<IntNodeData>(label, (new IntNodeData(Integer.parseInt(data))));
                nodes.put(id, n);
            }catch(NumberFormatException e){
                Node<StringNodeData> n = new Node<StringNodeData>(label, new StringNodeData(data));
                nodes.put(id, n);
            }
        }else{
            nodes.put(id, new Node(label));
        }

        return id;
    }

    public String newNode(String label, String data) throws Exception{

        int i = 0;
        String id = "n"+i;
        while(nodes.keySet().contains(id)){
            if(i > maxIdGen) throw new Exception("Automatic Node count reached.");
            i++;
            id = "n"+i;
        }
        return newNode(id, label, data);
    }

    public String newEdge(String id, String src, String tar, String label) throws GraphException {
        if(!nodes.keySet().contains(src) || !nodes.keySet().contains(tar)){
            throw new GraphException("src and tar nodes must already exist.");
        }
        Edge e = new Edge(nodes.get(src), nodes.get(tar), label);
        edges.put(id, e);
        return id;
    }

    public String newEdge(String src, String tar, String label) throws GraphException, Exception{

        int i = 0;
        String id = "e"+i;
        while(edges.keySet().contains(id)){
            if(i > maxIdGen) throw new Exception("Automatic Edge count reached.");
            i++;
            id = "e"+i;
        }
        return newEdge(id, src, tar, label);
    }

    public boolean add(String parentId, String childId) throws GraphException {

        if(graphs.containsKey(parentId)){
            if(nodes.containsKey(childId)){
                graphs.get(parentId).addNode(nodes.get(childId));
                nodes.remove(childId);
                return true;
            }else if(edges.containsKey(childId)){
                graphs.get(parentId).addEdge(edges.get(childId));
                edges.remove(childId);
                return true;
            }else{
                return false;
            }
        }else if(systems.containsKey(parentId) && rules.containsKey(childId)){
            systems.get(parentId).addRule(rules.get(childId));
            rules.remove(childId);
            return true;
        }else{
            return false;
        }

    }
}
