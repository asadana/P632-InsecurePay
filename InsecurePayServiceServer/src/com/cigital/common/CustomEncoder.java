package com.cigital.common;

import java.util.Base64;

/**
 * CustomEncoder class encodes a given SSN using Base64 encoding.
 * To use this, please uncomment the main method and give the desired SSN number.
 */
public class CustomEncoder {

	// Key used to encode/decode with Base64
	private static final String key = "abc";

	/**
	 * encode is a function that passes the given string and encodes it.
	 * 
	 * @param	ssnString	Contains the string/SSN that needs to be encoded.
	 * 
	 * @return	String		Returns the encoded string.
	 */
	public static String encode(String ssnString) {
		return base64Encode(xorWithKey(ssnString.getBytes(), key.getBytes()));
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
		
		// For loop xor's each byte of the ssnString
		for (int i = 0; i < ssnStringBytes.length; i++) {
			resultObjBytes[i] = (byte) (ssnStringBytes[i] ^ keyStringBytes[i % keyStringBytes.length]);
		}
		return resultObjBytes;
	}

	/**
	 * base64Encode is a function that performs Base64 encoding of ssnStringBytes
	 * 
	 * @param	ssnStringBytes	Contains a byte array of ssnString
	 * 
	 * @return	String			Return the string generated after encoding.
	 */
	private static String base64Encode(byte[] ssnStringBytes) {
		// Replacing removing all white space characters
		return Base64.getEncoder().encodeToString(ssnStringBytes).replaceAll("\\s", "");

	}

}
