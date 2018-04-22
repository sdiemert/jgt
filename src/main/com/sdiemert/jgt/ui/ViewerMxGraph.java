package com.sdiemert.jgt.ui;

import com.mxgraph.view.mxGraph;

public class ViewerMxGraph extends mxGraph {
    public boolean isCellSelectable(Object cell){
        if(model.isEdge(cell)){
            return false;
        }else{
            return super.isCellSelectable(cell);
        }
    }
}
