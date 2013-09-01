package com.alex.framework.server;

import java.util.Collection;

import com.alex.framework.Message;


/**
 * A Client Handler is in charge of dealing with messages from a client
 * and with transporting messages to a client.
 * In practical terms, that means it contains a way of sending messages
 * back to the client, likely GCM, and contains 
 * @author Alex
 *
 */
public interface ClientHandler {
	/**
	 * Send a message to the client.
	 * @param msg
	 */
	public void sendMessageToClient( Message msg );
	
	/**
	 * Called when a message is received from a javagroup.
	 * @param msg
	 */
	public void messageReceivedFromGroup( Message msg );
	
	/**
	 * A function to join a named group.
	 * This will create a GroupAdapter object which will notify this object when a message is sent.
	 * What exceptions should this throw?
	 * @param groupName
	 */
	public void joinGroup( String groupName );
	
	/**
	 * Send the provided message to the group named in the message header groupName.
	 * @param msg
	 */
	public void sendMessageToGroup( Message msg );
	
	/**
	 * Get the names of each group of which this client is a member.
	 * @return
	 */
	public Collection<String> getGroups();
	
	/**
	 * A method to get the group adapter for a group with a given name.
	 * @param groupName
	 * @return
	 */
	public GroupAdapter getGroup( String groupName );
	
	
	public void leaveGroup( String groupName );
	
	
	public void destroy();
	
	
	public void checkLeases();
}
