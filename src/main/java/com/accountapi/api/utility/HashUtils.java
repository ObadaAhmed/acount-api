package com.accountapi.api.utility;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtils {
    public static String hashString(String input) {
        try {
            // Create a MessageDigest object with SHA-256 algorithm
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Convert the input string to bytes
            byte[] encodedHash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            // Convert the byte array to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedHash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            // Handle the exception if the SHA-256 algorithm is not available
            e.printStackTrace();
            return null;
        }
    }
}