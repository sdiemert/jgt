
package com.sdiemert.jgt;

import com.sdiemert.jgt.cli.CommandLineInterface;

import java.io.Console;

public class Main {

    public static void main(String args[]){

        CommandLineInterface cli = new CommandLineInterface();

        Console console = System.console();

        if(console != null) {
            cli.run(console, System.out);
        }else{
            cli.run(System.in, System.out);
        }

    }

}