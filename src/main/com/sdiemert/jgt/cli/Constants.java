package com.sdiemert.jgt.cli;

public class Constants {

    public final static String VERB_NEW = "new";
    public final static String VERB_LOAD = "load";
    public final static String VERB_SHOW = "show";
    public final static String VERB_EXIT = "exit";
    public final static String VERB_DEL = "delete";
    public final static String VERB_ADD = "add";

    public final static String NOUN_SYS = "system";
    public final static String NOUN_GRAPH = "graph";
    public final static String NOUN_RULE = "rule";
    public final static String NOUN_NODE = "node";
    public final static String NOUN_EDGE = "edge";
    public final static String NOUN_COND = "cond";

    public final static String ADJ_ADD = "add";
    public final static String ADJ_DEL = "del";

    public final static String NOUN_OPTS = NOUN_SYS+"|"+NOUN_GRAPH+"|"+NOUN_RULE+"|"+NOUN_COND+"|"+NOUN_NODE+"|"+NOUN_EDGE;
    public final static String VERB_OPTS = VERB_NEW+"|"+VERB_LOAD+"|"+VERB_SHOW+"|"+VERB_DEL;

    public final static String ID = "[A-Za-z][A-Za-z0-9]*";

    public final static String NOT_REC = "Opps...Sorry, I don't recognize";
}
