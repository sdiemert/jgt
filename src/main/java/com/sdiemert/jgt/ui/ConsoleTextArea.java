package com.sdiemert.jgt.ui;

import com.sdiemert.jgt.util.Printer;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConsoleTextArea implements Printer {

    private JTextArea textArea;
    private Controller c;

    private ArrayList<String> cmdHistory;
    private int historyIndex;

    public ConsoleTextArea(JTextArea area, Controller c){
        super();

        this.textArea = area;
        this.c = c;

        this.cmdHistory = new ArrayList<String>();
        this.historyIndex = 0;

        this.textArea.setLineWrap(true);
        this.textArea.addKeyListener(new ConsoleKeyListener(this));
        this.print(">>> ");

        cursorToEnd();

    }

    public void command() {

        try {

            int currLine = this.textArea.getLineCount() - 1;
            int endLocation = this.textArea.getLineEndOffset(currLine);
            int startLocation = this.textArea.getLineStartOffset(currLine);

            String raw = this.textArea.getText(startLocation, (endLocation - startLocation));

            Matcher cmdMatcher = Pattern.compile("(.*)>>>\\s?(.+)").matcher(raw);

            String cmdString;

            if(cmdMatcher.find()) {
                cmdString = cmdMatcher.group(2);
            }else{
                cmdString = "";
            }

            cmdHistory.add(cmdString);
            historyIndex = cmdHistory.size() - 1;

            this.nextLine();

            if(!this.c.execute(cmdString, this)) return;

            this.c.displayPrompt(this);

            cursorToEnd();

        }catch(BadLocationException e) {
            System.out.println(e.getMessage());
        }

    }

    private void nextLine(){
        this.textArea.append("\n");
    }

    public void println(String out){
        this.textArea.append(out);
        this.nextLine();
    }

    public void print(String out){
        this.textArea.append(out);
    }

    public void scrollHistoryBack(){

        if(cmdHistory.size() == 0){
            return;
        } else if(historyIndex <= 0){
            historyIndex = 0;
        }else if(historyIndex >= cmdHistory.size() - 1){
            historyIndex = cmdHistory.size() - 1;
        }

        try {
            int currLine = this.textArea.getLineCount() - 1;
            int endLocation = this.textArea.getLineEndOffset(currLine);
            int startLocation = this.textArea.getLineStartOffset(currLine);
            textArea.replaceRange(this.c.getPrompt() + cmdHistory.get(historyIndex), startLocation, endLocation);
            historyIndex--;
            cursorToEnd();
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void scrollHistoryFwd(){

        if(cmdHistory.size() == 0){
            return;
        } else if(historyIndex >= cmdHistory.size() - 1){
            historyIndex = cmdHistory.size() - 1;
        }else if(historyIndex <= 0){
            historyIndex = 0;
        }

        try {
            int currLine = this.textArea.getLineCount() - 1;
            int endLocation = this.textArea.getLineEndOffset(currLine);
            int startLocation = this.textArea.getLineStartOffset(currLine);
            textArea.replaceRange(this.c.getPrompt() + cmdHistory.get(historyIndex), startLocation, endLocation);
            historyIndex++;
            cursorToEnd();
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    void cursorToEnd(){
        this.textArea.setCaretPosition(textArea.getDocument().getLength());
    }

}
