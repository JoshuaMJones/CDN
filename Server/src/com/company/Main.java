package com.company;

public class Main {

    private static int port = 8081;

    public static void main(String[] args) {
	// write your code here
        Server thisServer = new Server(port);
        thisServer.listen();

    }
}
