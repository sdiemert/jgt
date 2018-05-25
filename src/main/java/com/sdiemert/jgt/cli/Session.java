package com.sdiemert.jgt.cli;

import com.sdiemert.jgt.cli.command.Command;
import com.sdiemert.jgt.cli.scope.*;
import com.sdiemert.jgt.core.GraphException;
import com.sdiemert.jgt.core.RuleException;

import java.io.IOException;

public class Session {

    private Scope currScope = null;

    public Session(){
        currScope = new GlobalScope();
    }

    public void execute(Command c) throws ScopeException, GraphException, RuleException, IOException {
        currScope = c.apply(currScope);
    }

    public String scopeAsString(){
        return currScope.scopeAsString();
    }

    public Scope getScope(){
        return this.currScope;
    }

}
