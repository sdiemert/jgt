package com.sdiemert.jgt;

import com.microsoft.z3.*;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.HashSet;

public class Matcher {

    // Each matcher keeps its own Context object. This may be memory intensive
    // but will help when implementing concurrent graph matches.
    Context ctx = null;

    ArrayList<String> nodeLabels = null;
    ArrayList<String> edgeLabels = null;
    ArrayList<NodeData> nodeData = null;

    int gEdgeBVSize = -1;
    int gNodeBVSize = -1;

    int hEdgeBVSize = -1;
    int hNodeBVSize = -1;

    int nodeLabelBVSize = -1;
    int edgeLabelBVSize = -1;
    int nodeDataBVSize = -1;

    FuncDecl gSrc = null;
    FuncDecl gTar = null;
    FuncDecl gNodeLabel = null;
    FuncDecl gEdgeLabel = null;
    FuncDecl gNodeData = null;

    FuncDecl hSrc = null;
    FuncDecl hTar = null;
    FuncDecl hNodeLabel = null;
    FuncDecl hEdgeLabel = null;
    FuncDecl hNodeData = null;

    BoolExpr graphExpr = null;

    BoolExpr restrictions = null;

    FuncDecl nodeMap = null;
    FuncDecl edgeMap = null;


    public Matcher(){
        ctx = null;
    }

    public Morphism findMorphism(Graph g, Graph h){

        // 0) Check for obvious signs that there is no match of Graph g in Graph h

        // 0.1) If there are more edges in g than there are in h there cannot be a match.
        if(g.getEdges().size() > h.getEdges().size()) return null;

        // 0.2) If there are more nodes in g than there are nodes in h there cannot be a match.
        if(g.getNodes().size() > h.getNodes().size()) return null;

        // 1) Reset the matcher to ensure if it is a known state.
        this.reset();

        // 2) Combine labels from both graphs.
        this.unifyLabels(g, h);

        // 3) Determine bit lengths to use for Z3 BitVector representations
        this.determineBitLengths(g, h);

        // 4) Convert both graphs into a Z3 functional representation, results are stored in class properties.
        this.graphsToZ3(g, h);

        // 5) Create edge and node mapping functions (these are what we query Z3 with).
        this.createMappings();

        // 6) Restrict the mapping functions to get an isomorphism.
        this.addRestrictions(g, h);

        // 7) Query Z3
        Solver s = ctx.mkSolver();

        s.add(this.graphExpr, this.restrictions);

        Status ret = s.check();

        // 8) Handle results of Z3

        if(ret == Status.SATISFIABLE){
            return this.makeMorphismFromModel(g, h, s.getModel());
        }else{
            return null;
        }
    }

    private Morphism makeMorphismFromModel(Graph g, Graph h, Model model) {

        Morphism m = new Morphism();

        Expr e;

        for (int i = 0; i < g.getNodes().size(); i++){
            e = model.evaluate(ctx.mkApp(nodeMap, mkBVNum(i, gNodeBVSize)), false);
            m.addNodeMapping(i, ((BitVecNum) e).getInt());
        }

        for (int i = 0; i < g.getNodes().size(); i++){
            e = model.evaluate(ctx.mkApp(edgeMap, mkBVNum(i, gEdgeBVSize)), false);
            m.addEdgeMapping(i, ((BitVecNum) e).getInt());
        }

        return m;
    }

