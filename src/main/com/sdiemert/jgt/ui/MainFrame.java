package com.sdiemert.jgt.ui;

import com.sdiemert.jgt.cli.Session;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainFrame extends JFrame {

    ViewerPanel viewer;
    ConsolePanel console;

    Controller c;


    public MainFrame(Controller c){

        this.c = c;

        viewer = new ViewerPanel(this.c);
        console = new ConsolePanel(this.c);

        this.setLayout(new GridLayout());

        this.add(viewer);
        this.add(console);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000, 600);

    }

    public void run(){
        this.setVisible(true);
    }

    public ViewerPanel getViewer() {
        return viewer;
    }
}
