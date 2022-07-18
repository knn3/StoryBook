package com.example.storbook;

import android.app.Application;

// ALL GLOBAL variables will be stored in this section

public class global extends Application {
    // CareTaker mode

    boolean isCaretaker = false;
    public boolean isCaretaker() {
        return isCaretaker;
    }
    public void setCaretaker(boolean caretaker) {
        isCaretaker = caretaker;
    }

    //
    private String someVariable;
    public String getSomeVariable() {
        return someVariable;
    }
    public void setSomeVariable(String someVariable) {
        this.someVariable = someVariable;
    }
    //
}