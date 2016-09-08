package com.company;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class Main {

    public static int port = 8080;
    public static void main(String[] args) {
	// write your code here
        Client thisClient = new Client(8080);

        ClientGUI thisGUI = new ClientGUI();
        /*ArrayList<String> files = thisClient.getFileNames();
        for(String cur :files){
            System.out.println(cur);
        }

        thisClient.getFile(files.get(0), Integer.parseInt(files.get(1)));
        */
    }
}
