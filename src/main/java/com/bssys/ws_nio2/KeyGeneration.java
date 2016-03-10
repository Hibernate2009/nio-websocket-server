package com.bssys.ws_nio2;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class KeyGeneration {
	
	private final static String MAGIC_KEY = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";

	public static String getKey(String strWebSocketKey) throws NoSuchAlgorithmException {

		strWebSocketKey += MAGIC_KEY;

		MessageDigest shaMD = MessageDigest.getInstance("SHA-1");
		shaMD.reset();
		shaMD.update(strWebSocketKey.getBytes());
		byte messageDigest[] = shaMD.digest();

		return Base64.getEncoder().encodeToString(messageDigest);

	}
}