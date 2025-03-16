package com.playground;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UrlShortner {

    private String shortenUrl(String url) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(url.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            // Handle the exception appropriately, e.g., log it or throw a custom exception
            System.err.println("SHA-256 algorithm not found: " + e.getMessage());
            return null; // Or throw a custom exception
        }
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static void main(String[] args) {
        UrlShortner shortener = new UrlShortner();
        String longUrl = "https://www.example.com/a/very/long/url/that/needs/to/be/shortened";
        String shortUrl = shortener.shortenUrl(longUrl);
        System.out.println("Long URL: " + longUrl);
        System.out.println("Short URL: " + shortUrl);

        String longUrl2 = "https://www.example.com/a/very/long/url/that/needs/to/be/shortened2";
        String shortUrl2 = shortener.shortenUrl(longUrl2);
        System.out.println("Long URL: " + longUrl2);
        System.out.println("Short URL: " + shortUrl2);

    }
}
