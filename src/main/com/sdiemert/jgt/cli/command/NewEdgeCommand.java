package com.sdiemert.jgt.cli.command;

import com.sdiemert.jgt.cli.scope.GraphScope;
import com.sdiemert.jgt.cli.scope.Scope;
import com.sdiemert.jgt.cli.scope.ScopeException;
import com.sdiemert.jgt.core.GraphException;

public class NewEdgeCommand extends Command {

    private String adj;
    private String label;
    private String src;
    private String tar;
    private String sym;

    public NewEdgeCommand(String sym){
        this.sym = sym;
    }

    public Scope apply(GraphScope s) throws ScopeException, GraphException {

        if(this.adj != null){
            throw new ScopeException("Cannot create a '"+this.adj+"' edge in a graph (only allowed in rules)");
        }

        s.addEdge(this.sym, this.src, this.tar, this.label);
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

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getTar() {
        return tar;
    }

    public void setTar(String tar) {
        this.tar = tar;
    }
}
