package com.sdiemert.jgt.analysis;

import com.sdiemert.jgt.core.Edge;
import com.sdiemert.jgt.core.Node;
import com.sdiemert.jgt.core.Rule;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static com.sdiemert.jgt.analysis.VerificationResult.VerificationResultType.*;

public class RuleVerifier {

    /**
     * Executes several integrity checks on the Rule, e.g., no overlap between add and delete nodes.
     *
     * @param rule the Rule to check.
     * @return a VerificationResult object describing the results of the check.
     */
    public VerificationResult check(Rule rule){

        GraphVerifier gv = new GraphVerifier();
        ConditionVerifier cv = new ConditionVerifier();

        VerificationResult result;

        if((result = cv.check(rule)).isFail()) {

            return result;

        } else if((result = gv.check(rule.getRuleGraph())).isFail()){

            return result;

        }else if((result = this.addAndDeleteNodesNotOverlap(rule)).isFail()){

            return result;

        }else if((result = this.addAndDeleteEdgesNotOverlap(rule)).isFail()){

            return result;

        }

        return (new VerificationResult()).pass();
    }

    /**
     * Checks to ensure that add and delete nodes do not overlap, i.e., the intersection of the
     * add and delete node sets is empty.
     *
     * @param rule the Rule to check.
     * @return a VerificationResult object describing the results of the check.
     */
    public VerificationResult addAndDeleteNodesNotOverlap(Rule rule){

        Set<Node> nodeSet = new HashSet<Node>(rule.getAddNodes());
        nodeSet.retainAll(rule.getDelNodes());

        if(nodeSet.size() != 0){
            return new VerificationResult().fail(
                    FAIL_ADD_DELETE_OVERLAP,
                    "Add and delete nodes cannot overlap",
                    new ArrayList<Node>(nodeSet),
                    null);
        }else{
            return (new VerificationResult()).pass();
        }

    }

    /**
     * Checks to ensure that add and delete edges do not overlap, i.e., the intersection of the
     * add and delete edge sets is empty.
     *
     * @param rule the Rule to check.
     * @return a VerificationResult object describing the results of the check.
     */
    public VerificationResult addAndDeleteEdgesNotOverlap(Rule rule){

        Set<Edge> edgeSet = new HashSet<Edge>(rule.getAddEdges());
        edgeSet.retainAll(rule.getDelEdges());

        if(edgeSet.size() != 0){
            return new VerificationResult().fail(
                    FAIL_ADD_DELETE_OVERLAP,
                    "Add and delete edges cannot overlap",
                    null,
                    new ArrayList<Edge>(edgeSet));
        }else{
            return (new VerificationResult()).pass();
        }

    }

}
