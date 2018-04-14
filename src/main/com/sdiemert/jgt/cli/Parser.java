package com.sdiemert.jgt.cli;

import com.sdiemert.jgt.cli.command.Command;
import com.sdiemert.jgt.cli.command.NewGraphCommand;
import com.sdiemert.jgt.cli.command.NewNodeCommand;
import com.sdiemert.jgt.cli.command.ShowCommand;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    // g : new graph
    // n : new node "A" 1?
    // n : new add node "A" 1?
    // n : new del node "A" 1?
    // e : new edge n1 n2 "A"
    // e : new add edge n1 n2 "A"
    // e : new del edge n1 n2 "A"
    // s : new system
    // r : new rule
    // c : new cond
    //
    // rm x - removes x from the current scope
    //
    // show  - shows the items currently in the scope
    // back  - backs up a scope level
    // pick x - enters a scope identified by x, must be graph, rule, or system.
    //
    // x : load <path> - loads a file at the designated path, saves it locally with id x.
    // write x <path> - writes item identified by x to path

    Matcher assignmentMatcher = Pattern.compile("\\s*("+Constants.ID+")\\s*:\\s*(.*)").matcher("");

    Matcher verbMatcher = Pattern.compile(
            "("+Constants.VERB_NEW+"|"+Constants.VERB_SHOW+")(\\s+(.*))?"
    ).matcher("");

    Matcher newMatcher = Pattern.compile(
                    "(("+Constants.ADJ_ADD+"|"+Constants.ADJ_DEL+")\\s+)?" +
                    "("+Constants.NOUN_GRAPH+"|"+Constants.NOUN_NODE+")"+
                    "(\\s+(.*))?"
    ).matcher("");

    Matcher newNodeArgMatcher = Pattern.compile("(\"[A-Za-z0-9]+\")(\\s+(\"[A-Za-z0-9]+\"|[0-9]+))?").matcher("");

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


        // 3) offload details of command parsing to other methods.

        if(verb.equals(Constants.VERB_NEW)) {
            return parseNewCommand(sym, rest);
        }else if(verb.equals(Constants.VERB_SHOW)){
            return new ShowCommand();
        }else{
            throw new ParserException("Failed to parse: "+in);
        }

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

}
