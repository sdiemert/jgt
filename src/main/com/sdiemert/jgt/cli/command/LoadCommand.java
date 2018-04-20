package com.sdiemert.jgt.cli.command;

import com.sdiemert.jgt.cli.scope.GlobalScope;
import com.sdiemert.jgt.cli.scope.Scope;
import com.sdiemert.jgt.cli.scope.ScopeException;
import com.sdiemert.jgt.core.GTSystem;
import com.sdiemert.jgt.core.Graph;
import com.sdiemert.jgt.core.GraphException;
import com.sdiemert.jgt.core.RuleException;
import com.sdiemert.jgt.fs.JSONFileParser;

import java.io.IOException;

public class LoadCommand extends Command {

    String path;
    String type;

    public LoadCommand(String path, String type){
        this.path = path;
        this.type = type;
    }

    public Scope apply(GlobalScope s) throws ScopeException, GraphException, IOException, RuleException {

        JSONFileParser fp = new JSONFileParser();

        if(type.equals("graph")){
            Graph g = fp.loadGraph(this.path);
            s.add(g.getId(), g);
        }else if(type.equals("system")){
            GTSystem sys = fp.loadSystem(this.path);
            s.add(sys.getId(), sys);
        }else{
            throw new ScopeException("Unknown type for load command.");
        }

        return s;
    }




}
