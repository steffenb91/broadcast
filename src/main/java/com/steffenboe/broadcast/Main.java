package com.steffenboe.broadcast;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Main {
    public static void main(String[] args) throws UnknownHostException, IOException {
        if(args[0].equals("c")){
            Socket socket = new Socket("localhost", 8080);
            socket.getOutputStream().write("Hello World".getBytes());
            socket.close();
        }
        if(args.length >= 1 && !args[0].equals("c")){
            BroadcastNode node = new BroadcastNode(args[0]);
            if(args.length == 2){
                node.addNeighbor("localhost:" + args[1]);
            }
            node.init();
        }
    }
}