    private void addRestrictions(Graph g, Graph h) {

        // utility vars for making forall quantifiers
        BitVecExpr[] vars = new BitVecExpr[2];
        BoolExpr body = null;

        // accumulate expressions in this list.
        ArrayList<BoolExpr> exprs = new ArrayList<BoolExpr>();

        // 1) Create free variables for restrictions
        BitVecExpr e = ctx.mkBVConst("e", this.gEdgeBVSize);
        BitVecExpr e2 = ctx.mkBVConst("e2", this.gEdgeBVSize);
        BitVecExpr n = ctx.mkBVConst("n", this.gNodeBVSize);
        BitVecExpr n2 = ctx.mkBVConst("n2", this.gNodeBVSize);
        BitVecExpr nl = ctx.mkBVConst("nl", this.nodeLabelBVSize);
        BitVecExpr el = ctx.mkBVConst("el", this.edgeLabelBVSize);
        BitVecExpr nd = ctx.mkBVConst("nd", this.nodeDataBVSize);

        // 2) Impose restrictions on free variables
        exprs.add(ctx.mkBVUGE(e, this.mkBVNum(0, this.gEdgeBVSize)));
        exprs.add(ctx.mkBVULE(e, this.mkBVNum(g.getEdges().size() - 1, this.gEdgeBVSize)));

        exprs.add(ctx.mkBVUGE(e2, this.mkBVNum(0, this.gEdgeBVSize)));
        exprs.add(ctx.mkBVULE(e2, this.mkBVNum(g.getEdges().size() - 1, this.gEdgeBVSize)));

        exprs.add(ctx.mkBVUGE(n, this.mkBVNum(0, this.gNodeBVSize)));
        exprs.add(ctx.mkBVULE(n, this.mkBVNum(g.getNodes().size() - 1, this.gNodeBVSize)));

        exprs.add(ctx.mkBVUGE(n2, this.mkBVNum(0, this.gNodeBVSize)));
        exprs.add(ctx.mkBVULE(n2, this.mkBVNum(g.getNodes().size() - 1, this.gNodeBVSize)));

        exprs.add(ctx.mkBVUGE(nl, this.mkBVNum(0, this.nodeLabelBVSize)));
        exprs.add(ctx.mkBVULE(nl, this.mkBVNum(this.nodeLabels.size() - 1, this.nodeLabelBVSize)));

        exprs.add(ctx.mkBVUGE(el, this.mkBVNum(0, this.edgeLabelBVSize)));
        exprs.add(ctx.mkBVULE(el, this.mkBVNum(this.edgeLabels.size() - 1, this.edgeLabelBVSize)));

        exprs.add(ctx.mkBVUGE(nd, this.mkBVNum(0, this.nodeDataBVSize)));
        exprs.add(ctx.mkBVULE(nd, this.mkBVNum(this.nodeData.size() - 1, this.nodeDataBVSize)));

        // 3) Impose isomorphic constraints

        // 3.1) Isomorphic constraints on edges
        vars[0] = e;
        vars[1] = e2;

        body = ctx.mkImplies(
            ctx.mkEq(ctx.mkApp(edgeMap, e), ctx.mkApp(edgeMap, e2)),
            ctx.mkEq(e, e2));

        exprs.add(ctx.mkForall(vars, body, 0, null, null, null, null));

        // 3.2) Isomorphic constraint on nodes
        vars[0] = n;
        vars[1] = n2;

        body = ctx.mkImplies(
                ctx.mkEq(ctx.mkApp(nodeMap, n), ctx.mkApp(nodeMap, n2)),
                ctx.mkEq(n, n2));

        exprs.add(ctx.mkForall(vars, body, 0, null, null, null, null));

        // 4) Impose node labelling constraint
        vars[0] = n;
        vars[1] = nl;

        body = ctx.mkImplies(
                ctx.mkEq(ctx.mkApp(gNodeLabel, n), nl),
                ctx.mkEq(ctx.mkApp(hNodeLabel, ctx.mkApp(nodeMap, n)), nl));

        exprs.add(ctx.mkForall(vars, body, 0, null, null, null, null));

        // 5) Impose node data constraint
        vars[0] = n;
        vars[1] = nd;

        body = ctx.mkImplies(
                ctx.mkEq(ctx.mkApp(gNodeData, n), nd),
                ctx.mkEq(ctx.mkApp(hNodeData, ctx.mkApp(nodeMap, n)), nd));

        exprs.add(ctx.mkForall(vars, body, 0, null, null, null, null));

        // 6) Impose edge constraints

        // Only apply these constraints if we actually have edges in the graph.
        if(g.getEdges().size() > 0){

            // 6.1) edge src constraint
            vars[0] = e;
            vars[1] = n;
            body = ctx.mkImplies(
                    ctx.mkEq(ctx.mkApp(gSrc, e), n),
                    ctx.mkEq(ctx.mkApp(hSrc, ctx.mkApp(edgeMap, e)), ctx.mkApp(nodeMap, n)));
            exprs.add(ctx.mkForall(vars, body, 0, null, null, null, null));

            // 6.2) edge tar constraint
            vars[0] = e;
            vars[1] = n;
            body = ctx.mkImplies(
                    ctx.mkEq(ctx.mkApp(gTar, e), n),
                    ctx.mkEq(ctx.mkApp(hTar, ctx.mkApp(edgeMap, e)), ctx.mkApp(nodeMap, n)));
            exprs.add(ctx.mkForall(vars, body, 0, null, null, null, null));

            // 6.3) edge labels
            vars[0] = e;
            vars[1] = el;
            body = ctx.mkImplies(
                    ctx.mkEq(ctx.mkApp(gEdgeLabel, e), el),
                    ctx.mkEq(ctx.mkApp(hEdgeLabel, ctx.mkApp(edgeMap, e)), el));
            exprs.add(ctx.mkForall(vars, body, 0, null, null, null, null));

            // 6.4) complete edge function
            for(int i = 0; i < g.getEdges().size(); i++){
                exprs.add(ctx.mkBVULE((BitVecExpr)ctx.mkApp(edgeMap, mkBVNum(i, gEdgeBVSize)), mkBVNum(h.getEdges().size() - 1, hEdgeBVSize)));
            }

        }

        // 7) complete node function
        for(int i = 0; i < g.getNodes().size(); i++){
            exprs.add(ctx.mkBVULE((BitVecExpr)ctx.mkApp(nodeMap, mkBVNum(i, gNodeBVSize)), mkBVNum(h.getNodes().size() - 1, hNodeBVSize)));
        }

        // 7) Store the results in class property.
        this.restrictions = ctx.mkAnd(exprs.toArray(new BoolExpr[exprs.size()]));

    }

