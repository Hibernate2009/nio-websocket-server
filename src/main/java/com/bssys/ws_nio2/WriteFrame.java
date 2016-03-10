package com.bssys.ws_nio2;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class WriteFrame {
	/**
	 * 
	 * Write a A single-frame unmasked text message
	 * 
	 * @param os
	 * @param strText
	 * @throws IOException
	 */
	public static byte[] doWrite(String strText) throws IOException {

		byte[] textBytes = strText.getBytes("UTF-8");
		ByteArrayOutputStream bao = new ByteArrayOutputStream();

		/* Add start of Frame */
		bao.write(0x81);
		bao.write((byte) textBytes.length);
		bao.write(textBytes);
		bao.flush();
		bao.close();
		return bao.toByteArray();
	}
}