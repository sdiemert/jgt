package com.sdiemert.jgt.analysis;

import com.sdiemert.jgt.core.Condition;

public class ConditionVerifier {

    public VerificationResult check(Condition cond){

        VerificationResult result;

        if((result = this.checkMatchGraph(cond)).isFail()){
            return result;
        }

        return (new VerificationResult()).pass();

    }

    protected VerificationResult checkMatchGraph(Condition cond){
        GraphVerifier gv = new GraphVerifier();
        return gv.check(cond.getMatchGraph());
    }
}

