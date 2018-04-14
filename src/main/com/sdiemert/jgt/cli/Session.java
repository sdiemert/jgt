package com.sdiemert.jgt.cli;

import com.sdiemert.jgt.cli.command.Command;
import com.sdiemert.jgt.cli.scope.*;

import java.io.PrintStream;

public class Session {

    private Scope currScope = null;

    public Session(){
        currScope = new GlobalScope();
    }

    public void execute(Command c) throws ScopeException{
        currScope = c.apply(currScope);
    }

    public String scopeAsString(){
        return currScope.scopeAsString();
    }

}
