package com.playground.udpserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPServer {
    public static void main(String[] args) throws IOException {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : 6000;
        DatagramSocket socket = new DatagramSocket(port);
        System.out.println("UDP Server listening on port: " + port);

        byte[] buffer = new byte[4096];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        long totalBytes = 0;
        while (true) {
            socket.receive(packet);
            totalBytes += packet.getLength();

            if (packet.getLength() == 0) {
                break; // Stop on empty packet
            }
        }

        System.out.println("Total bytes received: " + totalBytes / (1024 * 1024) + " MB");
        socket.close();
        System.out.println("UDP Server finished.");
    }
}




