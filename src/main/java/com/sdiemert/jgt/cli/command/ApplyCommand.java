package com.sdiemert.jgt.cli.command;

import com.sdiemert.jgt.cli.scope.*;
import com.sdiemert.jgt.core.GTSystem;
import com.sdiemert.jgt.core.Graph;
import com.sdiemert.jgt.core.GraphException;
import com.sdiemert.jgt.core.Rule;

public class ApplyCommand extends Command {

    String system, rule, graph;

    public ApplyCommand(String s, String r, String g){
        this.system = s;
        this.rule = r;
        this.graph = g;
    }

    public Scope apply(GlobalScope s) throws ScopeException, GraphException{

        Rule r;
        GTSystem sys;
        Graph g;

        if(!s.getSystems().containsKey(system)){
            throw new ScopeException("System '"+system+"' does not exist.");
        }else{
            sys = s.getSystems().get(system);
        }

        if(!s.getGraphs().containsKey(graph)){
            throw new ScopeException("Graph '"+graph+"' does not exist.");
        }else{
            g = s.getGraphs().get(graph);
        }

        if(this.rule == null){
            sys.apply(g);

        }else{

            if((r = s.getSystems().get(system).getRule(rule)) == null){
                throw new ScopeException("Graph '"+graph+"' does not exist.");
            }

            r.apply(g);

        }

        GraphScope gs = new GraphScope(s, g);
        outputStream.println(gs.show());

        return s;
    }

}
