package com.cigital.insecurepay.common;

import android.util.Base64;


public class CustomDecoder {

    private static final String key = "abc";

    public static String decode(String s) {
        return new String(xorWithKey(base64Decode(s), key.getBytes()));
    }

    private static byte[] xorWithKey(byte[] a, byte[] key) {
        byte[] out = new byte[a.length];
        for (int i = 0; i < a.length; i++) {
            out[i] = (byte) (a[i] ^ key[i % key.length]);
        }
        return out;
    }

    private static byte[] base64Decode(String s) {
        return Base64.decode(s, Base64.DEFAULT);
    }

}

