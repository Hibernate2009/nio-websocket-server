package com.bssys.ws_nio2;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class ReadCompletionHandler implements CompletionHandler<Integer, SessionState> {

	private AsynchronousSocketChannel channel;
	private ByteBuffer inputBuffer;
	private WebSocketServer server;

	public ReadCompletionHandler(AsynchronousSocketChannel channel, ByteBuffer inputBuffer, WebSocketServer server) {
		this.channel = channel;
		this.inputBuffer = inputBuffer;
		this.server = server;
	}

	public void completed(Integer bytesRead, SessionState session) {
		if (bytesRead < 1) {
			System.out.println("Closing connection to " + channel);
			server.removeClient(channel);
		} else {

			byte[] buffer = new byte[bytesRead];
			inputBuffer.rewind();
			inputBuffer.get(buffer);

			byte[] decodeFrame = RawParse.parse(buffer).payload;
			String message = new String(decodeFrame);

			System.out.println("Received message from " + ":" + message);
			inputBuffer.clear();

			channel.read(inputBuffer, session, this);

			server.writeMessageToClients(channel, message);
		}
	}

	public void failed(Throwable exc, SessionState attachment) {
		server.removeClient(channel);
	}
}
