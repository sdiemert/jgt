package com.sdiemert.jgt.cli.scope;

import com.sdiemert.jgt.core.GTSystem;

public class SystemScope extends Scope {

    String sym;
    Scope parent;

    GTSystem sys;

    public SystemScope(Scope parent, String sym){
        this.sym = sym;
        this.parent = parent;
        this.sys = new GTSystem();
    }

    public String scopeAsString(){
        return sym;
    }

    public String show(){
        return "";
    }

}
