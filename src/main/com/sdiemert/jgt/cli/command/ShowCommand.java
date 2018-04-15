package com.sdiemert.jgt.cli.command;

import com.sdiemert.jgt.cli.scope.GlobalScope;
import com.sdiemert.jgt.cli.scope.GraphScope;
import com.sdiemert.jgt.cli.scope.Scope;
import com.sdiemert.jgt.cli.scope.ScopeException;

import java.io.PrintStream;

public class ShowCommand extends Command {

    public ShowCommand(PrintStream out){
        super(out);
    }

    public ShowCommand(){
        super();
    }

    public Scope apply(GraphScope s) throws ScopeException{
        this.outputStream.println(s.show());
        return s;
    }

    public Scope apply(GlobalScope s) throws ScopeException{
        this.outputStream.println(s.show());
        return s;
    }

}
