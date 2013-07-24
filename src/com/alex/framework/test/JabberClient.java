package com.alex.framework.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.UnknownHostException;

import com.alex.framework.Message;
import com.alex.framework.client.MessageHandler;
import com.alex.framework.client.ServerHandler;
import com.alex.logging.Logger;
import com.alex.logging.TimingRecord;

/**
 * A class to endlessly populate the server with random messages.
 * @author Alex
 *
 */
public class JabberClient implements MessageHandler {

	@Override
	public void onMessageReceived(Message newMessage) {
		// do nothing, we don't care about the response.
	}
	
	public JabberClient( int rate, String message, String group ) throws InterruptedException, UnknownHostException {
		ServerHandler handler = new com.alex.framework.client.TestClient("ec2-54-252-187-83.ap-southeast-2.compute.amazonaws.com",50512);
		handler.SetMessageHandler( new TestClient() );
		
		Logger.LogToConsole = false;
		
		handler.Register();
		
		handler.JoinGroup(group);
		
		while (true){
			Thread.sleep(rate);
			
			handler.Post(message, group);
		}
	
	}
	
	public static void main( String[] args ) throws UnknownHostException, InterruptedException {
		JabberClient client = new JabberClient(100, "something", "testGroup");
	}
	
	

}
