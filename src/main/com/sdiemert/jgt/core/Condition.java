package com.sdiemert.jgt.core;

/**
 * A Graph Condition is a sub-graph that can be matched into a host graph. The condition itself
 * does not make changes to the host graph, but can be evaluated for a match.
 */
public class Condition {

    Graph matchGraph;

    protected Matcher matcher;
    protected String name;

    /**
     * Creates a new Condition object based on the provided Graph.
     *
     * @param g the Graph to make the Condition out of.
     */
    public Condition(Graph g){
        this.matchGraph = g;
        this.matcher = new Matcher();
    }

    /**
     * Determines if there is a match between this rule's LHS graph and the provided host graph. Does not change
     * the host graph. Invoke Rule.apply(Graph, Morphism) to apply the match.
     *
     * @param host the Graph to find the match in.
     * @return a Morphism mapping between the LHS of the rule graph to the host graph.
     */
    public Morphism findMatch(Graph host){
        this.matcher.reset();
        return this.matcher.findMorphism(this.matchGraph, host);
    }

    /**
     * Determines if the Condition applies to the given host graph.
     * @param host the Graph to check.
     * @return a Morphism is there is match, null otherwise.
     */
    public Morphism applies(Graph host){
        return this.findMatch(host);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Graph getMatchGraph(){
        return this.matchGraph;
    }
}
