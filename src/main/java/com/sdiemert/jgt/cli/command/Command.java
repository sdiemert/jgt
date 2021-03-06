package com.sdiemert.jgt.cli.command;

import com.sdiemert.jgt.cli.scope.*;
import com.sdiemert.jgt.core.Graph;
import com.sdiemert.jgt.core.GraphException;
import com.sdiemert.jgt.core.RuleException;
import com.sdiemert.jgt.util.Printer;

import java.io.IOException;
import java.io.PrintStream;

public abstract class Command {

    Printer outputStream;

    public Command(){
        this.outputStream = null;
    }

    public Command(Printer out){
        this.outputStream = out;
    }

    public boolean isExit(){
        return false;
    }

    public Scope apply(Scope s) throws ScopeException, GraphException, RuleException, IOException {

        if(s instanceof GraphScope) return apply((GraphScope) s);
        else if(s instanceof GlobalScope) return apply((GlobalScope) s);
        else if(s instanceof RuleScope) return apply((RuleScope) s);
        else if(s instanceof SystemScope) return apply((SystemScope) s);
        else{
            throw new ScopeException("Unknown scope error");
        }

    }

    public Scope apply(GraphScope s) throws ScopeException, GraphException {
        throw new ScopeException("Cannot apply this command to current scope.");
    }

    public Scope apply(GlobalScope s) throws ScopeException, GraphException, IOException, RuleException {
        throw new ScopeException("Cannot apply this command to current scope.");
    }

    public Scope apply(RuleScope s) throws ScopeException, GraphException{
        throw new ScopeException("Cannot apply this command to current scope.");
    }

    public Scope apply(SystemScope s) throws ScopeException{
        throw new ScopeException("Cannot apply this command to current scope.");
    }


    public void setOutputStream(Printer out){
        this.outputStream = out;
    }
}
