package com.sdiemert.jgt.core;

import com.sdiemert.jgt.core.IntNodeData;
import com.sdiemert.jgt.core.StringNodeData;
import org.junit.Test;

import static org.junit.Assert.*;

public class NodeDataTest {

    @Test
    public void testIntCompareShouldReturnTrueForParameters(){
        IntNodeData i = new IntNodeData();
        assertTrue(i.compare(new IntNodeData()));
    }

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
    public void testStringCompareShouldReturnTrueForBothParams(){
        StringNodeData i = new StringNodeData();
        assertTrue(i.compare(new StringNodeData()));
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
        assertNotEquals(i, s);
    }

    @Test
    public void testIntNodeDataIsParameter(){
        IntNodeData i = new IntNodeData();
        assertTrue(i.isParameter());
    }

    @Test
    public void testIntNodeDataIsParameterShouldReturnFalse(){
        IntNodeData i = new IntNodeData(1);
        assertFalse(i.isParameter());
    }

    @Test
    public void testCloneWithParams(){
        IntNodeData i = new IntNodeData();
        IntNodeData newi = i.clone();
        assertNull(newi.getVal());
        assertNull(i.getVal());
    }

    @Test
    public void testCloneStringDataWithParams(){
        StringNodeData n = new StringNodeData();
        StringNodeData n1 = n.clone();
        assertNull(n.getVal());
        assertNull(n1.getVal());
    }

}