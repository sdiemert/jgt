package com.sdiemert.jgt.cli;

import com.sdiemert.jgt.cli.command.Command;
import com.sdiemert.jgt.cli.scope.ScopeException;
import com.sdiemert.jgt.core.GraphException;
import com.sdiemert.jgt.core.RuleException;

import java.io.Console;
import java.io.IOException;
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

    public void run(Console console, PrintStream out){
        this.printWelcome(out);
        while(true){
            this.printPrompt(out);
            if(!this.repl(console.readLine(), out)){
                break;
            }
        }
        printGoodbye(out);
    }

    public void run(InputStream in, PrintStream out){
        Scanner inputScanner = new Scanner(in);
        this.printWelcome(out);
        while(true){
            this.printPrompt(out);
            if(!this.repl(inputScanner.nextLine(), out)){
                break;
            }
        }
        printGoodbye(out);
    }

    private boolean repl(String s, PrintStream out){

        Command cmd;

        try {
            cmd = parser.command(s);

            cmd.setOutputStream(out);

            if(cmd.isExit()) return false;

            session.execute(cmd);

        }catch(ParserException e){
            printParserError(out, e.getMessage());
        }catch(ScopeException e){
            printScopeError(out, e.getMessage());
        }catch(GraphException e){
            printError(out, e.getMessage());
        }catch(RuleException e){
            printError(out, e.getMessage());
        }catch(IOException e){
            printError(out, e.getMessage());
        }

        return true;
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

    private void printError(PrintStream out, String err){
        out.println(err);
    }

}
