package com.sdiemert.jgt.cli.command;

import com.sdiemert.jgt.cli.scope.*;
import com.sdiemert.jgt.core.Rule;

public class NewRuleCommand extends Command {

    String sym;

    public NewRuleCommand(String sym){
        this.sym = sym;
    }

    public String getSym() {
        return sym;
    }

    public Scope apply(SystemScope s) throws ScopeException{

        for(Rule x : s.getRules()){
            if(x.getId().equals(getSym())){
                throw new ScopeException("A rule already exists in this system with identifier '"+x.getId()+"'");
            }
        }

        return new RuleScope(s, getSym());
    }

}
