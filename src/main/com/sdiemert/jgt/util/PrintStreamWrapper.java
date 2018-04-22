package com.sdiemert.jgt.util;

import java.io.PrintStream;

public class PrintStreamWrapper implements Printer {

    PrintStream ps;

    public PrintStreamWrapper(PrintStream p){
        this.ps = p;
    }

    public void println(String s){
        ps.println(s);
    }

    public void print(String s){
        ps.print(s);
    }
}
