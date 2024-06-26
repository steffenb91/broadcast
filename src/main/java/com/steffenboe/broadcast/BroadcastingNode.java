package com.steffenboe.broadcast;

import java.io.IOException;
import java.net.Socket;
import java.util.Set;

class BroadcastingNode {

    private Set<String> neighbors;

    BroadcastingNode(Set<String> neighbors) {
        this.neighbors = neighbors;
    }

    public void broadcast(String message) throws IOException {
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
