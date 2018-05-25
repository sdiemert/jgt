package com.sdiemert.jgt.core;

public class IntNodeData extends NodeData {

    private int val;

    public IntNodeData(int i){
        this.val = i;
    }

    public boolean compare(NodeData d){
        if(d instanceof IntNodeData && this.getVal() == ((IntNodeData) d).getVal()){
            return true;
        }else{
            return false;
        }
    }

    public IntNodeData clone(){
        return new IntNodeData(this.getVal());
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public String toString(){
        return ""+this.getVal();
    }
}
