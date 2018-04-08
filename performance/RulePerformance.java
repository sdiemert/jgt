
import com.sdiemert.jgt.*;

import java.util.ArrayList;

class RulePerformance{

    public static void additionOnly() throws GraphException{
        Graph ruleGraph = new Graph();

        Node n0 = new Node("A");
        Node n1 = new Node("B");
        Node n2 = new Node("C");
        Node n3 = new Node("A");
        Edge e0 = new Edge(n0, n1, "e");
        Edge e1 = new Edge(n1, n2, "e");
        Edge e2 = new Edge(n2, n2, "e");
        ruleGraph.addNodes(n0, n1, n2, n3);
        ruleGraph.addEdges(e0, e1, e2);

        ArrayList<Node> addNodes = new ArrayList<Node>();
        ArrayList<Edge> addEdges = new ArrayList<Edge>();

        addNodes.add(n1);
        addNodes.add(n2);
        addNodes.add(n3);
        addEdges.add(e0);
        addEdges.add(e1);
        addEdges.add(e2);

        Rule r = new Rule(ruleGraph, addNodes, addEdges, null, null);

        Graph host = new Graph();

        host.addNode(new Node("A"));

        long t0, t1;
        boolean ret;

        for(int i = 0; i < 100; i++){
            t0 = System.currentTimeMillis();
            ret = r.apply(host);
            t1 = System.currentTimeMillis();
            System.out.println(i + ", " + ret + ", " + host.getNodes().size() +", "+ host.getEdges().size() + ", "+(t1 - t0) + " ms");
        }
    }

    public static void addAndDelete() throws GraphException{
        Graph ruleGraph = new Graph();
        Node n0 = new Node("A");
        Node n1 = new Node("B");
        Node n2 = new Node("C");
        Node n3 = new Node("A");
        Edge e0 = new Edge(n0, n1, "e");
        Edge e1 = new Edge(n1, n2, "e");
        Edge e2 = new Edge(n2, n2, "e");
        ruleGraph.addNodes(n0, n1, n2, n3);
        ruleGraph.addEdges(e0, e1, e2);

        ArrayList<Node> addNodes = new ArrayList<Node>();
        ArrayList<Edge> addEdges = new ArrayList<Edge>();
        ArrayList<Node> delNodes = new ArrayList<Node>();

        addNodes.add(n1);
        addNodes.add(n2);
        addNodes.add(n3);
        addEdges.add(e0);
        addEdges.add(e1);
        addEdges.add(e2);
        delNodes.add(n0);

        Rule r = new Rule(ruleGraph, addNodes, addEdges, delNodes, null);

        Graph host = new Graph();

        host.addNode(new Node("A"));

        long t0, t1;
        boolean ret;

        for(int i = 0; i < 100; i++){
            t0 = System.currentTimeMillis();
            ret = r.apply(host);
            t1 = System.currentTimeMillis();
            System.out.println(i + ", " + ret + ", " + host.getNodes().size() +", "+ host.getEdges().size() + ", "+(t1 - t0) + " ms");
        }

    }

    public static void main(String args[]) throws GraphException {
        //additionOnly();
        //addAndDelete();
    }
}