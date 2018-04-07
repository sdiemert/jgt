package com.sdiemert.jgt;

import org.jetbrains.annotations.Contract;

import java.util.UUID;

public class Node<T extends NodeData> {

    private String id;
    private String type;
    private T data;

    // --------- CONSTUCTORS ------------------------

    /**
     * Makes a new NONE-typed Node object.
     */
    public Node() {
        this.id = "node-"+UUID.randomUUID().toString();
        this.type = null;
        this.data = null;
    }

    /**
     * Makes a new typed Node object.
     * @param label a string describing type of the Node.
     */
    @Contract("null -> fail")
    public Node(String label){

        assert(type != null);

        this.id = "node-"+UUID.randomUUID().toString();
        this.type = label;
        this.data = null;
    }

    /**
     * Makes a new typed Node object containing a data object.
     * @param label a string describing type of the Node.
     * @param data a NodeData object containing data for this node.
     */
    @Contract("null, _ -> fail; !null, null -> fail")
    public Node(String label, T data){

        assert(type != null && data != null);

        this.id = "node-"+UUID.randomUUID().toString();
        this.type = label;
        this.data = data;
    }

    /**
     * @return a copy of this node but with a new/different ID.
     */
    public Node<T> clone(){

        if(this.data != null && this.type != null) {
            return new Node<T>(this.getLabel(), (T) this.getData().clone());
        }else if(this.type != null){
            return new Node<T>(this.getLabel());
        }else{
            return new Node<T>();
        }
    }

    // --------- GETTERS AND SETTERS ----------------

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getLabel() {
        return type;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
