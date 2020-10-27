package com.example.app_bateaux;

import java.net.Socket;

public class SocketHandler {
    private static Socket sock;

    public static synchronized Socket getSock(){
        return sock;
    }

    public static synchronized void setSock(Socket socket){
        sock = socket;
    }
}
