package com.sdiemert.jgt.cli.scope;

import com.sdiemert.jgt.core.GTSystem;
import com.sdiemert.jgt.core.Rule;

import java.util.ArrayList;

public class SystemScope extends Scope {

    String sym;
    Scope parent;

    GTSystem sys;

    public SystemScope(Scope parent, String sym){
        this.sym = sym;
        this.parent = parent;
        this.sys = new GTSystem(sym);
    }

    public SystemScope(Scope parent, GTSystem sys){
        this.sym = sys.getId();
        this.parent = parent;
        this.sys = sys;
    }

    public String scopeAsString(){
        return sym;
    }

    public String show(){
        return "";
    }

    public ArrayList<Rule> getRules(){
        return this.sys.getRules();
    }

}
