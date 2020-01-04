package com.onica;

public class Main {

    public static void main(String[] args) throws Exception {
        BookManager bookManager = new BookManager();
        bookManager.loadFileToBookMap();
        bookManager.manageAction();
    }

}
