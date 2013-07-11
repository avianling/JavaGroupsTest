package com.alex.framework.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.alex.json.JSON;

public class SimpleServer implements Server, Runnable {

	@Override
	public void Start() {
		// start the utility class used for json parsing.
		// only needs to be called once per program.
		JSON json = new JSON();
		
		//threadManager = Executors.newCachedThreadPool();
		
		SetMessageHandler( new SimpleMessageHandler() );
		
		new Thread(this).start();
	}
	
	//private ExecutorService threadManager;
	
	private MessageHandler msgHandler;
	
	@Override
	public void SetMessageHandler( MessageHandler msgHandler ) {
		this.msgHandler = msgHandler;
	}
	
	

	@Override
	public void run() {
		// Create a socket and listen for incoming connections. 
		try {
			ServerSocket listener = new ServerSocket();
			listener.bind( new InetSocketAddress( InetAddress.getLocalHost(), 50512 ));	
			
			while ( true ) {
				Socket s = listener.accept();
				
				System.out.println("A client connected");
				
				msgHandler.parse(s);
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	
}
