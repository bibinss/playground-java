package com.playground;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;

public class DnsServer {

    private static final int DNS_PORT = 53;

    public static void main(String[] args) throws IOException {
        try (DatagramSocket socket = new DatagramSocket(DNS_PORT)) {
            System.out.println("DNS Server is running on port " + DNS_PORT);

            while (true) {
                byte[] buffer = new byte[512];
                DatagramPacket requestPacket = new DatagramPacket(buffer, buffer.length);
                socket.receive(requestPacket);

                System.out.println("Received DNS query from: " + requestPacket.getAddress());

                byte[] response = handleDnsRequest(requestPacket.getData(), requestPacket.getLength());
                DatagramPacket responsePacket = new DatagramPacket(response, response.length, requestPacket.getAddress(), requestPacket.getPort());

                socket.send(responsePacket);
                System.out.println("Sent DNS response.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] handleDnsRequest(byte[] request, int length) {
        ByteBuffer buffer = ByteBuffer.allocate(512);

        // Copy transaction ID
        buffer.put(request, 0, 2);

        // Flags (response, no error)
        buffer.putShort((short) 0x8180);

        // Questions = 1, Answers = 1, Authority = 0, Additional = 0
        buffer.putShort((short) 1);
        buffer.putShort((short) 1);
        buffer.putShort((short) 0);
        buffer.putShort((short) 0);

        // Copy the original query section
        buffer.put(request, 12, length - 12);

        // Answer section (hardcoded response for "example.com")
        buffer.put((byte) 0xC0); // Pointer to the question
        buffer.put((byte) 0x0C);

        // Type: A (IPv4 address)
        buffer.putShort((short) 0x0001);

        // Class: IN (Internet)
        buffer.putShort((short) 0x0001);

        // TTL: 60 seconds
        buffer.putInt(60);

        // Data length: 4 bytes (IPv4 address)
        buffer.putShort((short) 4);

        // IP address: 192.168.1.1
        buffer.put(new byte[]{(byte) 192, (byte) 168, 1, 1});

        return buffer.array();
    }
}
