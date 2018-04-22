package com.sdiemert.jgt.ui;

import com.sdiemert.jgt.cli.CommandLineInterface;
import com.sdiemert.jgt.cli.Parser;
import com.sdiemert.jgt.cli.ParserException;
import com.sdiemert.jgt.cli.command.Command;
import com.sdiemert.jgt.cli.scope.ScopeException;
import com.sdiemert.jgt.core.GraphException;
import com.sdiemert.jgt.core.RuleException;
import com.sdiemert.jgt.util.Printer;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.io.IOException;

public class ConsoleTextArea implements Printer {

    private StringBuilder sb;
    private JTextArea textArea;
    private Parser parser;
    private CommandLineInterface c;

    private int index;

    public ConsoleTextArea(JTextArea area, CommandLineInterface c){
        super();
        sb = new StringBuilder();
        this.textArea = area;

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

            this.nextLine();

            if(!this.c.repl(cmdString, this)) return;

            this.c.printPrompt(this);

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
