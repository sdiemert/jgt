package com.sdiemert.jgt.analysis;

import com.sdiemert.jgt.core.Edge;
import com.sdiemert.jgt.core.Node;

import java.util.ArrayList;
import java.util.List;

public class VerificationResult {

    private VerificationResult.VerificationResultType result;
    private String message;
    private List<Node> effectedNodes;
    private List<Edge> effectedEdges;

    public VerificationResult pass() {
        this.result = VerificationResult.VerificationResultType.PASS;
        this.message = "CHECK PASSED";
        this.effectedEdges = null;
        this.effectedNodes = null;
        return this;
    }

    public VerificationResult pass(String msg) {
        this.pass();
        this.message = msg;
        return this;
    }

    public VerificationResult fail(String msg, List<Node> nodes, List<Edge> edges) {
        this.result = VerificationResult.VerificationResultType.FAIL;
        this.message = msg;
        this.effectedNodes = nodes;
        this.effectedEdges = edges;
        return this;
    }

    public VerificationResult fail(VerificationResultType t, String msg, List<Node> nodes, List<Edge> edges) {
        this.fail(msg, nodes, edges);
        this.result = t;
        return this;
    }

    public boolean isFail(){
        return this.result != VerificationResultType.PASS;
    }

    public VerificationResult.VerificationResultType getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public List<Node> getEffectedNodes() {
        return effectedNodes;
    }

    public List<Edge> getEffectedEdges() {
        return effectedEdges;
    }

    public enum VerificationResultType{
        PASS,
        FAIL_INVALID_GRAPH,
        FAIL_ADD_DELETE_OVERLAP,
        FAIL_DUPLICATE_ELEMENT_REFERENCE,
        FAIL;
    }

}
