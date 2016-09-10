package com.company;

public class Main {

    public static void main(String[] args) {
        int inPort = 8080, outPort = 8081;
        Cache thisCache = new Cache(inPort, outPort);
        CacheGUI thisGUI = new CacheGUI(thisCache);
        thisCache.listen();

    }
}
