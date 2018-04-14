package com.sdiemert.jgt.cli.command;

import com.sdiemert.jgt.cli.scope.GraphScope;
import com.sdiemert.jgt.cli.scope.Scope;

public class NewNodeCommand extends Command {

    private String adj;
    private String label;
    private String data;
    private String sym;

    public NewNodeCommand(String sym){
        this.sym = sym;
    }

    public Scope apply(GraphScope s){
        s.addNode(this.sym, this.label, this.data);
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
