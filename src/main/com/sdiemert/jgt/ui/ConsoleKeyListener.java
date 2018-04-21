package com.sdiemert.jgt.ui;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class ConsoleKeyListener implements KeyListener {

    ConsoleTextArea cta;

    public ConsoleKeyListener(ConsoleTextArea cta){
        this.cta = cta;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            cta.command();
            e.consume();
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
