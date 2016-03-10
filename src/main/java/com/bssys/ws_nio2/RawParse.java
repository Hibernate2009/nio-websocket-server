package com.bssys.ws_nio2;

import java.nio.ByteBuffer;

public class RawParse {
	public static class Frame {
		byte opcode;
		boolean fin;
		byte payload[];
	}

	public static Frame parse(byte raw[]) {
		// easier to do this via ByteBuffer
		ByteBuffer buf = ByteBuffer.wrap(raw);

		// Fin + RSV + OpCode byte
		Frame frame = new Frame();
		byte b = buf.get();
		frame.fin = ((b & 0x80) != 0);
		boolean rsv1 = ((b & 0x40) != 0);
		boolean rsv2 = ((b & 0x20) != 0);
		boolean rsv3 = ((b & 0x10) != 0);
		frame.opcode = (byte) (b & 0x0F);

		// TODO: add control frame fin validation here
		// TODO: add frame RSV validation here

		// Masked + Payload Length
		b = buf.get();
		boolean masked = ((b & 0x80) != 0);
		int payloadLength = (byte) (0x7F & b);
		int byteCount = 0;
		if (payloadLength == 0x7F) {
			// 8 byte extended payload length
			byteCount = 8;
		} else if (payloadLength == 0x7E) {
			// 2 bytes extended payload length
			byteCount = 2;
		}

		// Decode Payload Length
		while (--byteCount > 0) {
			b = buf.get();
			payloadLength |= (b & 0xFF) << (8 * byteCount);
		}

		// TODO: add control frame payload length validation here

		byte maskingKey[] = null;
		if (masked) {
			// Masking Key
			maskingKey = new byte[4];
			buf.get(maskingKey, 0, 4);
		}

		// TODO: add masked + maskingkey validation here

		// Payload itself
		frame.payload = new byte[payloadLength];
		buf.get(frame.payload, 0, payloadLength);

		// Demask (if needed)
		if (masked) {
			for (int i = 0; i < frame.payload.length; i++) {
				frame.payload[i] ^= maskingKey[i % 4];
			}
		}

		return frame;
	}
}
