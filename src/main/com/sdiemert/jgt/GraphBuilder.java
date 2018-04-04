package com.sdiemert.jgt;

public class GraphBuilder {

    /**
     * Builds a simple graph from an adjacency matrix where each element is a boolean.
     * The generated graph will have all node and edge labels/data as null.
     *
     * @param M boolean square matrix, true indicates node at 1st dim index is adjacent to node a 2nd dim index.
     * @param size the size of the square matrix, also number of nodes in the graph.
     * @return a Graph object derived from the matrix.
     */
    Graph fromMatrix(boolean[][] M, int size) throws GraphException{

        assert(M.length == size);

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
}