    /**
     * Creates mapping function declarations. Puts the results
     * in the nodeMap and edgeMap class properties.
     */
    private void createMappings() {

        // Maps from nodes of graph g to nodes of graph h.
        this.nodeMap = ctx.mkFuncDecl(
                "nodeMap",
                ctx.mkBitVecSort(this.gNodeBVSize),
                ctx.mkBitVecSort(this.hNodeBVSize));

        this.edgeMap = ctx.mkFuncDecl(
                "edgeMap",
                ctx.mkBitVecSort(this.gEdgeBVSize),
                ctx.mkBitVecSort(this.hEdgeBVSize));
    }

    /**
     * This method converts Graph objects into a Z3 representation
     * and stores the results in class properties (e.g., gSrc, hSrc, graphExpr etc.)
     *
     * It is assumed that the bit vector sizes, node labels, edge labels,
     * and node data values are already populated correct.
     *
     * @param g the matching Graph
     * @param h the host Graph.
     */
    @Contract("null, _ -> fail; !null, null -> fail")
    void graphsToZ3(Graph g, Graph h){

        assert(this.gNodeBVSize != -1);
        assert(this.gEdgeBVSize != -1);
        assert(this.hNodeBVSize != -1);
        assert(this.hEdgeBVSize != -1);
        assert(this.nodeLabelBVSize != -1);
        assert(this.nodeDataBVSize != -1);
        assert(this.edgeLabelBVSize != -1);
        assert(this.nodeData != null);
        assert(this.edgeLabels != null);
        assert(this.nodeLabels != null);
        assert(g != null);
        assert(h != null);

        // a list of expressions that describe the graph, these will be and'd together
        // eventually.
        ArrayList<BoolExpr> exprs = new ArrayList<BoolExpr>();

        // 1) Define the graph functions

        // 1.1) Source functions

        // Maps from Edges to Nodes in g
        this.gSrc = ctx.mkFuncDecl(
                "gSrc",
                ctx.mkBitVecSort(this.gEdgeBVSize),
                ctx.mkBitVecSort(this.gNodeBVSize));

        // Maps from Edges of h to Nodes of h.
        this.hSrc = ctx.mkFuncDecl(
                "hSrc",
                ctx.mkBitVecSort(this.hEdgeBVSize),
                ctx.mkBitVecSort(this.hNodeBVSize));


        // 1.2) Target functions

        // Maps from Edges of g to Nodes of g.
        this.gTar = ctx.mkFuncDecl(
                "gTar",
                ctx.mkBitVecSort(this.gEdgeBVSize),
                ctx.mkBitVecSort(this.gNodeBVSize));

        // Maps from Edges of h to Nodes of h.
        this.hTar = ctx.mkFuncDecl(
                "hTar",
                ctx.mkBitVecSort(this.hEdgeBVSize),
                ctx.mkBitVecSort(this.hNodeBVSize));

        // 1.3) Edge Label Functions

        // Maps from Edges of g to indexes of edge labels
        this.gEdgeLabel = ctx.mkFuncDecl(
                "gEdgeLabel",
                ctx.mkBitVecSort(this.gEdgeBVSize),
                ctx.mkBitVecSort(this.edgeLabelBVSize));

        // Maps from Edges of h to indexes of edge labels
        this.hEdgeLabel = ctx.mkFuncDecl(
                "hEdgeLabel",
                ctx.mkBitVecSort(this.hEdgeBVSize),
                ctx.mkBitVecSort(this.edgeLabelBVSize));

        // 1.4) Node Label Functions

        // Maps from Nodes of g to indexes of node labels
        this.gNodeLabel = ctx.mkFuncDecl(
                "gNodeLabel",
                ctx.mkBitVecSort(this.gNodeBVSize),
                ctx.mkBitVecSort(this.nodeLabelBVSize));

        // Maps from Nodes of h to indexes of node labels
        this.hNodeLabel = ctx.mkFuncDecl(
                "hNodeLabel",
                ctx.mkBitVecSort(this.hNodeBVSize),
                ctx.mkBitVecSort(this.nodeLabelBVSize));

        // 1.5) Node Data Functions

        // Maps from Nodes of g to indexes of node data
        this.gNodeData = ctx.mkFuncDecl(
                "gNodeData",
                ctx.mkBitVecSort(this.gNodeBVSize),
                ctx.mkBitVecSort(this.nodeDataBVSize));

        // Maps from Nodes of h to indexes of node data
        this.hNodeData = ctx.mkFuncDecl(
                "hNodeData",
                ctx.mkBitVecSort(this.hNodeBVSize),
                ctx.mkBitVecSort(this.nodeDataBVSize));

        // 2) Define the values of the functions for known mappings.

        // 2.1) Define edge based functions for Graph g
        for (int i = 0; i < g.getEdges().size(); i++){
            // gSrc(e) == e.src
            exprs.add(ctx.mkEq(
                        this.mkUnaryBVFunctionApp(this.gSrc, i, this.gEdgeBVSize),
                        this.mkBVNum(g.getNodes().indexOf(g.getEdges().get(i).getSrc()), this.gNodeBVSize)));

            // gTar(e) == e.tar
            exprs.add(ctx.mkEq(
                    this.mkUnaryBVFunctionApp(this.gTar, i, this.gEdgeBVSize),
                    this.mkBVNum(g.getNodes().indexOf(g.getEdges().get(i).getTar()), this.gNodeBVSize)));

            // gEdgeLabel(e) == e.label
            exprs.add(ctx.mkEq(
                    this.mkUnaryBVFunctionApp(this.gEdgeLabel, i, this.gEdgeBVSize),
                    this.mkBVNum(this.edgeLabels.indexOf(g.getEdges().get(i).getLabel()), this.edgeLabelBVSize)));
        }

        // 2.2) Define edge based functions for Graph h
        for (int i = 0; i < h.getEdges().size(); i++){
            // hSrc(e) == e.src
            exprs.add(ctx.mkEq(
                    this.mkUnaryBVFunctionApp(this.hSrc, i, this.hEdgeBVSize),
                    this.mkBVNum(h.getNodes().indexOf(h.getEdges().get(i).getSrc()), this.hNodeBVSize)));

            // hTar(e) == e.tar
            exprs.add(ctx.mkEq(
                    this.mkUnaryBVFunctionApp(this.hTar, i, this.hEdgeBVSize),
                    this.mkBVNum(h.getNodes().indexOf(h.getEdges().get(i).getTar()), this.hNodeBVSize)));

            // hEdgeLabel(e) == e.label
            exprs.add(ctx.mkEq(
                    this.mkUnaryBVFunctionApp(this.hEdgeLabel, i, this.hEdgeBVSize),
                    this.mkBVNum(this.edgeLabels.indexOf(h.getEdges().get(i).getLabel()), this.edgeLabelBVSize)));
        }

        // NOTE: functions in Z3 are total, i.e., each input must have an output value.
        //       This means we must provide a mapping for all values that are part of the bit vector
        //       input domain but that we don't actually care about. These values won't matter
        //       when we actually apply the solver since we will restrict the values we actually
        //       care about to the number of nodes/edges.

        // 2.3) Complete edge functions for Graph g
        for (int i = g.getEdges().size(); i < this.gEdgeBVSize; i++){
            // gSrc(e) == 0
            exprs.add(ctx.mkEq(
                    this.mkUnaryBVFunctionApp(this.gSrc, i, this.gEdgeBVSize),
                    this.mkBVNum(0, this.gNodeBVSize)));

            // gTar(e) == 0
            exprs.add(ctx.mkEq(
                    this.mkUnaryBVFunctionApp(this.gTar, i, this.gEdgeBVSize),
                    this.mkBVNum(0, this.gNodeBVSize)));

            // gEdgeLabel(e) == 0
            exprs.add(ctx.mkEq(
                    this.mkUnaryBVFunctionApp(this.gEdgeLabel, i, this.gEdgeBVSize),
                    this.mkBVNum(0, this.nodeLabelBVSize)));
        }

        // 2.4) Complete edge functions for Graph h
        for (int i = h.getEdges().size(); i < this.hEdgeBVSize; i++){
            // hSrc(e) == e.src
            exprs.add(ctx.mkEq(
                    this.mkUnaryBVFunctionApp(this.hSrc, i, this.hEdgeBVSize),
                    this.mkBVNum(0, this.hNodeBVSize)));

            // hTar(e) == e.tar
            exprs.add(ctx.mkEq(
                    this.mkUnaryBVFunctionApp(this.hTar, i, this.hEdgeBVSize),
                    this.mkBVNum(0, this.hNodeBVSize)));

            // hEdgeLabel(e) == e.label
            exprs.add(ctx.mkEq(
                    this.mkUnaryBVFunctionApp(this.hEdgeLabel, i, this.hEdgeBVSize),
                    this.mkBVNum(0, this.nodeLabelBVSize)
                    ));
        }

        // 2.5) Define node based function values for Graph g
        for (int i = 0; i < g.getNodes().size(); i++){
            exprs.add(ctx.mkEq(
                    this.mkUnaryBVFunctionApp(this.gNodeLabel, i, this.gNodeBVSize),
                    this.mkBVNum(this.nodeLabels.indexOf(g.getNodes().get(i).getLabel()), this.nodeLabelBVSize)));

            exprs.add(ctx.mkEq(
                    this.mkUnaryBVFunctionApp(this.gNodeData, i, this.gNodeBVSize),
                    this.mkBVNum(this.nodeData.indexOf(g.getNodes().get(i).getData()), this.nodeDataBVSize)));
        }

        // 2.6) Define node based function values for Graph h
        for (int i = 0; i < h.getNodes().size(); i++){
            exprs.add(ctx.mkEq(
                    this.mkUnaryBVFunctionApp(this.hNodeLabel, i, this.hNodeBVSize),
                    this.mkBVNum(this.nodeLabels.indexOf(h.getNodes().get(i).getLabel()), this.nodeLabelBVSize)));

            exprs.add(ctx.mkEq(
                    this.mkUnaryBVFunctionApp(this.hNodeData, i, this.hNodeBVSize),
                    this.mkBVNum(this.nodeData.indexOf(h.getNodes().get(i).getData()), this.nodeDataBVSize)));
        }

        // See comment about for note about completing total functions in Z3.

        // 2.7) Complete node based functions for Graph g
        for (int i = g.getNodes().size(); i < this.gNodeBVSize; i++){
            exprs.add(ctx.mkEq(
                    this.mkUnaryBVFunctionApp(this.gNodeLabel, i, this.gNodeBVSize),
                    this.mkBVNum(0, this.nodeLabelBVSize)));

            exprs.add(ctx.mkEq(
                    this.mkUnaryBVFunctionApp(this.gNodeData, i, this.gNodeBVSize),
                    this.mkBVNum(0, this.nodeDataBVSize)));
        }

        // 2.8) Complete node based functions for Graph g
        for (int i = h.getNodes().size(); i < this.hNodeBVSize; i++){
            exprs.add(ctx.mkEq(
                    this.mkUnaryBVFunctionApp(this.hNodeLabel, i, this.hNodeBVSize),
                    this.mkBVNum(0, this.nodeLabelBVSize)));

            exprs.add(ctx.mkEq(
                    this.mkUnaryBVFunctionApp(this.hNodeData, i, this.hNodeBVSize),
                    this.mkBVNum(0, this.nodeDataBVSize)));
        }

        this.graphExpr = ctx.mkAnd(exprs.toArray(new BoolExpr[exprs.size()]));
    }

