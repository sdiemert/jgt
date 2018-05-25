package com.sdiemert.jgt.ui;

import com.mxgraph.layout.mxFastOrganicLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxStylesheet;
import com.sdiemert.jgt.cli.scope.GraphScope;
import com.sdiemert.jgt.cli.scope.RuleScope;
import com.sdiemert.jgt.core.Edge;
import com.sdiemert.jgt.core.Node;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Hashtable;

public class ViewerPanel extends JPanel {

    Controller control;
    ViewerMxGraph displayGraph;
    mxGraphComponent gc;
    mxFastOrganicLayout orgLayout;
    mxStylesheet stylesheet;

    final String lineNormal = "#686868";
    final String fillNormal = "#e5e5e5";
    final String fillAdd = "#9cd68f";
    final String lineAdd = "#2e6d1f";
    final String fillDel = "#6587c6";
    final String lineDel = "#1e396d";


    public ViewerPanel(Controller c){

        this.control = c;
        displayGraph = new ViewerMxGraph();

        gc = new mxGraphComponent(displayGraph);
        gc.setConnectable(false);

        orgLayout = new mxFastOrganicLayout(displayGraph);
        orgLayout.setForceConstant(150);

        this.setupStyles();

        this.setLayout(new BorderLayout());
        this.setBorder(new EmptyBorder(10,10,10,5));
        this.add(gc, BorderLayout.CENTER);

    }

    void displayGraph(GraphScope gs){

        Object parent = displayGraph.getDefaultParent();
        displayGraph.getModel().beginUpdate();
        HashMap<String, Object> nodeMap = new HashMap<String, Object>();

        displayGraph.removeCells(displayGraph.getChildCells(parent));

        try{

            for(Node n : gs.getGraph().getNodes()){
                String data = (n.getData() != null ? " : " + n.getData().toString() : "");
                Object o = displayGraph.insertVertex(
                        parent, n.getId(), n.getLabel() + data,
                        100, 100, 80, 30,
                        "NORMALVERTEX");

                nodeMap.put(n.getId(), o);
            }

            for(Edge e : gs.getGraph().getEdges()){
                Object o = displayGraph.insertEdge(
                        parent, null, e.getLabel(),
                        nodeMap.get(e.getSrc().getId()),
                        nodeMap.get(e.getTar().getId()),
                        "NORMALEDGE");
            }

        }finally{

            orgLayout.execute(parent);

            displayGraph.getModel().endUpdate();
            gc.refresh();
            this.revalidate();
        }
    }

    void displayRule(RuleScope rs){

        Object parent = displayGraph.getDefaultParent();
        displayGraph.getModel().beginUpdate();
        HashMap<String, Object> nodeMap = new HashMap<String, Object>();

        displayGraph.removeCells(displayGraph.getChildCells(parent));

        String style;

        try{

            for(Node n : rs.getGraphScope().getGraph().getNodes()){

                if(rs.getAddNodes().contains(n)){
                    style = "ADDVERTEX";
                }else if(rs.getDelNodes().contains(n)){
                    style = "DELVERTEX";
                }else{
                    style = "NORMALVERTEX";
                }

                String data = (n.getData() != null ? " : " + n.getData().toString() : "");
                Object o = displayGraph.insertVertex(
                        parent, n.getId(), n.getLabel() + data,
                        100, 100, 80, 30, style);

                nodeMap.put(n.getId(), o);
            }

            for(Edge e : rs.getGraphScope().getGraph().getEdges()){

                if(rs.getAddEdges().contains(e)){
                    style = "ADDEDGE";
                }else if(rs.getDelEdges().contains(e)){
                    style = "DELEDGE";
                }else{
                    style = "NORMALEDGE";
                }

                Object o = displayGraph.insertEdge(
                        parent, null, e.getLabel(),
                        nodeMap.get(e.getSrc().getId()),
                        nodeMap.get(e.getTar().getId()), style);
            }

        }finally{

            orgLayout.execute(parent);

            displayGraph.getModel().endUpdate();
            gc.refresh();
            this.revalidate();
        }

    }

    private void setupStyles(){

        stylesheet = displayGraph.getStylesheet();

        Hashtable<String, Object> normalVertexStyle = new Hashtable<String, Object>();
        normalVertexStyle.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
        normalVertexStyle.put(mxConstants.STYLE_FONTCOLOR, "#000000");
        normalVertexStyle.put(mxConstants.STYLE_FILLCOLOR, fillNormal);
        normalVertexStyle.put(mxConstants.STYLE_STROKECOLOR, lineNormal);
        normalVertexStyle.put(mxConstants.STYLE_EDITABLE, 0);
        stylesheet.putCellStyle("NORMALVERTEX", normalVertexStyle);

        Hashtable<String, Object> normalEdgeStyle = new Hashtable<String, Object>();
        normalEdgeStyle.put(mxConstants.STYLE_FONTCOLOR, "#000000");
        normalEdgeStyle.put(mxConstants.STYLE_STROKECOLOR, lineNormal);
        normalEdgeStyle.put(mxConstants.STYLE_EDITABLE, 0);
        stylesheet.putCellStyle("NORMALEDGE", normalEdgeStyle);

        Hashtable<String, Object> addVertexStyle = new Hashtable<String, Object>();
        addVertexStyle.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
        addVertexStyle.put(mxConstants.STYLE_FONTCOLOR, "#000000");
        addVertexStyle.put(mxConstants.STYLE_FILLCOLOR, fillAdd);
        addVertexStyle.put(mxConstants.STYLE_STROKECOLOR, lineAdd);
        addVertexStyle.put(mxConstants.STYLE_EDITABLE, 0);
        stylesheet.putCellStyle("ADDVERTEX", addVertexStyle);

        Hashtable<String, Object> addEdgeStyle = new Hashtable<String, Object>();
        addEdgeStyle.put(mxConstants.STYLE_FONTCOLOR, "#000000");
        addEdgeStyle.put(mxConstants.STYLE_STROKECOLOR, lineAdd);
        addEdgeStyle.put(mxConstants.STYLE_EDITABLE, 0);
        stylesheet.putCellStyle("ADDEDGE", addEdgeStyle);

        Hashtable<String, Object> delVertexStyle = new Hashtable<String, Object>();
        delVertexStyle.put(mxConstants.STYLE_SHAPE, mxConstants.SHAPE_RECTANGLE);
        delVertexStyle.put(mxConstants.STYLE_FONTCOLOR, "#000000");
        delVertexStyle.put(mxConstants.STYLE_FILLCOLOR, fillDel);
        delVertexStyle.put(mxConstants.STYLE_STROKECOLOR, lineDel);
        delVertexStyle.put(mxConstants.STYLE_EDITABLE, 0);
        stylesheet.putCellStyle("DELVERTEX", delVertexStyle);

        Hashtable<String, Object> delEdgeStyle = new Hashtable<String, Object>();
        delEdgeStyle.put(mxConstants.STYLE_FONTCOLOR, "#000000");
        delEdgeStyle.put(mxConstants.STYLE_STROKECOLOR, lineDel);
        delEdgeStyle.put(mxConstants.STYLE_EDITABLE, 0);
        stylesheet.putCellStyle("DELEDGE", delEdgeStyle);

    }
}
