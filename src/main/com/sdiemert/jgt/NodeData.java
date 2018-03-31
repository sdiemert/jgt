package com.sdiemert.jgt;

public abstract class NodeData {

    /**
     * Compares this NodeData object to another.
     *
     * This method should be sub-classed and implemented for a specific data type.
     *
     * @param o a NodeData object to compare against for equality.
     * @return true if the objects are equivalent, false otherwise.
     */
    public boolean compare(NodeData o){
        return this.equals(o);
    }

}
