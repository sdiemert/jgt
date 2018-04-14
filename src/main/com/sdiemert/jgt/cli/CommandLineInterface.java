package com.sdiemert.jgt.cli;

import com.sdiemert.jgt.core.GraphException;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandLineInterface {

    private Matcher rootMatcher = Pattern.compile(
            "\\s*("
            +Commands.VERB_NEW+"|"+Commands.VERB_LOAD+"|"+Commands.VERB_SHOW+"|"+Commands.VERB_DEL+"|"+Commands.VERB_ADD
            +")\\s*(.*)").matcher("");

    private Matcher nounMatcher = Pattern.compile(
            "(" +Commands.NOUN_SYS+"|"+Commands.NOUN_RULE+"|"
                    +Commands.NOUN_COND+"|"+Commands.NOUN_GRAPH+"|"
                    +Commands.NOUN_NODE+"|"+ Commands.NOUN_EDGE
                    +")(s)?\\s*(.*)").matcher("");

    private Matcher assignmentMatcher = Pattern.compile("->\\s*("+Commands.ID+")\\s*").matcher("");

    private Matcher whiteSpaceMatcher = Pattern.compile("\\s*$").matcher("");

    private Matcher exitMatcher = Pattern.compile("\\s*("+Commands.VERB_EXIT+")\\s*").matcher("");

    private Matcher nodeMatcher = Pattern.compile("\"([A-Za-z0-9]+)\"(\\s+(\"([A-Za-z0-9]+)\"|([0-9]+)))?\\s*(.*)").matcher("");

    private Matcher edgeMatcher = Pattern.compile("\\s*("+Commands.ID+")\\s+("+Commands.ID+")\\s+\"([A-Za-z0-9]+)\"\\s*(.*)").matcher("");

    private Matcher addMatcher = Pattern.compile("\\s*(("+Commands.ID+"\\s+)+)to\\s+("+Commands.ID+")").matcher("");

    private Session session;

    public CommandLineInterface(){

        session = new Session();

    }

    public void run(InputStream in, PrintStream out){

        Scanner inputScanner = new Scanner(in);

        String cmd = null;
        boolean exit = false;

        this.printWelcome(out);

        while(!exit){

            this.printPrompt(out);
            cmd = inputScanner.nextLine();

            exit = this.parseCommand(cmd, out);

        }

        printGoodbye(out);
    }

    private boolean parseCommand(String cmd, PrintStream out) {

        String cmdRoot = null;

        exitMatcher.reset(cmd);
        if(exitMatcher.find()) return true;

        rootMatcher.reset(cmd);


        if(rootMatcher.find()){

            cmdRoot = rootMatcher.group(1);

            if(cmdRoot.equals(Commands.VERB_NEW)) parseNewCommand(rootMatcher.group(2), out);
            else if(cmdRoot.equals(Commands.VERB_LOAD)) parseLoadCommand(rootMatcher.group(2), out);
            else if(cmdRoot.equals(Commands.VERB_SHOW)) parseShowCommand(rootMatcher.group(2), out);
            else if(cmdRoot.equals(Commands.VERB_ADD)) parseAddCommand(rootMatcher.group(2), out);
            else printTopLevelCommandErrorMessage(cmd, out);

        }else{

            printTopLevelCommandErrorMessage(cmd, out);

        }

        return false;
    }

    private void parseAddCommand(String cmd, PrintStream out){

        // expect: ID ID ID ... to ID

        addMatcher.reset(cmd);

        if(addMatcher.find()){

            // the first group will be list (space separated) of ids to add.
            // the last group will be the id to add to

            String parentId = addMatcher.group(addMatcher.groupCount());

            for(String id : addMatcher.group(1).split("\\s+")){
                try {
                    session.add(parentId, id);
                }catch(GraphException e){
                    out.println(e.getMessage());
                }
            }

        }else{

            printAddCommandErrorMessage(cmd, out);

        }

    }

    private void parseShowCommand(String cmd, PrintStream out) {

        String noun = null;
        nounMatcher.reset(cmd);
        whiteSpaceMatcher.reset(cmd);

        if(nounMatcher.find()) {

            noun = nounMatcher.group(1);

            if (noun.equals(Commands.NOUN_SYS)) showSystems(out);
            else if (noun.equals(Commands.NOUN_GRAPH)) showGraphs(out);
            else if (noun.equals(Commands.NOUN_RULE)) showRules(out);
            else if (noun.equals(Commands.NOUN_COND)) showConditions(out);
            else if (noun.equals(Commands.NOUN_NODE)) showNodes(out);
            else if (noun.equals(Commands.NOUN_EDGE)) showEdges(out);
            else {
                printShowErrorMessage(cmd, out);
            }

        }else if(whiteSpaceMatcher.find()){

            showSystems(out);
            showGraphs(out);
            showRules(out);
            showConditions(out);
            showNodes(out);
            showEdges(out);

        }else{
            printShowErrorMessage(cmd, out);
        }

    }

    private void parseLoadCommand(String cmd, PrintStream out) {
        // TODO: implement me...
        out.println("load command not yet implemented");
    }

    private void parseNewCommand(String cmd, PrintStream out) {

        String noun = null;
        nounMatcher.reset(cmd);

        try {

            if (nounMatcher.find()) {
                noun = nounMatcher.group(1);
                if (noun.equals(Commands.NOUN_SYS)) {
                    parseNewSystemCommand(nounMatcher.group(3), out);
                } else if (noun.equals(Commands.NOUN_GRAPH)) {
                    parseNewGraphCommand(nounMatcher.group(3), out);
                } else if (noun.equals(Commands.NOUN_RULE)) {
                    parseNewRuleCommand(nounMatcher.group(3), out);
                } else if (noun.equals(Commands.NOUN_COND)) {
                    parseNewCondCommand(nounMatcher.group(3), out);
                } else if (noun.equals(Commands.NOUN_NODE)) {
                    parseNewNodeCommand(nounMatcher.group(3), out);
                } else if (noun.equals(Commands.NOUN_EDGE)) {
                    parseNewEdgeCommand(nounMatcher.group(3), out);
                }
            } else {
                printNewCommandErrorMessage(cmd, out);
            }
        }catch(Exception e){
            out.println(e.getMessage());
        }
    }

    private void parseNewSystemCommand(String cmd, PrintStream out)  throws Exception{

        String id = null;
        assignmentMatcher.reset(cmd);
        whiteSpaceMatcher.reset(cmd);

        if(assignmentMatcher.find()){

            session.newSystem(assignmentMatcher.group(1));

        }else if(whiteSpaceMatcher.find()){

            session.newSystem();

        } else{
            printAssignmentMatchError(cmd, out);
        }
    }

    private void parseNewGraphCommand(String cmd, PrintStream out) throws Exception{

        String id = null;
        assignmentMatcher.reset(cmd);
        whiteSpaceMatcher.reset(cmd);

        if(assignmentMatcher.find()){

            session.newGraph(assignmentMatcher.group(1));

        }else if(whiteSpaceMatcher.find()){

            session.newGraph();

        } else{
            printAssignmentMatchError(cmd, out);
        }
    }

    private void parseNewRuleCommand(String cmd, PrintStream out) throws Exception{

        String id = null;
        assignmentMatcher.reset(cmd);
        whiteSpaceMatcher.reset(cmd);

        if(assignmentMatcher.find()){

            session.newGraph(assignmentMatcher.group(1));

        }else if(whiteSpaceMatcher.find()){

            session.newGraph();

        } else{
            printAssignmentMatchError(cmd, out);
        }

    }

    private void parseNewCondCommand(String cmd, PrintStream out) throws Exception{

    }

    private void parseNewNodeCommand(String cmd, PrintStream out) throws Exception{

        String label = null;
        String data = null;

        nodeMatcher.reset(cmd);

        if(nodeMatcher.find()){

            label = nodeMatcher.group(1);

            // data is a number
            if(nodeMatcher.group(5) != null) data = nodeMatcher.group(5);
            // data is a string
            else if (nodeMatcher.group(4) != null) data = nodeMatcher.group(4);

            if(nodeMatcher.group(6) != null){
                assignmentMatcher.reset(nodeMatcher.group(6));
                if(assignmentMatcher.find()) {
                    session.newNode(assignmentMatcher.group(1), label, data);
                }else{
                    session.newNode(label, data);
                }
            }else{
                session.newNode(label, data);
            }
        }else{

            printNewNodeErrorMessage(cmd, out);

        }

    }

    private void parseNewEdgeCommand(String cmd, PrintStream out) throws Exception{

        String label = null;
        String src = null;
        String tar = null;

        edgeMatcher.reset(cmd);

        try {

            if (edgeMatcher.find()) {

                src = edgeMatcher.group(1);
                tar = edgeMatcher.group(2);
                label = edgeMatcher.group(3);

                if (edgeMatcher.group(4) != null) {
                    assignmentMatcher.reset(edgeMatcher.group(4));
                    if (assignmentMatcher.find()) {
                        session.newEdge(assignmentMatcher.group(1), src, tar, label);
                    } else {
                        session.newEdge(src, tar, label);
                    }
                }else{
                    session.newEdge(src, tar, label);
                }

            } else {
                printAddEdgeErrorMessage(cmd, out);
            }
        }catch(GraphException e){

            printInvalidEdgeErrorMessage(out);
        }

    }

    private void showEdges(PrintStream out) {
        out.println("\nCurrent edges:");
        if(session.edges.size() < 1) out.println("\tNone.");
        else {
            for (String k : session.edges.keySet()) {
                out.println("\t"+k);
            }
        }
        out.println();
    }

    private void showNodes(PrintStream out){
        out.println("\nCurrent nodes:");

        if(session.nodes.size() < 1) out.println("\tNone.");
        else {
            for (String k : session.nodes.keySet()) {
                out.println("\t"+k);
            }
        }
        out.println();
    }

    private void showConditions(PrintStream out){
        out.println("\nCurrent conds:");
        if(session.conditions.size() < 1) out.println("\tNone.");
        else {
            for (String k : session.conditions.keySet()) {
                out.println("\t"+k);
            }
        }
        out.println();
    }

    private void showRules(PrintStream out){
        out.println("\nCurrent rules:");
        if(session.rules.size() < 1) out.println("\tNone.");
        else {
            for (String k : session.rules.keySet()) {
                out.println("\t"+k);
            }
        }
        out.println();
    }
    private void showGraphs(PrintStream out){
        out.println("\nCurrent graphs:");
        if(session.graphs.size() < 1) out.println("\tNone.");
        else {
            for (String k : session.graphs.keySet()) {
                out.println("\t"+k);
            }
        }
        out.println();
    }

    private void showSystems(PrintStream out){
        out.println("\nCurrent systems:");
        if(session.systems.size() < 1) out.println("\tNone.");
        else {
            for (String k : session.systems.keySet()) {
                out.println("\t"+k);
            }
        }
        out.println();
    }

    private void printPrompt(PrintStream out) {
        out.print(">>> ");
    }

    private void printWelcome(PrintStream out) {
        out.println("Hello! Welcome to the jGT command line interface.\n");
    }

    private void printGoodbye(PrintStream out) {
        out.println("Exiting...Goodbye\n");
    }

    private void printTopLevelCommandErrorMessage(String cmd, PrintStream out) {
        out.println("\n"+Commands.NOT_REC+": '" + cmd + "'");
        out.println("\nTry: ");
        out.println("\t- "+Commands.VERB_NEW+"  <system|rule|node|edge>");
        out.println("\t- "+Commands.VERB_LOAD+" <system|rule|node|edge>");
        out.println("\t- "+Commands.VERB_SHOW+" <system|rule|node|edge>");
        out.println();
    }

    private void printNewCommandErrorMessage(String cmd, PrintStream out){
        out.println("\n"+Commands.NOT_REC+": '" + cmd + "'");
        out.println("\nTry: ");
        out.println("\t- " + Commands.VERB_NEW+" "+Commands.NOUN_OPTS);
        out.println();
    }

    private void printAssignmentMatchError(String cmd, PrintStream out){
        out.println("\n"+Commands.NOT_REC+": '" + cmd + "'");
        out.println("Try:");
        out.println("\tnew <command> -> <identifier>");
        out.println("For example:");
        out.println("\tnew node \"A\" 1 -> n1");
        out.println();
    }

    private void printShowErrorMessage(String cmd, PrintStream out){
        out.println("\n"+Commands.NOT_REC+": '" + cmd + "'");
        out.println("Try:");
        out.println("\tshow ("+Commands.NOUN_OPTS+")s");
        out.println("For example:");
        out.println("\tshow nodes");
        out.println();
    }

    private void printNewNodeErrorMessage(String cmd, PrintStream out){
        out.println("\n"+Commands.NOT_REC+": '" + cmd + "'");
        out.println("Try:");
        out.println("\tnew node \"<label>\" <int|\"string\"> -> <id>");
        out.println("For example:");
        out.println("\tnew node \"A\" 1 -> n0");
        out.println();
    }

    private void printAddEdgeErrorMessage(String cmd, PrintStream out){
        out.println("\n"+Commands.NOT_REC+": '" + cmd + "'");
        out.println("Try:");
        out.println("\tnew edge <src node> <tar node> \"<label>\" -> <id>");
        out.println("For example:");
        out.println("\tnew node \"A\" 1 -> n0");
        out.println("\tnew node \"A\" 2 -> n1");
        out.println("\tnew edge n0 n1 \"A\" -> e0");
        out.println();
    }

    private void printAddCommandErrorMessage(String cmd, PrintStream out){
        out.println("\n"+Commands.NOT_REC+": '" + cmd + "'");
        out.println("Try:");
        out.println("\tadd <id>* to <id>");
        out.println("For example:");
        out.println("\tnew node \"A\" 1 -> n0");
        out.println("\tnew graph -> g");
        out.println("\tadd n0 to g");
        out.println();
    }

    private void printInvalidEdgeErrorMessage(PrintStream out){
        out.println("\nCould not create new edge, both source and target nodes must exist.\n");
    }

}
