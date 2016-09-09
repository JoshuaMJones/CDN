package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
/**
 * Created by ArO on 9/09/2016.
 */
public class Cache {
    private int clientPort, serverPort;
    private Socket serverSocket;
    private ServerSocket clientSocket;
    public String fileDir = "/Files/";
    ArrayList<String> fileNames;
    ArrayList<String> cacheLogs;
    public Cache(int inPort, int outPort){
        String basePath = new File("").getAbsolutePath();
        fileDir = basePath + fileDir;
        clientPort = inPort;
        serverPort = outPort;
    }
}
