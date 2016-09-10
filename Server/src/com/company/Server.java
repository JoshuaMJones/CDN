package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * Created by Josh on 7/09/2016.
 */
public class Server {
    public final static String fileLocation = "/ServerFiles/";
    ServerSocket fileServer;
    OutputStream outS;
    Socket clientSocket;
    String basePath;
    public Server(int portNum){
        fileServer = null;
        basePath = new File("").getAbsolutePath();
        basePath += fileLocation;
        File directory = new File(basePath);
        if(!directory.isDirectory()){
            try{
                directory.mkdir();
            }catch(Exception e){
                System.out.println("Failed to create basePath");
            }
        }

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
                System.out.println("accepted request");
                InputStream inS = clientSocket.getInputStream();
                InputStreamReader inSR = new InputStreamReader(inS);
                BufferedReader br = new BufferedReader(inSR);
                String request = br.readLine();
                System.out.println("Received the request, string is: " + request);
                if(request.equals("getallfiles")){
                    //return what files we have.
                    System.out.println("We are going to send a list of files");
                    File directory = new File(this.basePath);
                    File[] filesInDir = directory.listFiles();
                    System.out.println(filesInDir[0].toString());
                    String files = "";
                    for(File curFile : filesInDir){
                        files += curFile.getName() + "\n";
                        files += curFile.length() + "\n";
                    }
                    System.out.println("Sending this string: \n" + files);
                    outS = clientSocket.getOutputStream();
                    outSW = new OutputStreamWriter(outS);
                    BufferedWriter bufW = new BufferedWriter(outSW);
                    bufW.write(files);
                    bufW.flush();
                }else{
                    //return a particular file that we have.

                    //File thisFile = new File(fileLocation + request);
                    File thisFile = new File(basePath + request);
                    byte[] fileByteArray = new byte[(int)thisFile.length()];
                    fileIS = new FileInputStream(thisFile);
                    bufIS = new BufferedInputStream(fileIS);
                    bufIS.read(fileByteArray, 0, fileByteArray.length);
                    outS = clientSocket.getOutputStream();
                    outS.write(fileByteArray, 0, fileByteArray.length);
                    outS.flush();
                    System.out.println("Sent the file: " + request);
                }
            }
        }catch (Exception e){
            System.out.println("Something went wrong when listening: ");
            e.printStackTrace();
        }
    }
}
