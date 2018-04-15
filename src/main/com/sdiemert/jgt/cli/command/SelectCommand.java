package com.sdiemert.jgt.cli.command;

import com.sdiemert.jgt.cli.scope.*;
import com.sdiemert.jgt.core.Rule;

public class SelectCommand extends Command {

    String id;

    public SelectCommand(String id){
        this.id = id;
    }

    public Scope apply(GlobalScope s) throws ScopeException{

        // may select a system or a graph.

        if(s.getGraphs().containsKey(id)){
            return (new GraphScope(s, s.getGraphs().get(id)));
        }else if(s.getSystems().containsKey(id)){
            return (new SystemScope(s, s.getSystems().get(id)));
        }else{
            throw new ScopeException("Cannot find '"+id+"' in the current scope.");
        }
    }

    public Scope apply(SystemScope s) throws ScopeException{

        for(Rule r : s.getRules()){
            if(r.getId().equals(id)){
                return new RuleScope(s, r);
            }
        }

        // not found, throw an error.
        throw new ScopeException("Cannot find '"+id+"' in the current scope.");

    }

}


