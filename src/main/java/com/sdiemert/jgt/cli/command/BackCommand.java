package com.sdiemert.jgt.cli.command;

import com.sdiemert.jgt.cli.scope.*;
import com.sdiemert.jgt.core.GraphException;

public class BackCommand extends Command {

    public Scope apply(GraphScope s) throws ScopeException{
        return s.exit();
    }

    public Scope apply(RuleScope s) throws ScopeException, GraphException {
        return s.exit();
    }

    public Scope apply(SystemScope s) throws ScopeException {
        return s.exit();
    }
}
