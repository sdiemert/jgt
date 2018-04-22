package com.sdiemert.jgt.ui;

import com.sdiemert.jgt.cli.Parser;
import com.sdiemert.jgt.util.Printer;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.util.ArrayList;

public class ConsoleTextArea implements Printer {

    private StringBuilder sb;
    private JTextArea textArea;
    private Parser parser;
    private Controller c;

    private ArrayList<String> cmdHistory;
    private int historyIndex;

    private int index;

    public ConsoleTextArea(JTextArea area, Controller c){
        super();
        sb = new StringBuilder();
        this.textArea = area;
        this.cmdHistory = new ArrayList<String>();
        this.historyIndex = 0;

        this.c = c;

        this.textArea.setLineWrap(true);
        this.parser = new Parser();
        this.textArea.addKeyListener(new ConsoleKeyListener(this));
        this.print(">>> ");
        this.textArea.setCaretPosition(this.index);

        System.out.println(this.index);

    }

    public void command() {

        try {

            int endLocation = this.textArea.getLineEndOffset(this.textArea.getLineCount() - 1);

            String cmdString = this.textArea.getText(index, (endLocation - index));

            cmdHistory.add(cmdString);
            historyIndex = cmdHistory.size() - 1;

            this.nextLine();

            if(!this.c.execute(cmdString, this)) return;

            this.c.displayPrompt(this);

            this.index += cmdString.length();
            this.textArea.setCaretPosition(this.index);

        }catch(BadLocationException e) {
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