    void determineBitLengths(Graph g, Graph h){

        this.gEdgeBVSize = g.getEdges().size() > 1 ? Matcher.numberOfBits(g.getEdges().size() - 1): 1;
        this.gNodeBVSize = g.getNodes().size() > 1 ? Matcher.numberOfBits(g.getNodes().size() - 1): 1;

        this.hEdgeBVSize = h.getEdges().size() > 1 ? Matcher.numberOfBits(h.getEdges().size() - 1): 1;
        this.hNodeBVSize = h.getNodes().size() > 1 ? Matcher.numberOfBits(h.getNodes().size() - 1): 1;

        this.nodeLabelBVSize = this.nodeLabels.size() > 1 ? Matcher.numberOfBits(this.nodeLabels.size() - 1): 1;
        this.edgeLabelBVSize = this.edgeLabels.size() > 1 ? Matcher.numberOfBits(this.edgeLabels.size() - 1): 1;
        this.nodeDataBVSize = this.nodeData.size() > 1 ? Matcher.numberOfBits(this.nodeData.size() - 1) : 1;
    }

    void unifyLabels(Graph g, Graph h){
        this.unifyNodeLabels(g, h);
        this.unifyEdgeLabels(g, h);
        this.unifyNodeData(g, h);
    }

    void unifyNodeLabels(Graph g, Graph h){
        HashSet<String> hs = new HashSet<String>();
        hs.addAll(g.getUniqueNodeLabels());
        hs.addAll(h.getUniqueNodeLabels());
        this.nodeLabels.addAll(hs);
    }

