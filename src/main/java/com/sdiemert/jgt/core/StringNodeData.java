package com.sdiemert.jgt.core;

public class StringNodeData extends NodeData {

    private String val;

    /**
     * Creates a constant instance of string node data.
     * @param i
     */
    public StringNodeData(String i){
        this.val = i;
    }

    /**
     * Creates a parameterizable instance of String Node Data.
     */
    public StringNodeData(){
        this.val = null;
    }

    public boolean compare(NodeData d){
        if(this.val == null && d.getVal() == null) {
            return true;
        }else if(d instanceof StringNodeData && this.getVal().equals(((StringNodeData) d).getVal())){
            return true;
        }else{
            return false;
        }
    }

    public StringNodeData clone(){
        return new StringNodeData(this.getVal());
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public String toString(){
        return val;
    }

    public boolean isParameter(){
        return val == null;
    }
}
