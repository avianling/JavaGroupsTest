package com.alex.framework.server;

import com.alex.framework.Message;

/**
 * A GroupAdapter handles communication to and from a particular group.
 * A group adapter contains methods for posting messages to a group,
 * and for receiving messages from that group.
 * @author Alex
 *
 */
public interface GroupAdapter {
	//TODO: Implement proper exceptions for these methods.
	
	/**
	 * A method to join a particular group.
	 * @param groupName
	 * @throws Exception if there is an issue joining that particular group. 
	 */
	public void joinGroup( String groupName ) throws Exception;
	
	/**
	 * A method to post a given message to this group.
	 * @param msg
	 * @throws Exception if there is an issue sending a message.
	 */
	public void postMessage( Message msg ) throws Exception;
	
	/**
	 * Set which client handler will be used to process outgoing messages meant for this client.
	 * @param handler
	 */
	public void setClientHandler( ClientHandler handler );
}