    void unifyEdgeLabels(Graph g, Graph h){
        HashSet<String> hs = new HashSet<String>();
        hs.addAll(g.getUniqueEdgeLabels());
        hs.addAll(h.getUniqueEdgeLabels());
        this.edgeLabels.addAll(hs);
    }

    void unifyNodeData(Graph g, Graph h){
        this.nodeData.addAll(g.getUniqueNodeData());
        for(NodeData d : h.getUniqueNodeData()){
            if(!this.nodeData.contains(d)){
                this.nodeData.add(d);
            }
        }
    }

    public void reset(){

        if(ctx != null){
            ctx.close();
        }

        ctx = new Context();

        this.nodeLabels = new ArrayList<String>();
        this.edgeLabels = new ArrayList<String>();
        this.nodeData = new ArrayList<NodeData>();

        this.gEdgeBVSize = -1;
        this.gNodeBVSize = -1;
        this.hEdgeBVSize = -1;
        this.hNodeBVSize = -1;
        this.nodeDataBVSize = -1;
        this.edgeLabelBVSize = -1;
        this.nodeLabelBVSize = -1;

        this.gSrc = null;
        this.gTar = null;
        this.gNodeData = null;
        this.gNodeLabel = null;
        this.gEdgeLabel = null;

        this.hSrc = null;
        this.hTar = null;
        this.hNodeData = null;
        this.hNodeLabel = null;
        this.hEdgeLabel = null;

        this.graphExpr = null;

    }

    @Contract(pure = true)
    static int numberOfBits(int i){
        assert(i >= 0);
        return Integer.SIZE - Integer.numberOfLeadingZeros(i);
    }

    private Expr mkUnaryBVFunctionApp(FuncDecl f, int arg, int argBVSize){
        return ctx.mkApp(f, ctx.mkNumeral(arg, ctx.mkBitVecSort(argBVSize)));
    }

    private Expr mkUnaryBVFunctionApp(FuncDecl f, BitVecExpr arg){
        return ctx.mkApp(f, arg);
    }

    private BitVecExpr mkBVNum(int val, int bVSize){
        return (BitVecExpr) ctx.mkNumeral(val, ctx.mkBitVecSort(bVSize));
    }

}
