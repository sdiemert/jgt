package com.sdiemert.jgt.cli.command;

import com.sdiemert.jgt.cli.scope.*;

import java.io.PrintStream;

public abstract class Command {

    PrintStream outputStream;

    public Command(){
        this.outputStream = null;
    }

    public Command(PrintStream out){
        this.outputStream = out;
    }

    public boolean isExit(){
        return false;
    }

    public Scope apply(Scope s) throws ScopeException{

        if(s instanceof GraphScope) return apply((GraphScope) s);
        else if(s instanceof GlobalScope) return apply((GlobalScope) s);
        else if(s instanceof RuleScope) return apply((RuleScope) s);
        else if(s instanceof SystemScope) return apply((SystemScope) s);
        else{
            throw new ScopeException("Unknown scope error");
        }

    }

    public Scope apply(GraphScope s) throws ScopeException {
        throw new ScopeException("Cannot apply this command to current scope.");
    }

    public Scope apply(GlobalScope s) throws ScopeException{
        throw new ScopeException("Cannot apply this command to current scope.");
    }

    public Scope apply(RuleScope s) throws ScopeException{
        throw new ScopeException("Cannot apply this command to current scope.");
    }

    public Scope apply(SystemScope s) throws ScopeException{
        throw new ScopeException("Cannot apply this command to current scope.");
    }


    public void setOutputStream(PrintStream out){
        this.outputStream = out;
    }
}
