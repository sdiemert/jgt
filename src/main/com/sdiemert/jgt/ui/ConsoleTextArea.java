package com.sdiemert.jgt.ui;

import com.sdiemert.jgt.util.Printer;

import javax.swing.*;
import javax.swing.text.BadLocationException;

public class ConsoleTextArea implements Printer {

    private StringBuilder sb;
    private JTextArea textArea;

    private int index;

    public ConsoleTextArea(JTextArea area){
        super();
        sb = new StringBuilder();
        this.textArea = area;


        this.textArea.setLineWrap(true);

        this.textArea.addKeyListener(new ConsoleKeyListener(this));

        this.print(">>> ");

        this.textArea.setCaretPosition(this.index);

        System.out.println(this.index);

    }

    public void command() {

        try {

            int endLocation = this.textArea.getLineEndOffset(this.textArea.getLineCount() - 1);

            String cmdString = this.textArea.getText(index, (endLocation - index));

            System.out.println(index + " " + endLocation + " '"+cmdString+"'");

            this.nextLine();
            this.println(cmdString);
            this.print(">>> ");

            this.index += cmdString.length();
            this.textArea.setCaretPosition(this.index);

        }catch(BadLocationException e){
            System.out.println(e.getMessage());
        }

    }

    private void nextLine(){
        this.textArea.append("\n");
        this.index+=1;
    }

    public void println(String out){
        this.textArea.append(out);
        this.index += out.length();
        this.nextLine();

    }

    public void print(String out){
        this.textArea.append(out);
        this.index += out.length();
    }

}
