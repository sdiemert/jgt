package com.sdiemert.jgt.util;

import com.sdiemert.jgt.core.Edge;
import com.sdiemert.jgt.core.Graph;
import com.sdiemert.jgt.core.GraphException;
import com.sdiemert.jgt.core.Node;

public class GraphBuilder {

    /**
     * Builds a simple graph from an adjacency matrix where each element is a boolean.
     * The generated graph will have all node and edge labels/data as null.
     *
     * @param M boolean square matrix, true indicates node at 1st dim index is adjacent to node a 2nd dim index.
     * @param size the size of the square matrix, also number of nodes in the graph.
     * @return a Graph object derived from the matrix.
     *
     * @throws GraphException if there was an error generating the Graph.
     */
    public Graph fromMatrix(boolean[][] M, int size) throws GraphException{

        Graph g = new Graph();

        for(int i = 0; i < size; i++){
            g.addNode(new Node());
        }

        for(int i = 0; i < size; i++){

            if(M[i].length != size) throw new GraphException("Matrix must be square");

            for(int j = 0; j < size; j++){
                if(M[i][j]){
                    g.addEdge(new Edge(g.getNodes().get(i), g.getNodes().get(j)));
                }
            }
        }
        return g;
    }

    /**
     * Builds a simple graph from an adjacency matrix where each element is a String or null.
     * The generated graph will have all edge's labeled according to the Matrix. Node labels are
     * created based on the nodeLabel input array. The graph will not have data on nodes.
     *
     * @param M the square matrix, non-null elements indicate an edge from node in 1st dim to node in 2nd dim
     *          with edge label of the String. Size of matrix must be equal to size parameter.
     *
     * @param nodeLabels an array of node label strings, must be length of the size parameter.
     * @param size size of the nodeLabel array and square adjacency matrix.
     * @return the corresponding Graph.
     *
     * @throws GraphException if there was an error generating the Graph.
     */
    public Graph fromMatrix(String[][] M, String[] nodeLabels, int size) throws GraphException{

        assert(M.length == size && nodeLabels.length == size);

        Graph g = new Graph();

        for(int i = 0; i < size; i++){
            g.addNode(new Node(nodeLabels[i]));
        }

        for(int i = 0; i < size; i++){

            if(M[i].length != size) throw new GraphException("Matrix must be square");

            for(int j = 0; j < size; j++){
                if(M[i][j] != null){
                    g.addEdge(new Edge(g.getNodes().get(i), g.getNodes().get(j), M[i][j]));
                }
            }
        }

        return g;
    }
}
