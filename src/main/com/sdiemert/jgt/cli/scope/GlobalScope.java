package com.sdiemert.jgt.cli.scope;

import com.sdiemert.jgt.core.GTSystem;
import com.sdiemert.jgt.core.Graph;

import java.util.HashMap;

public class GlobalScope extends Scope {

    HashMap<String, Graph> graphs;
    HashMap<String, GTSystem> systems;

    public GlobalScope(){
        this.graphs = new HashMap<String, Graph>();
        this.systems = new HashMap<String, GTSystem>();
    }

    public String scopeAsString(){
        return "";
    }

    public String show(){

        StringBuilder sb = new StringBuilder();

        sb.append("Graphs: ");

        if(this.graphs.size() > 0){
            sb.append("\n");
            for(String k : graphs.keySet()){
                sb.append("\t");
                sb.append(k);
                sb.append(" : Graph\n");
            }

        }else{
            sb.append("None\n");
        }

        sb.append("Systems: ");

        if(this.systems.size() > 0){
            sb.append("\n");
            for(String k : systems.keySet()){
                sb.append("\t");
                sb.append(k);
                sb.append(" : System\n");
            }
        }else{
            sb.append("None\n");
        }

        return sb.toString();

    }

    public void add(String sym, Graph g){
        this.graphs.put(sym, g);
    }

    public void add(String sym, GTSystem gt){
        this.systems.put(sym, gt);
    }

    public HashMap<String, Graph> getGraphs(){
        return this.graphs;
    }

    public HashMap<String, GTSystem> getSystems(){
        return this.systems;
    }

}
