package com.alex.framework.server;

import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ParallelMessageHandler implements MessageHandler, Runnable {

	/**
	 * The queue containing incoming connections which must be processed
	 */
	private static BlockingQueue<Socket> incomingConnections;
	
	private static LinkedList<ParallelMessageHandler> workers;
	
	private MessageHandler delegateHandler;
	
	public ParallelMessageHandler() {
		
		// if the incoming connections array already exists, then we are being created as a worker thread
		
		// Only ran by the first thread.
		if ( incomingConnections == null ) {
			incomingConnections = new LinkedBlockingQueue<Socket>();
			workers = new LinkedList<ParallelMessageHandler>();
			
			// create the worker threads.
			for ( int i=1; i < 4; i++ ) {
				ParallelMessageHandler h = new ParallelMessageHandler();
			}
		}
		
		delegateHandler = new SimpleMessageHandler();
		
		workers.add(this);
		Thread t = new Thread(this);
		t.start();
	}
	
	@Override
	public void run() {
		while ( true ) {
			try {
				Socket s = incomingConnections.take();
				
				delegateHandler.parse(s);
				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void parse(Socket clientSocket) {
		// Queue the connection up so that people can use it.
		
		try {
			incomingConnections.put(clientSocket);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
