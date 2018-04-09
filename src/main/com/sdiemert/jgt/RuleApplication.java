package com.sdiemert.jgt;

public class RuleApplication {

    private Rule rule;
    private Morphism morph;

    /**
     * Creates a record of applying a rule to a graph for a given match.
     *
     * NOTE: this RuleApplication object will only make sense in the context of a Graph that
     * matches the given Morphism. It is up to the calling/client code to track
     * RuleApplications along side the given graph.
     *
     * @param r a reference to the Rule that was applied.
     * @param m a reference to the Morphism that was used.
     */
    public RuleApplication(Rule r, Morphism m){
        this.rule = r;
        this.morph = m;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public Morphism getMorphism() {
        return morph;
    }

    public void setMorphism(Morphism morph) {
        this.morph = morph;
    }
}
