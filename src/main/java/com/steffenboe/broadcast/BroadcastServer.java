package com.steffenboe.broadcast;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

class BroadcastServer {

    private String port;
    private Node node;

    BroadcastServer(String port, Node node) {
        this.port = port;
        this.node = node;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(Integer.parseInt(port))) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                node.broadcast(clientSocket.getInputStream());
            }
        } catch (BindException e) {
            System.out.println("Port " + port + " is already in use");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            System.exit(1);

        }
    }

}
