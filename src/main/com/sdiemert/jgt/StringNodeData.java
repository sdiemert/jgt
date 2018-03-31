package com.sdiemert.jgt;

public class StringNodeData extends NodeData {

    private String val;

    public StringNodeData(String i){
        this.val = i;
    }

    public boolean compare(NodeData d){
        if(d instanceof StringNodeData && this.getVal().equals(((StringNodeData) d).getVal())){
            return true;
        }else{
            return false;
        }
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
}
