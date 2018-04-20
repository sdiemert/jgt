package com.sdiemert.jgt.cli;

import com.sdiemert.jgt.cli.command.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    // g : new graph (done)
    // n : new node "A" 1? (done)
    // n : new add node "A" 1? (done)
    // n : new del node "A" 1? (done)
    // e : new edge n1 n2 "A" (done)
    // e : new add edge n1 n2 "A" (done)
    // e : new del edge n1 n2 "A" (done)
    // s : new system (done)
    // r : new rule (done)
    // c : new cond
    //
    // rm x - removes x from the current scope (done)
    //
    // show  - shows the items currently in the scope (done)
    // show x - shows x from the current scope. (done)
    // back  - backs up a scope level (done)
    // pick x - enters a scope identified by x, must be graph, rule, or system. (done)
    //
    // apply <system id> <graph id> - works in global scope, picks a rule in the system to apply, displays resulting graph. (done)
    // apply <system id>.<rule id> <graph id> - applies specified rule to graph, displays resulting graph. (done)
    //
    // load <path> - loads a file at the designated path, loads all elements in the file.
    // write x <path> - writes item identified by x to path

    private Matcher assignmentMatcher = Pattern.compile(
            "\\s*("+Constants.ID+")\\s*:\\s*(.*)"
        ).matcher("");

    private Matcher verbMatcher = Pattern.compile(
            "("+Constants.VERB_NEW+
                    "|"+Constants.VERB_SHOW+
                    "|"+Constants.VERB_BACK+
                    "|"+Constants.VERB_PICK+
                    "|"+Constants.VERB_DEL+
                    "|"+Constants.VERB_APPLY+
                    ")(\\s+(.*))?"
        ).matcher("");

    private Matcher newMatcher = Pattern.compile(
                    "(("+Constants.ADJ_ADD+"|"+Constants.ADJ_DEL+")\\s+)?" +
                    "("+Constants.NOUN_GRAPH+"|"+Constants.NOUN_NODE+"|"+
                            Constants.NOUN_EDGE+"|"+Constants.NOUN_SYS+"|"+Constants.NOUN_RULE+")"+
                    "(\\s+(.*))?"
        ).matcher("");

    private Matcher newNodeArgMatcher = Pattern.compile(
            "(\"[A-Za-z0-9]+\")(\\s+(\"[A-Za-z0-9]+\"|[0-9]+))?"
        ).matcher("");

    private  Matcher newEdgeArgMatcher = Pattern.compile(
            "("+Constants.ID+")\\s+("+Constants.ID+")\\s+(\"[A-Za-z0-9]+\")"
        ).matcher("");

    private Matcher applyMatcher = Pattern.compile(
            "((("+Constants.ID+")\\s+("+Constants.ID+"))|(("+Constants.ID+")\\.("+Constants.ID+")\\s+("+Constants.ID+")))"
        ).matcher("");

    public Command command(String in) throws ParserException{

        String sym = null;
        String rest = null;
        String verb = null;

        // 1) identify a symbol to assign (possibly none).
        assignmentMatcher.reset(in);

        if(assignmentMatcher.find()){
            sym = assignmentMatcher.group(1);
            rest = assignmentMatcher.group(2);
        }else{
            sym = null;
            rest = in;
        }

        // 2) identify a verb.
        verbMatcher.reset(rest);

        if(verbMatcher.find()){
            verb = verbMatcher.group(1);
            rest = verbMatcher.group(3);
        }else{
            throw new ParserException("Failed to parse: "+in);
        }


        // 3) based on the verb, take different actions.

        if(verb.equals(Constants.VERB_NEW)) {
            return parseNewCommand(sym, rest);
        }else if(verb.equals(Constants.VERB_SHOW)) {
            return new ShowCommand(rest);
        }else if(verb.equals(Constants.VERB_BACK)){
            return new BackCommand();
        }else if(verb.equals(Constants.VERB_PICK)){
            return new SelectCommand(rest);
        }else if(verb.equals(Constants.VERB_DEL)){
            if(rest.matches(Constants.ID)) {
                return new DeleteCommand(rest);
            }else{
                throw new ParserException("Expected identifier after "+Constants.VERB_DEL);
            }
        }else if(verb.equals(Constants.VERB_APPLY)){
            return parseAppyCommand(rest);
        }else{
            throw new ParserException("Failed to parse: "+in);
        }

    }

    Command parseAppyCommand(String in) throws ParserException{

        String sys, rule, graph;

        applyMatcher.reset(in);

        if(applyMatcher.find()) {


            // system type match.
            if (applyMatcher.group(2) != null) {

                sys = applyMatcher.group(3);
                rule = null;
                graph = applyMatcher.group(4);

            } else if (applyMatcher.group(5) != null) {

                sys = applyMatcher.group(6);
                rule = applyMatcher.group(7);
                graph = applyMatcher.group(8);

            } else {

                throw new ParserException("Invalid apply command '" + in + "'");
            }

        }else{
            throw new ParserException("Invalid apply command '"+in+"'");
        }

        return new ApplyCommand(sys, rule, graph);

    }

    Command parseNewCommand(String sym, String in) throws ParserException{

        // in has everything after the "new" keyword,
        // new graph
        // new add node
        // new del node
        // new node

        if(sym == null){
            throw new ParserException("new command must have assignment");
        }

        String adj = null, noun = null, args = null;

        newMatcher.reset(in);

        if(newMatcher.find()){

            adj = newMatcher.group(2);
            noun = newMatcher.group(3);
            args = newMatcher.group(5);

            if(noun.equals(Constants.NOUN_NODE)){

                NewNodeCommand cmd = new NewNodeCommand(sym);
                cmd.setAdj(adj);
                parseNewNodeArguments(args, cmd);
                return cmd;

            }else if(noun.equals(Constants.NOUN_GRAPH)){

                return (new NewGraphCommand(sym));

            }else if(noun.equals(Constants.NOUN_RULE)){

                return (new NewRuleCommand(sym));

            }else if(noun.equals(Constants.NOUN_SYS)){

                return (new NewSystemCommand(sym));


            }else if(noun.equals(Constants.NOUN_EDGE)){

                NewEdgeCommand cmd = new NewEdgeCommand(sym);
                cmd.setAdj(adj);
                parseNewEdgeArguments(args, cmd);
                return cmd;

            }else{
                throw new ParserException("Failed to parse: "+in);
            }

        }else{
            throw new ParserException("Failed to parse: "+in);
        }
    }

    void parseNewNodeArguments(String in, NewNodeCommand cmd) throws ParserException{
        newNodeArgMatcher.reset(in);
        if(newNodeArgMatcher.find()){
            cmd.setLabel(newNodeArgMatcher.group(1));
            cmd.setData(newNodeArgMatcher.group(3));
        }else{
            throw new ParserException("Invalid arguments to new node: '"+in+"'");
        }
    }

    void parseNewEdgeArguments(String in, NewEdgeCommand cmd) throws ParserException{
        newEdgeArgMatcher.reset(in);
        if(newEdgeArgMatcher.find()){
            cmd.setSrc(newEdgeArgMatcher.group(1));
            cmd.setTar(newEdgeArgMatcher.group(2));
            cmd.setLabel(newEdgeArgMatcher.group(3));
        }else{
            throw new ParserException("Invalid arguments to new edge: '"+in+"'");
        }
    }

}
