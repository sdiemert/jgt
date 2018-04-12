package com.sdiemert.jgt.analysis;

import com.sdiemert.jgt.core.Edge;
import com.sdiemert.jgt.core.Graph;

import java.util.ArrayList;
import java.util.Arrays;

import com.sdiemert.jgt.analysis.VerificationResult.VerificationResultType;
import com.sdiemert.jgt.core.Node;

import static com.sdiemert.jgt.analysis.VerificationResult.VerificationResultType.FAIL_DUPLICATE_ELEMENT_REFERENCE;

public class GraphVerifier {

    /**
     * Executes several integrity checks on the given Graph.
     *
     * @param g the Graph to check.
     * @return a VerificationResult object describing the results of the check.
     */
    public VerificationResult check(Graph g){

        VerificationResult result;

        if((result = checkGraphDanglingEdges(g)).isFail()){

            return result;

        } else if((result = checkDuplicateEdges(g)).isFail()) {

            return result;

        } else if((result = checkDuplicateNodes(g)).isFail()) {

            return result;

        } else{

            return (new VerificationResult()).pass();

        }
    }

    /**
     * Checks for dangling edges in the Graph, these are Edges which do not have a source or target
     * Node that is also in the Graph. This should not be possible if the API is used correctly, but
     * it is possible that some internal logic messes up the data structures.
     *
     * @param g the Graph to check.
     * @return a VerificationResult describing the result of the check.
     */
    protected VerificationResult checkGraphDanglingEdges(Graph g){

        for(Edge e : g.getEdges()){

            if(!g.getNodes().contains(e.getSrc())){
                return (new VerificationResult()).fail(
                        VerificationResultType.FAIL_INVALID_GRAPH,
                        "Edge must have a source node",
                        null, Arrays.asList(e));
            }

            if(!g.getNodes().contains(e.getTar())){
                return (new VerificationResult()).fail(
                        VerificationResultType.FAIL_INVALID_GRAPH,
                        "Edge must have a target node",
                        null, Arrays.asList(e));
            }
        }

        return (new VerificationResult()).pass();
    }

    /**
     * Checks for duplicate Nodes in the graph, that is multiple references to the same
     * Node object. This is different than *equivalent* nodes which just happen to have the
     * same label and data fields.
     *
     * @param g the Graph to check.
     * @return a VerificationResult describing the result of the check.
     */
    protected VerificationResult checkDuplicateNodes(Graph g){

        ArrayList<Node> dupNodes = new ArrayList<Node>();

        for(int i = 0; i < g.getNodes().size(); i++){
            for(int j = i+1; j < g.getNodes().size(); j++){
                if(g.getNodes().get(i) == g.getNodes().get(j)) dupNodes.add(g.getNodes().get(i));
            }
        }

        if(dupNodes.size() > 0) {
            return (new VerificationResult()).fail(
                    FAIL_DUPLICATE_ELEMENT_REFERENCE,
                    "Cannot have duplicate references to the same Node in a Graph's node list",
                    dupNodes, null
            );
        }

        return (new VerificationResult()).pass();

    }

    /**
     * Checks for duplicate Edges in the graph, that is multiple references to the same
     * Edges object. This is different than *equivalent* Edges which just happen to have the
     * same label field.
     *
     * @param g the Graph to check.
     * @return a VerificationResult describing the result of the check.
     */
    protected VerificationResult checkDuplicateEdges(Graph g){

        ArrayList<Edge> dupEdges = new ArrayList<Edge>();

        for(int i = 0; i < g.getEdges().size(); i++){
            for(int j = i+1; j < g.getEdges().size(); j++){
                if(g.getEdges().get(i) == g.getEdges().get(j)) dupEdges.add(g.getEdges().get(i));
            }
        }

        if(dupEdges.size() > 0) {
            return (new VerificationResult()).fail(
                    FAIL_DUPLICATE_ELEMENT_REFERENCE,
                    "Cannot have duplicate references to the same Edge in a Graph's edge list",
                    null, dupEdges
            );
        }

        return (new VerificationResult()).pass();

    }
}
