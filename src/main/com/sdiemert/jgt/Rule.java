package com.sdiemert.jgt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Rule {

    Graph ruleGraph;
    Graph lhsGraph;

    private List<Node> addNodes;
    private List<Edge> addEdges;
    private List<Node> delNodes;
    private List<Edge> delEdges;

    private Matcher matcher;

    /**
     * Makes a new transformation Rule.
     *
     * @param g the graph describing the Rule.
     * @param addNodes references to Nodes in g that are to be added during rule application.
     * @param addEdges references to Edges in g that are to be added during rule application.
     * @param delNodes references to Nodes in g that are to be deleted during rule application.
     * @param delEdges references to Edges in g that are to be deleted during rule application.
     *
     * @throws GraphException if rule graph or its LHS (without addNodes and addEdges) are not valid.
     */
    public Rule(Graph g, List<Node> addNodes, List<Edge> addEdges, List<Node> delNodes, List<Edge> delEdges) throws GraphException {

        this.ruleGraph  = g;

        this.addNodes = addNodes != null ? addNodes : new ArrayList<Node>();
        this.addEdges = addEdges != null ? addEdges : new ArrayList<Edge>();

        this.delNodes = delNodes != null ? delNodes : new ArrayList<Node>();
        this.delEdges = delEdges != null ? delEdges : new ArrayList<Edge>();

        // TODO: Implement NACs.

        // determine the LHS once, otherwise we would have to do it every time the rule is applied.
        this.determineLHS();

        matcher = new Matcher();

    }

    /**
     * Gets the left hand side of the rule. This is the ruleGraph with the
     * addNodes and addEdges removed.
     *
     * @throws GraphException if the resulting lhsGraph would be invalid.
     */
    private void determineLHS() throws GraphException {

        List<Node> lhsNodes = new ArrayList<Node>();
        List<Edge> lhsEdges = new ArrayList<Edge>();

        // get only those nodes that are not in the addNodes.
        for (Node n : this.ruleGraph.getNodes()) {
            if (!this.addNodes.contains(n)) {
                lhsNodes.add(n);
            }
        }

        for(Edge e : this.ruleGraph.getEdges()){
            if(!this.addEdges.contains(e)){
                lhsEdges.add(e);
            }
        }

        lhsGraph = ruleGraph.subgraph(lhsNodes, lhsEdges);
    }

    /**
     * Applies the rule to the provided host graph, changes are applied in
     * place to the host graph.
     *
     * @param host the graph to apply the transformation too.
     * @return true if the transformation completed, false if rule was not applied there was no match in host graph.
     * @throws GraphException if there is an error applying the rule.
     */
    public boolean apply(Graph host) throws GraphException {
        matcher.reset();
        Morphism morph = matcher.findMorphism(this.lhsGraph, host);
        return apply(host, morph);
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
        return this.matcher.findMorphism(this.lhsGraph, host);
    }

    /**
     * Applies the rule to the provided host graph via the provided morphism,
     * changes are applied to the host graph in place.
     *
     * @param host the graph to apply the transformation too.
     * @param morph a morphism between the rule LHS and the host graph.
     * @return true if the transformation completed, false if rule was not applied there was no match in host graph.
     * @throws GraphException if there is an error applying the rule.
     */
    public boolean apply(Graph host, Morphism morph) throws GraphException {

        // 1) if no match was found return false, leave h unchanged.
        if(morph == null){
            return false;
        }

        if(host == null){
            return false;
        }

        // 2) Determine deletions from the host graph
        List<Node> nodesToDelete = new ArrayList<Node>();
        List<Edge> edgesToDelete = new ArrayList<Edge>();

        // 2.1) determines edges to delete by index.
        for(Edge e : this.delEdges){
            edgesToDelete.add(host.getEdges().get(morph.mapEdge(this.lhsGraph.getEdges().indexOf(e))));
        }

        // 2.2) determine nodes to delete by index.
        for(Node n : this.delNodes){
            Node delNode = host.getNodes().get(morph.mapNode(this.lhsGraph.getNodes().indexOf(n)));

            nodesToDelete.add(delNode);

            // also delete edges that would be left "dangling".
            for(Edge e : host.incident(delNode)){
                if(!edgesToDelete.contains(e)){
                    edgesToDelete.add(e);
                }
            }
        }

        // NOTE: cannot delete the nodes and edges yet as it will upset the indexing
        //       used by the morphism.

        // 3) Add additions to host graph

        // 3.1) Node additions

        HashMap<Node, Node> newNodeMap = new HashMap<Node, Node>();

        for(Node n : this.addNodes){
            Node newNode = n.clone();
            newNodeMap.put(n, newNode);
            host.addNode(newNode);
        }

        // 3.2) Edge additions
        for(Edge e : this.addEdges){
            Edge newEdge = null;
            Node src = null, tar = null;

            if(this.addNodes.contains(e.getSrc())) {
                // the src node is a new node being added by the rule
                // get a reference to it from above.
                src = newNodeMap.get(e.getSrc());
            }else{
                // the src node already exists in the host graph,
                // find it by mapping from the lhs graph to the host graph.
                src = host.getNodes().get(morph.mapNode(this.lhsGraph.getNodes().indexOf(e.getSrc())));
            }

            if(this.addNodes.contains(e.getTar())){
                // the tar node is a new node being added by the rule
                // get a reference from above.
                tar = newNodeMap.get(e.getTar());
            }else{
                // the tar node already exists in the host graph.
                // find it by mapping from the lhs graph to the host graph.
                tar = host.getNodes().get(morph.mapNode(this.lhsGraph.getNodes().indexOf(e.getTar())));
            }

            host.addEdge(new Edge(src, tar, e.getLabel()));
        }

        // 4) Delete the nodes and edges identified above for deletion.

        // 4.1) Delete edges from above
        for(Edge e : edgesToDelete){
            host.deleteEdge(e);
        }

        // 4.2) Delete nodes from above
        for(Node n : nodesToDelete){
            host.deleteNode(n);
        }

        return true;
    }
}
