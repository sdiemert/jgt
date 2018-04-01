package com.sdiemert.jgt;

import com.microsoft.z3.*;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Matcher {

    // Each matcher keeps its own Context object. This may be memory intensive
    // but will help when implementing concurrent graph matches.
    Context ctx = null;

    ArrayList<String> nodeLabels = null;
    ArrayList<String> edgeLabels = null;
    ArrayList<NodeData> nodeData = null;

    int gEdgeBVSize = 1;
    int gNodeBVSize = 1;

    int hEdgeBVSize = 1;
    int hNodeBVSize = 1;

    int nodeLabelBVSize = 1;
    int edgeLabelBVSize = 1;
    int nodeDataBVSize = 1;

    public Matcher(){
        this.ctx = null;
    }

    public Morphism findMorphism(Graph g, Graph h){

        this.reset();

        this.unifyLabels(g, h);

        this.determineBitLengths(g, h);

        return null;

    }

    void graphToZ3(Graph g, String name){

        // a list of expressions that describe the graph, these will be and'd together
        // eventually.
        ArrayList<Expr> exprs = new ArrayList<Expr>();

    }

    void determineBitLengths(Graph g, Graph h){

        this.gEdgeBVSize = g.getEdges().size() > 1 ? Matcher.numberOfBits(g.getEdges().size() - 1): 1;
        this.gNodeBVSize = g.getNodes().size() > 1 ? Matcher.numberOfBits(g.getNodes().size() - 1): 1;

        this.hEdgeBVSize = h.getEdges().size() > 1 ? Matcher.numberOfBits(h.getEdges().size() - 1): 1;
        this.hNodeBVSize = h.getNodes().size() > 1 ? Matcher.numberOfBits(h.getNodes().size() - 1): 1;

        this.nodeLabelBVSize = this.nodeLabels.size() > 1 ? Matcher.numberOfBits(this.nodeLabels.size() - 1): 1;
        this.edgeLabelBVSize = this.edgeLabels.size() > 1 ? Matcher.numberOfBits(this.edgeLabels.size() - 1): 1;
        this.nodeDataBVSize = this.nodeData.size() > 1 ? Matcher.numberOfBits(this.nodeData.size() - 1) : 1;
    }

    void unifyLabels(Graph g, Graph h){
        this.unifyNodeLabels(g, h);
        this.unifyEdgeLabels(g, h);
        this.unifyNodeData(g, h);
    }

    void unifyNodeLabels(Graph g, Graph h){
        HashSet<String> hs = new HashSet<String>();
        hs.addAll(g.getUniqueNodeLabels());
        hs.addAll(h.getUniqueNodeLabels());
        this.nodeLabels.addAll(hs);
    }

    void unifyEdgeLabels(Graph g, Graph h){
        HashSet<String> hs = new HashSet<String>();
        hs.addAll(g.getUniqueEdgeLabels());
        hs.addAll(h.getUniqueEdgeLabels());
        this.edgeLabels.addAll(hs);
    }

    void unifyNodeData(Graph g, Graph h){
        this.nodeData.addAll(g.getUniqueNodeData());
        for(NodeData d : h.getUniqueNodeData()){
            if(!this.nodeData.contains(d)){
                this.nodeData.add(d);
            }
        }
    }


    public void reset(){

        if(this.ctx != null){
            this.ctx.close();
        }

        this.ctx = new Context();

        this.nodeLabels = new ArrayList<String>();
        this.edgeLabels = new ArrayList<String>();
        this.nodeData = new ArrayList<NodeData>();

        this.gEdgeBVSize = 1;
        this.gNodeBVSize = 1;
        this.hEdgeBVSize = 1;
        this.hNodeBVSize = 1;
        this.nodeDataBVSize = 1;
        this.edgeLabelBVSize = 1;
        this.nodeLabelBVSize = 1;

    }

    @Contract(pure = true)
    static int numberOfBits(int i){
        assert(i >= 0);
        return Integer.SIZE - Integer.numberOfLeadingZeros(i);
    }



}
