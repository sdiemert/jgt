package com.sdiemert.jgt.ui;

import com.sdiemert.jgt.cli.CommandLineInterface;
import com.sdiemert.jgt.cli.Session;
import com.sdiemert.jgt.cli.scope.GraphScope;
import com.sdiemert.jgt.util.Printer;

public class Controller {

    MainFrame view;
    Session model;
    CommandLineInterface cli;

    public Controller(MainFrame view, Session model){
        this.view = view;
        this.model = model;
        this.cli = new CommandLineInterface(this.model);
    }

    public Controller(){
        this.model = new Session();
        this.view = new MainFrame(this);
        this.cli = new CommandLineInterface(this.model);

    }

    public void run(){
        this.view.run();
    }

    public MainFrame getView() {
        return view;
    }

    public Session getModel() {
        return model;
    }

    public boolean execute(String cmdString, Printer output){
        boolean result = this.cli.repl(cmdString, output);

        if(!result) return false;
        else{
            this.updateViewWithScope();
            return true;
        }
    }

    public void displayPrompt(Printer output){
        this.cli.printPrompt(output);
    }

    private void updateViewWithScope(){
        if(this.model.getScope() instanceof GraphScope){
            this.view.getViewer().displayGraph((GraphScope) this.model.getScope());
        }
    }
}
