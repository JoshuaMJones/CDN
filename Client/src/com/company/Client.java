package com.company;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Josh on 7/09/2016.
 */
public class Client {
    private int portNum;
    private Socket clientSocket;
    public Client(int port){
        portNum = port;
    }

    public ArrayList<String> getFileNames(){
        ArrayList<String> fileNames = new ArrayList<String>();
        try{
            clientSocket = new Socket("127.0.0.1", portNum);

            OutputStream outS = clientSocket.getOutputStream();
            OutputStreamWriter outSW = new OutputStreamWriter(outS);
            BufferedWriter bufW = new BufferedWriter(outSW);

            String message = "getallfiles";
            bufW.write(message);
            bufW.flush();
            System.out.println("sent message to server to get file names");

            //Receiving the file names

            InputStream inS = clientSocket.getInputStream();
            InputStreamReader inSR = new InputStreamReader(inS);
            BufferedReader bufR = new BufferedReader(inSR);
            String curLine = bufR.readLine();
            while(curLine != null){
                fileNames.add(curLine);
            }

        }catch(Exception e){
            System.out.println("Problems during getting files" + e);
        }finally{
            try{
                clientSocket.close();
            }
            catch(Exception e)
            {
                System.out.println("failed to close the socket");
            }
        }

        return fileNames;
    }
    public void getFile(String fileName){

    }
}