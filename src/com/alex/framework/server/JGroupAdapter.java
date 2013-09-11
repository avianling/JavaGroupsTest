package com.alex.framework.server;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.jgroups.Address;
import org.jgroups.Header;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.stack.IpAddress;

import com.alex.framework.server.registrar.GroupRegistrar;
import com.alex.logging.Logger;

/**
 * JGroupAdapter is an implementation of a GroupAdapter for working with javagroups.
 * @author Alex
 *
 */
public class JGroupAdapter extends ReceiverAdapter implements GroupAdapter {

	private JChannel channel;
	
	private long leaseExpiryTime;
	
	private ClientHandler clientHandler;
	
	/*
	 * Contains the messages which we are waiting to send to the client when they connect.
	 * Should be capped at some number to prevent extreme memory comsumption.
	 */
	private List<com.alex.framework.Message> messageQueue;
	
	// This is the sequence number of the last message we sent to our client.
	// Should be used when we request updates from the group message store ( called group ).
	private int sequenceNumberOfLastMessageToClient;
	
	private String groupName;
	
	public JGroupAdapter() throws Exception {
		channel = new JChannel();
		channel.setReceiver(this);
		
		messageQueue = new LinkedList<com.alex.framework.Message>();
		
		updateLease();
	}
	
	private void updateLease() {
		leaseExpiryTime = System.currentTimeMillis() + LeaseController.leaseTime;
	}
	
	

	@Override
	public void postMessage(com.alex.framework.Message msg) throws Exception {
		// Convert the message into a JGroups message and then post it.
		updateLease();
		Logger.Log("Server: GroupAdapter is posting a message to the group");
		org.jgroups.Message message = new org.jgroups.Message(null, null, msg.Payload);
		channel.send(message);
	}
	
	/**
	 * Called whenever this groupAdapter gets a message from the javagroup its listening to.
	 */
	public void receive(Message msg) { 
		Logger.Log("Server: GroupAdapter got a message from the group. Sending it to the client handler.");
		
		com.alex.framework.Message message = new com.alex.framework.Message();
		message.Payload = msg.getObject().toString();
		if ( msg.dest() != null ) {
			message.Headers.put("dest", msg.dest().toString());
		}
		message.Headers.put("src", msg.getSrc().toString());
		
		// put the message in the message queue.
		synchronized ( messageQueue ) {
			messageQueue.add(message);
		}
		
		clientHandler.messageReceivedFromGroup(message);
	}

	@Override
	public void Start(ClientHandler client, String groupName) throws Exception {
		this.groupName = groupName;
		clientHandler = client;
		
		updateLease();
		Group group = GroupRegistrar.getGroup(groupName);
		sequenceNumberOfLastMessageToClient = group.getSequenceNumber();
		
		channel.connect(groupName);
	}

	@Override
	public List<com.alex.framework.Message> getUpdates() {
		updateLease();
		synchronized( messageQueue ) {
			List l = messageQueue;
			messageQueue = new LinkedList<com.alex.framework.Message>();
			return l;
		}
	}

	@Override
	public long getLeaseExpiryTime() {
		return leaseExpiryTime;
	}

	@Override
	public void destroy() {
		// Deregister us from JavaGroups
		channel.close();
		channel = null;
		
		// Deregister us from GCM
		// TODO:lars
		
		clientHandler.leaveGroup(this.groupName);
	}

}
