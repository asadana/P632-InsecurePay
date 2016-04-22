package com.cigital.insecurepay.common;

import android.util.Base64;

/**
 * CustomDecoder is a class that displays the decoded SSN Number. Encode operation is performed by XORing the SSN No
 * with the hardcoded key and then Base64 encoding is applied on the XORed string
 */
public class CustomDecoder {

    //Key for XOR function
    private static final String key = "abc";

    //Main Decode function which calls Decode and XOR function
    public static String decode(String s) {
        return new String(xorWithKey(base64Decode(s), key.getBytes()));
    }

    //XOR with key function
    private static byte[] xorWithKey(byte[] a, byte[] key) {
        byte[] out = new byte[a.length];
        for (int i = 0; i < a.length; i++) {
            out[i] = (byte) (a[i] ^ key[i % key.length]);
        }
        return out;
    }

    //Base64 Decode function
    private static byte[] base64Decode(String s) {
        return Base64.decode(s, Base64.DEFAULT);
    }

}

