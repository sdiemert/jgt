package com.sdiemert.jgt.cli.command;

import com.sdiemert.jgt.cli.scope.GraphScope;
import com.sdiemert.jgt.cli.scope.RuleScope;
import com.sdiemert.jgt.cli.scope.Scope;
import com.sdiemert.jgt.cli.scope.ScopeException;

public class NewNodeCommand extends Command {

    private String adj;
    private String label;
    private String data;
    private String sym;

    public NewNodeCommand(String sym){
        this.sym = sym;
    }

    public Scope apply(GraphScope s) throws ScopeException{

        if(this.adj != null){
            throw new ScopeException("Cannot create a '"+this.adj+"' node in a graph (only allowed in rules)");
        }

        for(String k : s.getScopeNodes().keySet()){
            if(k.equals(sym)) throw new ScopeException("A already exists with identifier '"+sym+"'");
        }

        s.addNode(this.sym, this.label, this.data);
        return s;
    }

    public Scope apply(RuleScope s) throws ScopeException {
        s.addNode(this.sym, this.label, this.data, this.adj);
        return s;
    }


    public String getAdj() {
        return adj;
    }

    public void setAdj(String adj) {
        this.adj = adj;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
