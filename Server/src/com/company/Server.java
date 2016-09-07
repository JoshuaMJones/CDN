package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * Created by Josh on 7/09/2016.
 */
public class Server {
    public final static String fileLocation = "Server/Files/";
    ServerSocket fileServer;
    OutputStream outS;
    InputStream inS;
    Socket clientSocket;
    public Server(int portNum){
        fileServer = null;
        try{
            fileServer = new ServerSocket(portNum);
        }catch (Exception e){
            System.out.println("Setting up the fileserver socket didn't work: " + e);
        }
    }

    public void listen(){
        clientSocket = null;
        FileInputStream fileIS = null;
        BufferedInputStream bufIS = null;
        OutputStreamWriter outSW = null;
        try{
            while(true){
                System.out.println("waiting for client");
                clientSocket = fileServer.accept();
                inS = clientSocket.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inS));
                String request = br.readLine();
                if(request.equals("getallfiles")){
                    //return what files we have.
                    File directory = new File(fileLocation);
                    File[] filesInDir = directory.listFiles();
                    String files = "";
                    for(File curFile : filesInDir){
                        files += curFile.getName() + "\n";
                        files += curFile.length() + "\n";
                    }
                    outS = clientSocket.getOutputStream();
                    outSW = new OutputStreamWriter(outS);
                    BufferedWriter bufW = new BufferedWriter(outSW);
                    bufW.write(files);
                    bufW.flush();
                }else{
                    //return a particular file that we have.

                    File thisFile = new File(fileLocation + request);
                    byte[] fileByteArray = new byte[(int)thisFile.length()];
                    fileIS = new FileInputStream(thisFile);
                    bufIS = new BufferedInputStream(fileIS);
                    bufIS.read(fileByteArray, 0, fileByteArray.length);
                    outS = clientSocket.getOutputStream();
                    outS.write(fileByteArray, 0, fileByteArray.length);
                    outS.flush();
                }
            }
        }catch (Exception e){
            System.out.println("Something went wrong when listening: " + e);
        }
    }
}
