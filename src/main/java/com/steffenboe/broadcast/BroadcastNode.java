package com.steffenboe.broadcast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class BroadcastNode {

    private static final String ID = UUID.randomUUID().toString();

    private Set<String> neighbors = new HashSet<String>();
    private String port;

    public BroadcastNode(String port) {
        this.port = port;
        System.out.println(ID + ": Starting on port " + port + "...");
    }

    public void init() {
        try (ServerSocket serverSocket = new ServerSocket(Integer.parseInt(port))) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                Thread.ofVirtual().start(() -> {
                    try (PrintWriter out = getPrintWriter(clientSocket);
                            BufferedReader in = getBufferedReader(clientSocket);) {
                        String message = in.readLine();
                        System.out.println(ID + ": Received: " + message);
                        neighbors.forEach(neighbor -> {
                            try (Socket socket = new Socket(neighbor.split(":")[0],
                                    Integer.parseInt(neighbor.split(":")[1]))) {
                                System.out.println(ID + ": Sending message to " + neighbor + "...");
                                socket.getOutputStream().write(message.getBytes());
                                System.out.println(ID + ": Sent: " + message + " to " + neighbor);
                            } catch (Exception e) {
                                System.out.println(ID + ": Error: " + e.getMessage());
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (BindException e) {
            System.out.println("Port " + port + " is already in use");
            System.exit(1);
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            System.exit(1);

        }
    }

    private BufferedReader getBufferedReader(Socket clientSocket) throws IOException {
        return new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
    }

    private PrintWriter getPrintWriter(Socket clientSocket) throws IOException {
        return new PrintWriter(clientSocket.getOutputStream(), true);

    }

    public void addNeighbor(String neighbor) {
        neighbors.add(neighbor);
    }

}
