package com.sdiemert.jgt;

import org.junit.Test;

import static org.junit.Assert.*;

public class NodeDataTest {

    @Test
    public void testIntCompareShouldReturnTrueForSameInts(){
        IntNodeData i = new IntNodeData(0);
        assertTrue(i.compare(new IntNodeData(0)));
    }

    @Test
    public void testIntCompareShouldReturnFalseForDiffInts(){
        IntNodeData i = new IntNodeData(0);
        assertFalse(i.compare(new IntNodeData(1)));
    }

    @Test
    public void testIntCompareShouldReturnFalseForDiffTypes(){
        IntNodeData i = new IntNodeData(0);
        assertFalse(i.compare(new StringNodeData("foo")));
    }

    @Test
    public void testStringCompareShouldReturnFalseForDiffStrings(){
        StringNodeData i = new StringNodeData("foo");
        assertFalse(i.compare(new StringNodeData("bar")));
    }

    @Test
    public void testStringCompareShouldReturnTrueForSameStrings(){
        StringNodeData i = new StringNodeData("foo");
        assertTrue(i.compare(new StringNodeData("foo")));
    }

    @Test
    public void testStringCompareShouldReturnFalseForDiffTypes(){
        StringNodeData i = new StringNodeData("foo");
        assertFalse(i.compare(new IntNodeData(1)));
    }

    @Test
    public void testEqualsShouldReturnTrueForEqualVals(){
        IntNodeData i = new IntNodeData(1);
        IntNodeData j = new IntNodeData(1);
        assertTrue(i.equals(j));
        assertEquals(i, j);
    }

    @Test
    public void testEqualsShouldReturnFalseForNotNodeData(){
        IntNodeData i = new IntNodeData(1);
        String s = "foo";
        assertFalse(i.equals(s));
    }

}