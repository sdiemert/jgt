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

    public void add(String sym, Rule r) throws ScopeException{

        for(Rule x : this.sys.getRules()){
            if(x.getId().equals(r.getId())){
                throw new ScopeException("A rule already exists in this system with identifier '"+sym+"'");
            }
        }

        this.sys.addRule(r);
    }

    public String show(String k) throws ScopeException{

        for(Rule r : this.getRules()){
            if(r.getId().equals(k)){
                return r.getId()+" : Rule";
            }
        }

        throw new ScopeException("Unknown identifier '"+k+"'");

    }

    public String show() throws ScopeException{
        StringBuilder sb = new StringBuilder();

        sb.append("Rules:");

        if(this.getRules().size() == 0){
            sb.append(" None.\n");
        }else{
            sb.append("\n");
            for(Rule r : this.getRules()){
                sb.append(r.getId());
                sb.append(" : Rule\n");
            }
        }

        return sb.toString();
    }

    public ArrayList<Rule> getRules(){
        return this.sys.getRules();
    }

    public Scope exit() throws ScopeException {
        this.parent.add(this.sym, this.sys);
        return this.parent;
    }

}
