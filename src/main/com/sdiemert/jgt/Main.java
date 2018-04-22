
package com.sdiemert.jgt;

import com.sdiemert.jgt.cli.CommandLineInterface;
import com.sdiemert.jgt.cli.Session;
import com.sdiemert.jgt.ui.Controller;
import com.sdiemert.jgt.ui.MainFrame;
import com.sdiemert.jgt.util.PrintStreamWrapper;

import java.io.Console;

public class Main {

    public static void cli(){
        CommandLineInterface cli = new CommandLineInterface();
        Console console = System.console();
        if(console != null) {
            cli.run(console, new PrintStreamWrapper(System.out));
        }else{
            cli.run(System.in, new PrintStreamWrapper(System.out));
        }
    }

    public static void ui(){
        Controller controller = new Controller();
        controller.run();
    }

    public static void main(String args[]){

        //cli();
        ui();

    }

}