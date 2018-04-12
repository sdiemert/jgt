package com.sdiemert.jgt.cli;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandLineInterface {

    private Matcher rootMatcher = Pattern.compile(
            "\\s*("
            +Commands.VERB_NEW+"|"+Commands.VERB_LOAD+"|"+Commands.VERB_SHOW
            +")\\s+(.*)").matcher("");

    private Matcher nounMatcher = Pattern.compile(
            "(" +Commands.NOUN_SYS+"|"+Commands.NOUN_RULE+"|"
                    +Commands.NOUN_COND+"|"+Commands.NOUN_GRAPH+"|"
                    +Commands.NOUN_NODE+"|"+ Commands.NOUN_EDGE
                    +")\\s+(.*)").matcher("");

    public CommandLineInterface(){


    }

    public void run(InputStream in, PrintStream out){

        Scanner inputScanner = new Scanner(in);

        boolean exit = false;
        String cmd = null;

        this.printWelcome(out);
        this.printPrompt(out);

        while(!exit){

            cmd = inputScanner.nextLine();

            this.parseCommand(cmd, out);

            this.printPrompt(out);

        }
    }

    private void parseCommand(String cmd, PrintStream out) {

        String cmdRoot = null;

        rootMatcher.reset(cmd);
        if(rootMatcher.find()){
            cmdRoot = rootMatcher.group(1);

            if(cmdRoot.equals(Commands.VERB_NEW)) parseNewCommand(rootMatcher.group(2), out);
            else if(cmdRoot.equals(Commands.VERB_LOAD)) parseLoadCommand(rootMatcher.group(2), out);
            else if(cmdRoot.equals(Commands.VERB_SHOW)) parseShowCommand(rootMatcher.group(2), out);
            else printTopLevelCommandErrorMessage(cmd, out);
        }else{
            printTopLevelCommandErrorMessage(cmd, out);
        }


    }

    private void printTopLevelCommandErrorMessage(String cmd, PrintStream out) {
        out.println("\nOpps...I don't recognize: '" + cmd + "'");
        out.println("\nTry: ");
        out.println("- "+Commands.VERB_NEW+" <system|rule|node|edge>");
        out.println("- "+Commands.VERB_LOAD+" <system|rule|node|edge>");
        out.println("- "+Commands.VERB_SHOW+" <system|rule|node|edge>");
        out.println();
    }

    private void parseShowCommand(String cmd, PrintStream out) {
        out.println("show cmd");
    }

    private void parseLoadCommand(String cmd, PrintStream out) {
        out.println("load cmd");
    }

    private void parseNewCommand(String cmd, PrintStream out) {

        String noun = null;

        nounMatcher.reset(cmd);
        if(nounMatcher.find()){
            noun = nounMatcher.group(1);
            if(noun.equals(Commands.NOUN_SYS)){

            }
        }else{
            //printNewCommandErrorMessage(cmd, out);
        }


    }

    private void printPrompt(PrintStream out) {
        out.print(">>>");
    }

    private void printWelcome(PrintStream out) {
        out.println("Hello! Welcome to the jGT command line interface.%n");
    }

}
