package com.playground.tcpserver;

// TCPClient.java - Sends 1GB of data
import java.io.*;
import java.net.*;

public class TCPClient {
    public static void main(String[] args) throws IOException {
        String serverAddress = args.length > 0 ? args[0] : "localhost";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 6000;

        Socket socket = new Socket(serverAddress, port);
        System.out.println("Connected to TCP server: " + serverAddress + ":" + port);

        DataOutputStream outputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));

        byte[] buffer = new byte[4096];
        long totalBytes = 1024L * 1024 * 1024; // 1 GB

        for (long sent = 0; sent < totalBytes; sent += buffer.length) {
            outputStream.write(buffer);
        }

        outputStream.flush();
        System.out.println("Sent: " + totalBytes / (1024 * 1024) + " MB");

        outputStream.close();
        socket.close();
        System.out.println("TCP Client finished.");
    }
}

// Instructions to run:
// 1. Compile: javac TCPServer.java TCPClient.java
// 2. Start Server 2: java TCPServer 6000
// 3. Start Server 1 (Client role): java TCPClient localhost 6000
