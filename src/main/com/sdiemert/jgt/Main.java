
package com.sdiemert.jgt;

import com.sdiemert.jgt.cli.CommandLineInterface;

public class Main {

    public static void main(String args[]){

        CommandLineInterface cli = new CommandLineInterface();

        cli.run(System.in, System.out);

    }

}