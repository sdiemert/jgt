package com.sdiemert.jgt.cli.command;

import com.sdiemert.jgt.cli.scope.GraphScope;
import com.sdiemert.jgt.cli.scope.Scope;
import com.sdiemert.jgt.cli.scope.ScopeException;

public class BackCommand extends Command {
    public Scope apply(GraphScope s) throws ScopeException{
        return s.exit();
    }
}
