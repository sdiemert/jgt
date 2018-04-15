package com.sdiemert.jgt.cli.scope;

import com.sdiemert.jgt.core.GTSystem;
import com.sdiemert.jgt.core.Graph;
import com.sdiemert.jgt.core.Rule;

public abstract class Scope {

    public abstract String scopeAsString();

    public abstract String show();

    public Scope exit() throws ScopeException{
        return this;
    }

    public void add(String sym, Graph g) throws ScopeException{
        throw new ScopeException("Cannot add Graph to this scope.");
    }

    public void add(String sym, GTSystem gt) throws ScopeException{
        throw new ScopeException("Cannot add GTSystem to this scope.");
    }

    public void add(String sym, Rule r) throws ScopeException{
        throw new ScopeException("Cannot add Rule to this scope.");
    }

}
