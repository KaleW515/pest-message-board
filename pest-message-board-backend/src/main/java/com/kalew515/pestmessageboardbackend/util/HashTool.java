package com.kalew515.pestmessageboardbackend.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class HashTool {
    public static String SHA256sum (String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] sha256 = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuffer sha256Hex = new StringBuffer();
            for (int i = 0; i < sha256.length; i++) {
                String hex = Integer.toHexString(0xff & sha256[i]);
                if (hex.length() == 1) {
                    sha256Hex.append('0');
                }
                sha256Hex.append(hex);
            }
            return sha256Hex.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getRandomString (Integer length) {
        String charset = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(charset.charAt(random.nextInt(charset.length())));
        }
        return sb.toString();
    }
}
