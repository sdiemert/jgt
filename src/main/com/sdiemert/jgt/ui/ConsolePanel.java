package com.sdiemert.jgt.ui;

import com.sdiemert.jgt.cli.CommandLineInterface;

import javax.swing.*;
import java.awt.*;
import java.io.PrintStream;

public class ConsolePanel extends JPanel {

    Controller control;
    JTextArea ta;
    ConsoleTextArea cta;
    CommandLineInterface cli;
    JScrollPane sp;

    public ConsolePanel(Controller c){
        this.control = c;
        this.ta = new JTextArea(10,  40);
        this.cli = new CommandLineInterface(this.control.getModel());
        this.cta = new ConsoleTextArea(this.ta, this.cli);

        this.sp = new JScrollPane(this.ta);


        this.setLayout(new BorderLayout());


        this.add(this.sp, BorderLayout.CENTER);
    }

}
