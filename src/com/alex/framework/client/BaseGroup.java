package com.alex.framework.client;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import com.alex.framework.Message;

/**
 * Base Group is the main way of using this messaging framework.
 * Developers should extend this class and overwrite the onMessageReceived method
 * in order to receive updates when messages are received and 
 * @author Alex
 *
 */
public abstract class BaseGroup implements MessageHandler {
	/**
	 * The server handler is the object responsible for all
	 * communication with the server.
	 */
	protected ServerHandler serverHandler;
	
	protected String groupName;

	public BaseGroup( String groupName ) {
		this.groupName = groupName;
	}
	
	public void connect() throws UnknownHostException {
		// TODO: Get this from a properties object?
		// Use the basic java address structure if possible.
		//serverHandler = new TestClient(new InetSocketAddress("ec2-54-252-187-83.ap-southeast-2.compute.amazonaws.com",50512));
		serverHandler = new TestClient(new InetSocketAddress(InetAddress.getByName("192.168.1.108"),50512));
		
		serverHandler.SetMessageHandler(this);
		
		serverHandler.Register();
		
		serverHandler.JoinGroup(groupName);
	}

	@Override
	public final void messageReceived(Message newMessage) {
		onMessageReceived(newMessage);
	}
	
	public void sendMessage( String message ) {
		serverHandler.Post(message, groupName);
	}
	
	/**
	 * This method will be called whenever a new message arrives. 
	 * @param msg
	 */
	public abstract void onMessageReceived(Message msg);
	
}
