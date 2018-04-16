package com.sdiemert.jgt.cli.command;

import com.sdiemert.jgt.cli.scope.*;

import java.io.PrintStream;

public class ShowCommand extends Command {

    String sym;

    public ShowCommand(PrintStream out){
        super(out);
    }

    public ShowCommand(){
        super();
    }

    public ShowCommand(String sym){
        super();
        this.sym = sym;
    }

    public ShowCommand(PrintStream out, String sym){
        super(out);
        this.sym = sym;
    }

    public Scope apply(GraphScope s) throws ScopeException{

        if(sym != null){
            this.outputStream.println(s.show(sym));
        }else{
            this.outputStream.println(s.show());
        }


        return s;
    }

    public Scope apply(GlobalScope s) throws ScopeException{

        if(sym != null){
            this.outputStream.println(s.show(sym));
        }else{
            this.outputStream.println(s.show());
        }

        return s;
    }

    public Scope apply(SystemScope s) throws ScopeException{

        if(sym != null){
            this.outputStream.println(s.show(sym));
        }else {
            this.outputStream.println(s.show());
        }
        return s;
    }

    public Scope apply(RuleScope s) throws ScopeException{

        if(sym != null){
            this.outputStream.println(s.show(sym));
        }else{
            this.outputStream.println(s.show());
        }

        return s;
    }

}
