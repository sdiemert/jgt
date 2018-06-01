package com.sdiemert.jgt.core;

public class IntNodeData extends NodeData {

    private Integer val;

    /**
     * Creates a new node data object with a fixed integer value.
     * @param i the value of the data.
     */
    public IntNodeData(int i){
        this.val = i;
    }

    /**
     * Creates a new node data object with a variable or parameterizable
     * integer value.
     */
    public IntNodeData(){
        this.val = null;
    }

    public boolean compare(NodeData d){

        if(this.getVal() == null && d.getVal() == null){
            return true;
        } else if(d instanceof IntNodeData && this.getVal().equals(((IntNodeData) d).getVal())){
            return true;
        }else{
            return false;
        }
    }

    public boolean isParameter(){
        return this.val == null;
    }

    public IntNodeData clone(){
        if(!isParameter()) {
            return new IntNodeData(this.getVal());
        }else{
            return new IntNodeData();
        }
    }

    public Integer getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public String toString(){
        return ""+this.getVal();
    }
}
