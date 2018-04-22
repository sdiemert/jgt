package com.sdiemert.jgt.ui;

import com.mxgraph.layout.mxFastOrganicLayout;
import com.mxgraph.layout.mxGraphLayout;
import com.mxgraph.layout.mxStackLayout;
import com.mxgraph.swing.mxGraphComponent;
import com.sdiemert.jgt.cli.scope.GraphScope;
import com.sdiemert.jgt.core.Edge;
import com.sdiemert.jgt.core.Node;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class ViewerPanel extends JPanel {

    Controller control;
    ViewerMxGraph displayGraph;
    mxGraphComponent gc;
    mxGraphLayout orgLayout;


    public ViewerPanel(Controller c){
        this.control = c;
        displayGraph = new ViewerMxGraph();
        gc = new mxGraphComponent(displayGraph);

        orgLayout = new mxFastOrganicLayout(displayGraph);

        gc.setConnectable(false);

        this.setLayout(new BorderLayout());
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
                        100, 100, 80, 30, "editable=0");
                nodeMap.put(n.getId(), o);
            }

            for(Edge e : gs.getGraph().getEdges()){
                Object o = displayGraph.insertEdge(
                        parent, null, e.getLabel(),
                        nodeMap.get(e.getSrc().getId()), nodeMap.get(e.getTar().getId()), "editable=0");
            }

        }finally{
            orgLayout.execute(parent);

            displayGraph.getModel().endUpdate();
            gc.refresh();
            this.revalidate();
        }
    }
}
