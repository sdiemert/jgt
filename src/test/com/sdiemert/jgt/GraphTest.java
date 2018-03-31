package com.sdiemert.jgt;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GraphTest {

    @Test
    public void testConstructShouldProduceEmptyGraph(){
        Graph g = new Graph();
        assertEquals(0, g.getNodes().size());
        assertEquals(0, g.getEdges().size());
    }



}