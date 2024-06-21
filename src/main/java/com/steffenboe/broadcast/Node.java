package com.steffenboe.broadcast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Set;

public class Node {

    private Set<String> neighbors;

    Node(Set<String> neighbors) {
        this.neighbors = neighbors;
    }

    public void broadcast(InputStream inputStream) throws IOException {
        final String message = readLineFrom(inputStream);
        System.out.println("Received: " + message);
        forwardToNeighbors(message);
    }

    private void forwardToNeighbors(final String message) {
        Thread.ofVirtual().start(() -> {
            neighbors.forEach(neighbor -> {
                forwardMessage(message, neighbor);
            });
        });
    }

    private void forwardMessage(String message, String neighbor) {
        try {
            forwardMessageToNeighbor(message, neighbor);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readLineFrom(InputStream inputStream) throws IOException {
        return new BufferedReader(
                new InputStreamReader(inputStream)).readLine();
    }

    private void forwardMessageToNeighbor(String message, String neighbor) throws IOException {
        try (Socket socket = new Socket(neighbor.split(":")[0],
                Integer.parseInt(neighbor.split(":")[1]))) {
            System.out.println("Sending message to " + neighbor + "...");
            writeToOutputStream(message, socket);
            System.out.println("Sent: " + message + " to " + neighbor);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void writeToOutputStream(String message, Socket socket) throws IOException {
        socket.getOutputStream().write(message.getBytes());
    }

}
