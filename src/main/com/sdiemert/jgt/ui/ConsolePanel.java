package com.sdiemert.jgt.ui;

import com.sdiemert.jgt.cli.CommandLineInterface;

import javax.swing.*;
import java.io.PrintStream;

public class ConsolePanel extends JPanel {

    Controller control;
    JTextArea ta;
    ConsoleTextArea cta;
    CommandLineInterface cli;

    public ConsolePanel(Controller c){
        this.control = c;
        this.ta = new JTextArea(10,  40);
        this.cta = new ConsoleTextArea(this.ta);
        this.cli = new CommandLineInterface(this.control.getModel());

        this.add(this.ta);
    }

}
