package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.function.BooleanSupplier;

/**
 * Created by ArO on 9/09/2016.
 */
public class Cache {
    private int clientPort, serverPort;
    private Socket serverSocket;
    private ServerSocket cacheSocket;
    private Socket clientSocket;
    public String fileDir = "/Files/";
    ArrayList<String> fileNames;
    ArrayList<String> cacheLogs;
    ArrayList<String> serverFileNames;
    DateFormat dateFormat;
    Boolean change;
    File directory;
    File[] filesInDir;

    public Cache(int inPort, int outPort){
        String basePath = new File("").getAbsolutePath();
        fileDir = basePath + fileDir;
        clientPort = inPort;
        serverPort = outPort;
        cacheLogs = new ArrayList<String>();
        fileNames = new ArrayList<String>();
        serverFileNames = new ArrayList<String>();
        dateFormat = new SimpleDateFormat("HH:mm:ss yyyy/MM/dd");
        change = false;
        directory = new File(fileDir);
        filesInDir = directory.listFiles();
        for(File curFile : filesInDir){
            fileNames.add(curFile.getName());
        }
        try{
            cacheSocket = new ServerSocket(clientPort);
        }catch(Exception e){
            System.out.println("setting up the listening socket didn't work");
            e.printStackTrace();
        }
    }

    public void listen(){
        System.out.println("simulating the listen part");
        try {
            while (true) {
                System.out.println("Waiting for the client");
                clientSocket = cacheSocket.accept();
                String currentTime = dateFormat.format(Calendar.getInstance().getTime());
                //String currentTime = dateFormat.format(LocalDateTime.now().toLocalDate().toString());
                System.out.println("received client request");
                InputStream inS = clientSocket.getInputStream();
                InputStreamReader inSR = new InputStreamReader(inS);
                BufferedReader br = new BufferedReader(inSR);
                String request = br.readLine();
                System.out.println("Received the request, string is: " + request);
                if(request.equals("getallfiles")){
                    //forward the request to the server.
                    serverSocket = new Socket("127.0.0.1", serverPort);

                    String userRequest = "user request: Asked server for list of it's files at: " + currentTime;
                    String response = "response: sent the user the list of files";
                    cacheLogs.add(userRequest);
                    cacheLogs.add(response);

                    OutputStream outS = serverSocket.getOutputStream();
                    OutputStreamWriter outSW = new OutputStreamWriter(outS);
                    BufferedWriter bufW = new BufferedWriter(outSW);

                    bufW.write(request + "\n");
                    bufW.flush();
                    System.out.println("sent request to server");

                    InputStream inServer = serverSocket.getInputStream();
                    InputStreamReader inServerR = new InputStreamReader(inServer);
                    BufferedReader inServerBuf = new BufferedReader(inServerR);

                    OutputStream outClient = clientSocket.getOutputStream();
                    OutputStreamWriter outClientSW = new OutputStreamWriter(outClient);
                    BufferedWriter outClientBuf = new BufferedWriter(outClientSW);

                    //Write from the server input stream to an arraylist
                    String curLine = inServerBuf.readLine();
                    String cumulativeFileNames = "";
                    //TODO do we need to change this while?
                    //
                    //
                    //
                    //
                    //
                    //
                    //
                    //
                    while(!curLine.equals(null)){

                        serverFileNames.add(curLine);
                        cumulativeFileNames += curLine + "\n";
                        if(!inServerBuf.ready()){
                            break;
                        }
                        curLine = inServerBuf.readLine();
                    }
                    //And then write them to the client output stream

                    outClientBuf.write(cumulativeFileNames);
                    outClientBuf.flush();

                    serverSocket.close();
                }else{
                    //it will be requesting a particular file
                    String thisFileLocation = fileDir + request;
                    System.out.println("the file should be at: " + thisFileLocation);
                    File thisFile = new File(thisFileLocation);
                    String userRequest = "user request: file:" + request + " at: " + currentTime;
                    String response = "response: ";
                    int fileSize = Integer.parseInt(br.readLine());
                    byte[] fileByteArray = new byte[fileSize];
                    System.out.println("We have a byte[] of size: " + fileSize);
                    if(thisFile.exists()){
                        //then we return this file
                        System.out.println("We already have the file");
                        response += "cached file " + request;
                        FileInputStream fileIS = new FileInputStream(thisFile);
                        BufferedInputStream bufIS = new BufferedInputStream(fileIS);
                        bufIS.read(fileByteArray,0,fileByteArray.length);
                    }else{
                        //send a request to the server to get the file
                        //Also create the file here(cache it)
                        response += "file " + request + "downloaded from the server";
                        System.out.println("Downloading the file");
                        serverSocket = new Socket("127.0.0.1", serverPort);
                        OutputStream outS = serverSocket.getOutputStream();
                        OutputStreamWriter outSW = new OutputStreamWriter(outS);
                        BufferedWriter bufW = new BufferedWriter(outSW);
                        bufW.write(request + "\n");
                        bufW.flush();
                        System.out.println("sent message to server to get the file: " + request);
                        InputStream serverIn = serverSocket.getInputStream();

                        FileOutputStream fileOS = new FileOutputStream(thisFileLocation);
                        BufferedOutputStream bufOS = new BufferedOutputStream((fileOS));
                        System.out.println("Contents of the byteArray");

                        serverIn.read(fileByteArray,0,fileByteArray.length);

                        for(int i = 0; i < fileByteArray.length; i++){
                            System.out.println(fileByteArray[i]);
                        }

                        bufOS.write(fileByteArray);
                        bufOS.flush();
                        bufOS.close();

                        fileNames.add(request);
                    }
                    cacheLogs.add(userRequest);
                    cacheLogs.add(response);

                    //return the file to the client

                    OutputStream outS = clientSocket.getOutputStream();
                    outS.write(fileByteArray, 0, fileByteArray.length);
                    outS.flush();
                    outS.close();

                }
                change = true;
                clientSocket.close();

            }
        }catch(Exception e){
            System.out.println("Something went wrong while listening for client");
            e.printStackTrace();
        }
    }
}
