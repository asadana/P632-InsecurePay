package com.cigital.common;

import java.util.Base64;

/*
 * Encodes SSN using Base64 encoding
 */

public class CustomEncoder {

	private static final String key = "abc";

	public static String encode(String s) {
		return base64Encode(xorWithKey(s.getBytes(), key.getBytes()));
	}

	/*
	 * Function to XOR the SSN with key 'abc'
	 */
	private static byte[] xorWithKey(byte[] a, byte[] key) {
		byte[] out = new byte[a.length];
		for (int i = 0; i < a.length; i++) {
			out[i] = (byte) (a[i] ^ key[i % key.length]);
		}
		return out;
	}

	/*
	 * Function to perform Base64 encoding on the XORed byte string
	 */
	private static String base64Encode(byte[] bytes) {
		return Base64.getEncoder().encodeToString(bytes).replaceAll("\\s", "");

	}

}
