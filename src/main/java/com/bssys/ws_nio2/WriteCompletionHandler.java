package com.bssys.ws_nio2;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class WriteCompletionHandler implements CompletionHandler<Integer, SessionState> {
	
	public WriteCompletionHandler() {
		
	}

	public void completed(Integer result, SessionState attachment) {
		
	}

	public void failed(Throwable exc, SessionState attachment) {
		// TODO Auto-generated method stub
		
	}

}
