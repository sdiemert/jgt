package com.sdiemert.jgt.cli.command;

import com.sdiemert.jgt.cli.scope.*;
import com.sdiemert.jgt.core.GTSystem;

public class NewSystemCommand extends Command {

    String sym;

    public NewSystemCommand(String sym){
        this.sym = sym;
    }

    public String getSym() {
        return sym;
    }

    public Scope apply(GlobalScope s) throws ScopeException{
        for(String sys : s.getSystems().keySet()){
            if(sys.equals(sym)) throw new ScopeException("A system already exists with identifier '"+sym+"'");
        }
        return new SystemScope(s, getSym());
    }

}
