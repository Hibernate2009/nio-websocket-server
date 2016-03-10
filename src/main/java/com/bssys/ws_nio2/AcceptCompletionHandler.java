package com.bssys.ws_nio2;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AcceptCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, Void> {

	private AsynchronousServerSocketChannel listener;
	private WebSocketServer server;

	public AcceptCompletionHandler(AsynchronousServerSocketChannel listener, WebSocketServer server) {
		this.listener = listener;
		this.server = server;
	}

	public void completed(AsynchronousSocketChannel socketChannel, Void arg1) {
		System.out.println("client connected: " + socketChannel);

		listener.accept(null, this);

		ByteBuffer inputBuffer = ByteBuffer.allocate(2048);
		HandShakeReadCompletionHandler readCompletionHandler = new HandShakeReadCompletionHandler(socketChannel,
				inputBuffer, server);
		SessionState sessionState = new SessionState();
		socketChannel.read(inputBuffer, sessionState, readCompletionHandler);
	}

	public void failed(Throwable arg0, Void arg1) {
	}
}
