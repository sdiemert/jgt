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

    public String show(String k) throws ScopeException{

        String s = k+" : ";

        if(graphs.containsKey(k)){
            s += "graph";
        }else if(systems.containsKey(k)){
            s += "system";
        }else{
            throw new ScopeException("Unknown identifier '"+k+"'");
        }
        return s;
    }

    public String show() throws ScopeException{

        StringBuilder sb = new StringBuilder();

        sb.append("Graphs: ");

        if(this.graphs.size() > 0){
            sb.append("\n");
            for(String k : graphs.keySet()){
                sb.append("\t");
                sb.append(this.show(k));
                sb.append("\n");
            }

        }else{
            sb.append("None\n");
        }

        sb.append("Systems: ");

        if(this.systems.size() > 0){
            sb.append("\n");
            for(String k : systems.keySet()){
                sb.append("\t");
                sb.append(this.show(k));
                sb.append("\n");
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

    public void delete(String k) throws ScopeException{
        if(graphs.containsKey(k)){
            graphs.remove(k);
        }else if(systems.containsKey(k)){
            systems.remove(k);
        }else{
            throw new ScopeException("Cannot find element identified by '"+k+"' to delete.");
        }
    }

}
