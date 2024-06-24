package com.steffenboe.broadcast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

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
                String message = getMessageFrom(clientSocket);
                node.broadcast(message);
            }
        } catch (BindException e) {
            System.out.println("Port " + port + " is already in use");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            System.exit(1);

        }
    }

    private String getMessageFrom(Socket clientSocket) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
        String message = bufferedReader.readLine();
        bufferedReader.close();
        return message;
    }

}
