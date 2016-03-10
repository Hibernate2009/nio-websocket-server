package com.bssys.ws_nio2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebSocketServer {

	private final List<AsynchronousSocketChannel> connections = Collections
			.synchronizedList(new ArrayList<AsynchronousSocketChannel>());

	public void run() throws IOException {
		ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(10);
		AsynchronousChannelGroup group = AsynchronousChannelGroup.withThreadPool(newFixedThreadPool);

		final AsynchronousServerSocketChannel listener = AsynchronousServerSocketChannel.open(group);
		InetSocketAddress address = new InetSocketAddress("localhost", 3333);
		listener.bind(address);

		AcceptCompletionHandler acceptCompletionHandler = new AcceptCompletionHandler(listener, this);

		listener.accept(null, acceptCompletionHandler);
	}

	public void addClient(AsynchronousSocketChannel client) {
		connections.add(client);
	}

	public void removeClient(AsynchronousSocketChannel channel) {
		connections.remove(channel);
	}

	public void writeMessageToClients(AsynchronousSocketChannel channel, String message) {
		synchronized (connections) {
			for (AsynchronousSocketChannel clientConnection : connections) {
				if (clientConnection != channel) {
					try {
						byte[] doWrite = WriteFrame.doWrite(message);
						ByteBuffer outputBuffer = ByteBuffer.wrap(doWrite);
						SessionState sessionState = new SessionState();
						clientConnection.write(outputBuffer, sessionState, new WriteCompletionHandler());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	public static void main(String[] args) throws IOException {
		System.out.println("WebSocket server start...");
		new WebSocketServer().run();
		try {
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
