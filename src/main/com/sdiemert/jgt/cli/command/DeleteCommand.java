package com.sdiemert.jgt.cli.command;

import com.sdiemert.jgt.cli.scope.*;
import com.sdiemert.jgt.core.Rule;

public class DeleteCommand extends Command {

    String id;

    public DeleteCommand(String id){
        this.id = id;
    }

    public Scope apply(Scope s) throws ScopeException{
        s.delete(id);
        return s;
    }

}


