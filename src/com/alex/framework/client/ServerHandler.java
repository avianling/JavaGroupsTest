package com.alex.framework.client;

import com.alex.framework.Message;

/**
 * Client objects to handle connection & communication with the server.
 * Contains options to send messages & set retrival options.
 * Handles communication with the server.
 * @author Alex
 *
 */
public interface ServerHandler {
	public void Connect();
	
	/**
	 * A method to register this client with the server.
	 * This must be called before any other communications are attempted.
	 */
	public void Register();
	
	/**
	 * A method to join a given group.
	 * Will fail if we are not already registered.
	 * @param groupName
	 */
	public void JoinGroup( String groupName );
	
	public void SetMessageHandler( MessageHandler msgHandler );
	
	public void Close();
	
	/**
	 * Post the given message to the server.
	 * @param message
	 */
	public void Post(String message, String group);
}
