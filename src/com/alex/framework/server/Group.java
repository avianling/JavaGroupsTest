package com.alex.framework.server;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import org.jgroups.JChannel;
import org.jgroups.ReceiverAdapter;

import com.alex.framework.Message;
import com.alex.logging.Logger;

/**
 * This class represents a group and contains methods to access the message history of the group.
 * This gets messages from 
 * @author Alex
 *
 */
public class Group extends ReceiverAdapter {
	public String groupName;
	
	private JChannel channel;
	
	public Group( String groupName ) throws Exception {
		this.groupName = groupName;
		messageBacklog = new ArrayList<Message>();
		
		channel = new JChannel();
		channel.setReceiver(this);
		channel.connect(groupName);
	}
	
	// This represents all of the messages within the group. 
	// Note - this is currently not very efficient: messages should not be stored in memory,
	// but should be loaded from a database? ( Database access should be cached, of course )
	private ArrayList<Message> messageBacklog;
	
	/**
	 * This method is used to get the sequence number of the most recently posted update.
	 * 
	 * @return
	 */
	public synchronized int getSequenceNumber() {
		return messageBacklog.size();
	}
	
	public List<Message> getMessagesSince( int messageStartingPoint ) {
		List<Message> sublist;
		
		synchronized(this) {
			sublist =  messageBacklog.subList(messageStartingPoint, getSequenceNumber());
		}
			
		return sublist;
	}
	
	
	/**
	 * Called whenever this groupAdapter gets a message from the javagroup its listening to.
	 */
	public void receive(org.jgroups.Message msg) { 
		//Logger.Log("Server: GroupStore received an update. Saving it.");
		com.alex.framework.Message message = new com.alex.framework.Message();
		message.Payload = msg.getObject().toString();
		if ( msg.dest() != null ) {
			message.Headers.put("dest", msg.dest().toString());
		}
		message.Headers.put("src", msg.getSrc().toString());
		
		synchronized (this) {
			messageBacklog.add(message);
		}
		
	}
}
