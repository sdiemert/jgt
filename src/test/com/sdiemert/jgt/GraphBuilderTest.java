package com.sdiemert.jgt;

import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.*;

public class GraphBuilderTest {

    @Test
    public void testFromMatrix() throws GraphException{

        boolean[][] M = {{false, true},
                          {false, false}};

        GraphBuilder gb = new GraphBuilder();
        Graph g = gb.fromMatrix(M, 2);

        assertNotNull(g);
        assertEquals(2, g.getNodes().size());
        assertEquals(1, g.getEdges().size());
        assertEquals(g.getEdges().get(0), g.adjacentList(g.getNodes().get(0), g.getNodes().get(1)).get(0));
    }

    @Test(expected = GraphException.class)
    public void testFromMatrixNotSqaureMatrix() throws GraphException{
        boolean[][] M = {{false, true, false},
                {false, false}};
        GraphBuilder gb = new GraphBuilder();
        gb.fromMatrix(M, 2);
    }

    @Test(expected = GraphException.class)
    public void testFromMatrixSizeNotEqualToRows() throws GraphException{
        boolean[][] M = {{false, true},
                {false, false}};
        GraphBuilder gb = new GraphBuilder();
        gb.fromMatrix(M, 1);
    }

}