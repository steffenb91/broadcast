package com.steffenboe.broadcast;

import java.util.HashSet;
import java.util.Set;

public class Broadcast {

    private Set<String> neighbors = new HashSet<String>();
    private String port;

    public Broadcast(String port) {
        this.port = port;
        System.out.println("Starting on port " + port + "...");
    }

    public void init() {
        Server server = new Server(port, new BroadcastingNode(neighbors));
        server.start();
    }

    public void addNeighbor(String neighbor) {
        neighbors.add(neighbor);
    }

}
