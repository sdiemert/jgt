package com.sdiemert.jgt.ui;

import com.sdiemert.jgt.cli.Session;

public class Controller {

    MainFrame view;
    Session model;

    public Controller(MainFrame view, Session model){
        this.view = view;
        this.model = model;
    }

    public Controller(){
        this.model = new Session();
        this.view = new MainFrame(this);

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
}
