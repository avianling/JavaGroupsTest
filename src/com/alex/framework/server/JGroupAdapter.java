package com.alex.framework.server;

import java.util.ArrayList;
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
	
	// This is the sequence number of the last message we sent to our client.
	// Should be used when we request updates from the group message store ( called group ).
	private int sequenceNumberOfLastMessageToClient;
	
	private String groupName;
	
	public JGroupAdapter() throws Exception {
		channel = new JChannel();
		channel.setReceiver(this);
		
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
		try {
			List<com.alex.framework.Message> messages = GroupRegistrar.getGroup(groupName).getMessagesSince(0);
			sequenceNumberOfLastMessageToClient = GroupRegistrar.getGroup(groupName).getSequenceNumber();
			return messages;
		} catch ( Exception e ) {
			e.printStackTrace();
			return null;
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
