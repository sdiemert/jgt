package com.sdiemert.jgt.ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ConsolePanel extends JPanel {

    Controller control;
    JTextArea ta;
    ConsoleTextArea cta;

    JScrollPane sp;

    public ConsolePanel(Controller c){
        this.control = c;
        this.ta = new JTextArea(10,  40);
        this.cta = new ConsoleTextArea(this.ta, this.control);

        this.sp = new JScrollPane(this.ta);
        this.setBorder(new EmptyBorder(10,5,10,10));
        this.setLayout(new BorderLayout());
        this.add(this.sp, BorderLayout.CENTER);
    }

}
