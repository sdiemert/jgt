package com.sdiemert.jgt.cli;

import com.sdiemert.jgt.cli.command.Command;
import com.sdiemert.jgt.cli.scope.ScopeException;
import com.sdiemert.jgt.core.GraphException;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class CommandLineInterface {

    private Session session;
    private Parser parser;

    public CommandLineInterface(){

        session = new Session();
        parser = new Parser();

    }

    public void run(InputStream in, PrintStream out){

        Scanner inputScanner = new Scanner(in);

        Command cmd = null;

        this.printWelcome(out);

        while(true){
            this.printPrompt(out);

            try {
                cmd = parser.command(inputScanner.nextLine());

                cmd.setOutputStream(out);

                if(cmd.isExit()) break;

                session.execute(cmd);

            }catch(ParserException e){
                printParserError(out, e.getMessage());
            }catch(ScopeException e){
                printScopeError(out, e.getMessage());
            }

        }

        printGoodbye(out);
    }

    private void printPrompt(PrintStream out) {
        out.print(session.scopeAsString()+" >>> ");
    }

    private void printWelcome(PrintStream out) {
        out.println("Hello! Welcome to the jGT command line interface.\n");
    }

    private void printGoodbye(PrintStream out) {
        out.println("Exiting...Goodbye\n");
    }

    private void printParserError(PrintStream out, String err){
        out.println(err);
    }

    private void printScopeError(PrintStream out, String err){
        out.println(err);
    }

}
