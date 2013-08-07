package com.alex.framework.client;

import java.net.UnknownHostException;

import com.alex.framework.Message;

public abstract class BaseGroup implements MessageHandler {
	/**
	 * The server handler is the object responsible for all
	 * communication with the server.
	 */
	private ServerHandler serverHandler;
	
	private String groupName;
	
	public BaseGroup( String groupName ) throws UnknownHostException {
		// TODO: Get this from a properties object?
		// Use the basic java address structure if possible.
		serverHandler = new TestClient("ec2-54-252-187-83.ap-southeast-2.compute.amazonaws.com",50512);
		
		serverHandler.SetMessageHandler(this);
		
		serverHandler.Register();
		
		serverHandler.JoinGroup(groupName);
	}

	@Override
	public final void messageReceived(Message newMessage) {
		onMessageReceived(newMessage);
	}
	
	public final void sendMessage( String message ) {
		serverHandler.Post(message, groupName);
	}
	
	/**
	 * This method will be called whenever a new message arrives. 
	 * @param msg
	 */
	public abstract void onMessageReceived(Message msg);
	
}
