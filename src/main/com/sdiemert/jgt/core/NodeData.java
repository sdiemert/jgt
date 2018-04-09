package com.sdiemert.jgt.core;

import org.jetbrains.annotations.Contract;

public abstract class NodeData {

    /**
     * Compares this NodeData object to another.
     *
     * This method should be sub-classed and implemented for a specific data type.
     *
     * @param o a NodeData object to compare against for equality.
     * @return true if the objects are equivalent, false otherwise.
     */
    protected abstract boolean compare(NodeData o);

    /**
     * Compares this NodeData object with another object.
     * @param o the object to compare against.
     * @return true if the objects are functionally equal, false otherwise.
     */
    @Contract(value = "null -> false", pure = true)
    public boolean equals(Object o){
        if(o instanceof NodeData){
            return this.compare((NodeData) o);
        }else{
            return false;
        }
    }

    /**
     * @return a copy of this NodeData object.
     */
    public abstract NodeData clone();

}
