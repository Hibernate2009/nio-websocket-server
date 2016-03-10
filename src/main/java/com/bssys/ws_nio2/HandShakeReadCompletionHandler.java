package com.bssys.ws_nio2;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class HandShakeReadCompletionHandler implements CompletionHandler<Integer, SessionState> {

	private AsynchronousSocketChannel socketChannel;
	private ByteBuffer inputBuffer;
	private WebSocketServer server;

	public HandShakeReadCompletionHandler(AsynchronousSocketChannel socketChannel, ByteBuffer inputBuffer,
			WebSocketServer server) {
		this.socketChannel = socketChannel;
		this.inputBuffer = inputBuffer;
		this.server = server;
	}

	public void completed(Integer bytesRead, SessionState sessionState) {

		byte[] buffer = new byte[bytesRead];
		inputBuffer.rewind();

		inputBuffer.get(buffer);
		String message = new String(buffer);

		System.out.println("Received message from client : " + message);

		HttpRequest httpRequest = new HttpRequest(message);
		Map<String, String> headers = httpRequest.getHeaders();

		inputBuffer.clear();

		String websocketKey = headers.get("Sec-WebSocket-Key").trim();
		try {
			String websocketAcceptKey = KeyGeneration.getKey(websocketKey);
			String response = String.format("HTTP/1.1 101 Switching Protocols\r\n" + "Upgrade: websocket\r\n"
					+ "Connection: Upgrade\r\n" + "Sec-WebSocket-Accept: %s\r\n\r\n", websocketAcceptKey);
			ByteBuffer wrap = ByteBuffer.wrap(response.getBytes());
			HandShakeWriteCompletionHandler handShakeWriteCompletionHandler = new HandShakeWriteCompletionHandler(
					socketChannel, server);
			socketChannel.write(wrap, sessionState, handShakeWriteCompletionHandler);

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public void failed(Throwable exc, SessionState sessionState) {
	}
}