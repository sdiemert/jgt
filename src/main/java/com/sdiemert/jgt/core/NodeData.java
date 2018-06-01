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
    public boolean equals(Object o){
        if(o instanceof NodeData){
            return this.compare((NodeData) o);
        }else{
            return false;
        }
    }

    /**
     * Indicates if the NodeData object is a fixed value or
     * is variable based on a provided parameter.
     *
     * Must be implemented by a sub-class.
     *
     * @return true if the value is a parameter, false otherwise.
     */
    public abstract boolean isParameter();

    /**
     * @return a copy of this NodeData object.
     */
    public abstract NodeData clone();


    public abstract String toString();

    public abstract Object getVal();

}
