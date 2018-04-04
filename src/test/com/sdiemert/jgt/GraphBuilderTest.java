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

    @Test
    public void testFromMatrixWithLabels() throws GraphException{

        String[][] M = {
                {null, "A",  null},
                {null, null, "B"},
                {"C", null, null}
        };

        String[] labels = {"n0", "n1", "n2"};

        GraphBuilder gb = new GraphBuilder();
        Graph g = gb.fromMatrix(M, labels, 3);

        assertNotNull(g);
        assertEquals(3, g.getNodes().size());
        assertEquals(3, g.getEdges().size());
        assertEquals("A", g.getEdges().get(0).getLabel());
        assertEquals("B", g.getEdges().get(1).getLabel());
        assertEquals("C", g.getEdges().get(2).getLabel());
        assertEquals("n0", g.getNodes().get(0).getLabel());
        assertEquals("n1", g.getNodes().get(1).getLabel());
        assertEquals("n2", g.getNodes().get(2).getLabel());

    }

}