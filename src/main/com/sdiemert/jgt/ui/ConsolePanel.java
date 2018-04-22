package com.sdiemert.jgt.ui;

import javax.swing.*;
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
        this.setLayout(new BorderLayout());
        this.add(this.sp, BorderLayout.CENTER);
    }

}
