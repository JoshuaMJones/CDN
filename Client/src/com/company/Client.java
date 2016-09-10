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
    public String fileDir = "/ClientFiles/";
    ArrayList<String> fileNames;
    public Client(int port){
        String basePath = new File("").getAbsolutePath();
        fileDir = basePath + fileDir;
        portNum = port;
    }

    public ArrayList<String> getFileNames(){
        fileNames = new ArrayList<String>();
        try{
            clientSocket = new Socket("127.0.0.1", portNum);

            OutputStream outS = clientSocket.getOutputStream();
            OutputStreamWriter outSW = new OutputStreamWriter(outS);
            BufferedWriter bufW = new BufferedWriter(outSW);

            String message = "getallfiles\n";
            bufW.write(message);
            bufW.flush();
            System.out.println("sent message to server to get file names");

            //Receiving the file names

            InputStream inS = clientSocket.getInputStream();
            InputStreamReader inSR = new InputStreamReader(inS);
            BufferedReader bufR = new BufferedReader(inSR);
            String curLine = bufR.readLine();
            while(!curLine.equals(null)){

                fileNames.add(curLine);
                if(!bufR.ready()){
                    break;
                }
                curLine = bufR.readLine();
            }

        }catch(Exception e){
            System.out.println("Problems during getting files:");
            e.printStackTrace();
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
    public void getFile(String fileName, int fileSize){
        try{
            clientSocket = new Socket("127.0.0.1", portNum);

            OutputStream outS = clientSocket.getOutputStream();
            OutputStreamWriter outSW = new OutputStreamWriter(outS);
            BufferedWriter bufW = new BufferedWriter(outSW);

            bufW.write(fileName + "\n" + fileSize + "\n");
            bufW.flush();
            System.out.println("sent message to server to get the file: " + fileName);

            //int tempFileSize = 1000;
            //byte[] fileByteArray = new byte[tempFileSize];
            byte[] fileByteArray = new byte[fileSize];
            InputStream inS = clientSocket.getInputStream();


            String fileLocation = fileDir + fileName;
            FileOutputStream fileOS = new FileOutputStream(fileLocation);
            BufferedOutputStream bufOS = new BufferedOutputStream((fileOS));

            int bytesRead = inS.read(fileByteArray,0,fileByteArray.length);
            //int current = bytesRead;
            /*System.out.println("about to enter while");
            for(int i =0; i <fileByteArray.length; i++){
                System.out.println(fileByteArray[i]);
            }*/

            bufOS.write(fileByteArray);
            //System.out.println("About to write to file");
            //bufOS.write(fileByteArray,0,current);
            bufOS.flush();

            System.out.println("Received file from server: " + fileName);
            bufOS.close();
            //fileOS.close();
            clientSocket.close();

        }catch(Exception e){
            System.out.println("Problem occured while transferring file: " + fileName);
            e.printStackTrace();
        }finally {
            try{
                clientSocket.close();
            }
            catch(Exception e)
            {
                System.out.println("failed to close the socket");
            }
        }
    }
}
