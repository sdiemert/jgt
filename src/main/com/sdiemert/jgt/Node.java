package com.sdiemert.jgt;

import java.util.UUID;

public class Node {

    private String id;

    // --------- CONSTUCTORS ------------------------

    public Node() {
        this.id = "node-"+UUID.randomUUID().toString();
    }

    // --------- GETTERS AND SETTERS ----------------

    public String getId() {
        return id;
    }
}
