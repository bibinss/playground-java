package com.playground.tcpserver;

// TCPServer.java - Receives and verifies data
import java.io.*;
import java.net.*;

public class TCPServer {
    public static void main(String[] args) throws IOException {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 6000;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("TCP Server listening on port: " + port);

        Socket socket = serverSocket.accept();
        System.out.println("Connection established with: " + socket.getInetAddress());

        DataInputStream inputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        long totalBytes = 0;

        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            totalBytes += bytesRead;
        }

        System.out.println("Received: " + totalBytes / (1024 * 1024) + " MB");

        inputStream.close();
        socket.close();
        serverSocket.close();
        System.out.println("TCP Server finished.");
    }
}
