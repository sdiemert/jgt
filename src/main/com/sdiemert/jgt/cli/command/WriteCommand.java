package com.sdiemert.jgt.cli.command;

import com.sdiemert.jgt.cli.scope.GlobalScope;
import com.sdiemert.jgt.cli.scope.Scope;
import com.sdiemert.jgt.cli.scope.ScopeException;
import com.sdiemert.jgt.fs.JSONFileWriter;

import java.io.IOException;

public class WriteCommand extends Command {

    String path;
    String id;

    public WriteCommand(String path, String id) {
        this.path = path;
        this.id = id;
    }

    public Scope apply(GlobalScope s) throws ScopeException, IOException {

        JSONFileWriter fw = new JSONFileWriter();

        if (s.getSystems().containsKey(id)) {
            fw.writeSystem(s.getSystems().get(id), path);
        } else if (s.getGraphs().containsKey(id)) {
            fw.writeGraph(s.getGraphs().get(id), path);
        } else {
            throw new ScopeException("'" + id + "' does not exist or is invalid.");
        }

        return s;
    }


}
