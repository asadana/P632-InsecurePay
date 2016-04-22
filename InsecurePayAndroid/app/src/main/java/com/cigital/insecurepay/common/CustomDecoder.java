package com.cigital.insecurepay.common;

import android.util.Base64;

/**
 * CustomDecoder is a class that displays the decoded SSN Number. Encode operation is performed by XORing the SSN No
 * with the hardcoded key and then Base64 encoding is applied on the XORed string
 */
public class CustomDecoder {

    //Key used for XOR function
    private static final String key = "abc";

    /**
     * decode is a function that receives the encoded SSN NO as string and decodes it.
     *
     * @param	ssnString	Contains the string/SSN that needs to be decoded.
     *
     * @return	String		Returns the decoded string.
     */
    public static String decode(String ssnString) {
        return new String(xorWithKey(base64Decode(ssnString), key.getBytes()));
    }

    /**
     * xorWithKey is a function to XOR the SSN with key
     *
     * @param	ssnStringBytes	Contains the byte array from the ssn string
     * @param	keyStringBytes	Contains the byte array from the key string
     *
     * @return	byte[]			Returns the xor'ed byte array
     */
    private static byte[] xorWithKey(byte[] ssnStringBytes, byte[] keyStringBytes) {
        byte[] resultObjBytes = new byte[ssnStringBytes.length];
        // For loop to XOR each byte of the ssnString
        for (int i = 0; i < ssnStringBytes.length; i++) {
            resultObjBytes[i] = (byte) (ssnStringBytes[i] ^ keyStringBytes[i % keyStringBytes.length]);
        }
        return resultObjBytes;
    }

    /**
     * base64Decode is a function that performs Base64 decoding of ssnStringBytes
     *
     * @param	ssnStringBytes	Contains a byte array of ssnString
     *
     * @return	String			Return the string generated after decoding.
     */
    private static byte[] base64Decode(String ssnStringBytes) {
        return Base64.decode(ssnStringBytes, Base64.DEFAULT);
    }

}

