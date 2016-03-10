package com.bssys.ws_nio2;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class HandShakeWriteCompletionHandler implements CompletionHandler<Integer, SessionState> {

	private AsynchronousSocketChannel socketChannel;
	private WebSocketServer server;

	public HandShakeWriteCompletionHandler(AsynchronousSocketChannel socketChannel, WebSocketServer server) {
		this.socketChannel = socketChannel;
		this.server = server;
	}

	public void completed(Integer result, SessionState sessionState) {
		server.addClient(socketChannel);
		ByteBuffer inputBuffer = ByteBuffer.allocate(1024);
		socketChannel.read(inputBuffer, sessionState, new ReadCompletionHandler(socketChannel, inputBuffer, server));
	}

	public void failed(Throwable exc, SessionState sessionState) {
	}
}