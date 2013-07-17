package com.alex.framework.server;

import java.util.ArrayList;
import java.util.List;

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
	 * A method to create the group adapter.
	 * The constructor is not used for this as using the perscribed interface allows for the swapping
	 * of different concrete group adapters and thus increases modifiability.
	 * @param client
	 * @param groupName
	 */
	public void Start( ClientHandler client, String groupName ) throws Exception;
	
	/**
	 * A method to post a given message to this group.
	 * @param msg
	 * @throws Exception if there is an issue sending a message.
	 */
	public void postMessage( Message msg ) throws Exception;
	
	/**
	 * This method will check for any updates and send them back to the client.
	 */
	public List<com.alex.framework.Message> getUpdates();
}
