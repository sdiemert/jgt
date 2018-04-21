package com.sdiemert.jgt.ui;

import javax.swing.*;

public class ViewerPanel extends JPanel {

    Controller control;

    public ViewerPanel(Controller c){
        this.control = c;

        this.add(new JLabel("foo"));
    }
}
