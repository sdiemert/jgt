package com.sdiemert.jgt;

import java.util.HashMap;

public class Morphism {

    private HashMap<Integer, Integer> nodeMap = null;
    private HashMap<Integer, Integer> edgeMap = null;

    public Morphism(){
        this.nodeMap = new HashMap<Integer, Integer>();
        this.edgeMap = new HashMap<Integer, Integer>();
    }

    public void addNodeMapping(int matchNode, int hostNode){
        this.nodeMap.put(matchNode, hostNode);
    }

    public void addEdgeMapping(int matchEdge, int hostEdge){
        this.edgeMap.put(matchEdge, hostEdge);
    }

    public int mapNode(int x){
        return this.nodeMap.get(x);
    }

    public int mapEdge(int x){
        return this.nodeMap.get(x);
    }

}
