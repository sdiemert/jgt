package com.sdiemert.jgt.cli.command;

import com.sdiemert.jgt.cli.scope.*;

public class NewGraphCommand extends Command {

    String sym;

    public NewGraphCommand(String sym){
        this.sym = sym;
    }

    public String getSym() {
        return sym;
    }

    public boolean isExit(){
        return false;
    }

    public Scope apply(GlobalScope s) throws ScopeException{
        return new GraphScope(s, getSym());
    }

}
