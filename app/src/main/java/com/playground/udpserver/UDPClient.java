package com.playground.udpserver;

// UDPClient.java - Sends 1GB of data
import java.io.*;
import java.net.*;

public class UDPClient {
    public static void main(String[] args) throws IOException {
        String serverAddress = args.length > 0 ? args[0] : "localhost";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 6000;

        DatagramSocket socket = new DatagramSocket();
        InetAddress server = InetAddress.getByName(serverAddress);

        byte[] buffer = new byte[4096];
        long totalBytes = 1024L * 1024 * 1024; // 1 GB

        System.out.println("Sending 1GB to " + serverAddress + ":" + port);

        for (long sent = 0; sent < totalBytes; sent += buffer.length) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, server, port);
            socket.send(packet);
        }

        // Send an empty packet to signal completion
        socket.send(new DatagramPacket(new byte[0], 0, server, port));

        System.out.println("1GB sent successfully.");
        socket.close();
    }
}

// Instructions to run:
// 1. Compile: javac UDPServer.java UDPClient.java
// 2. Start Server 2: java UDPServer 6000
// 3. Start Server 1 (Client role): java UDPClient localhost 6000
