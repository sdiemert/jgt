package com.sdiemert.jgt.cli;

import com.sdiemert.jgt.cli.command.Command;
import com.sdiemert.jgt.cli.scope.*;
import com.sdiemert.jgt.core.GraphException;

public class Session {

    private Scope currScope = null;

    public Session(){
        currScope = new GlobalScope();
    }

    public void execute(Command c) throws ScopeException, GraphException {
        currScope = c.apply(currScope);
    }

    public String scopeAsString(){
        return currScope.scopeAsString();
    }

}
