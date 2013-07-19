package com.alex.framework.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.alex.framework.Message;
import com.alex.framework.client.MessageHandler;
import com.alex.framework.client.ServerHandler;
import com.alex.logging.Logger;

public class TestClient implements MessageHandler {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// create a test client which gets text input from stdin & sends it to the server.
		ServerHandler handler = new com.alex.framework.client.TestClient("ec2-54-252-187-83.ap-southeast-2.compute.amazonaws.com",50512);
		handler.SetMessageHandler( new TestClient() );
		
		Logger.LogToConsole = false;
		
		handler.Register();
		
		handler.JoinGroup("testGroup");
		
		handler.GetUpdates();
		
		BufferedReader inputReader = new BufferedReader( new InputStreamReader( System.in ) );
		
		while(true) {
			String input = inputReader.readLine();
			handler.Post(input, "testGroup");
			handler.GetUpdates();
		}
	}

	@Override
	public void onMessageReceived(Message newMessage) {
		System.out.println(">>" + newMessage.Payload);
	}

}
