package com.sdiemert.jgt.core;

import org.jetbrains.annotations.Contract;

import java.util.UUID;

public class Edge {

    private Node src;
    private Node tar;
    private String id;
    private String label;

    // ------------ Constructors --------------

    /**
     * Creates a new directed Edge object that joins two nodes.
     *
     * @param src the source node of the edge.
     * @param tar the target node of the edge.
     */
    @Contract("null, _ -> fail; !null, null -> fail")
    public Edge(Node src, Node tar){

        assert(src != null && tar != null);

        this.src = src;
        this.tar = tar;
        this.id = "edge-" + UUID.randomUUID().toString();
        this.label = "NONE";
    }


    public Edge(String id, Node src, Node tar){

        assert(src != null && tar != null);

        this.src = src;
        this.tar = tar;
        this.id = id;
        this.label = "NONE";
    }

    /**
     * Creates a new directed labelled edge object that joins to nodes.
     * @param src the source node of the edge.
     * @param tar the target node of the edge.
     * @param label a label for the edge.
     */
    @Contract("null, _, _ -> fail; !null, null, _ -> fail; !null, !null, null -> fail")
    public Edge(Node src, Node tar, String label){
        assert(src != null && tar != null && label != null);

        this.src = src;
        this.tar = tar;
        this.id = "edge-" + UUID.randomUUID().toString();
        this.label = label;
    }

    public Edge(String id, Node src, Node tar, String label){
        assert(src != null && tar != null && label != null);

        this.src = src;
        this.tar = tar;
        this.id = id;
        this.label = label;
    }

    // ----------- Getters and Setters ----------

    public Node getSrc() {
        return this.src;
    }

    public Node getTar() {
        return this.tar;
    }

    public String getId(){
        return this.id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
