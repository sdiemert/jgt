package com.sdiemert.jgt;

import org.jetbrains.annotations.Contract;

import java.util.UUID;

public class Edge {

    private Node src;
    private Node tar;
    private String id;

    // ------------ Constructors --------------

    /**
     * Creates a new Edge object that joins two nodes.
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
}
