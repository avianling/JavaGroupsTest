package com.alex.framework.server;

import org.jgroups.Address;
import org.jgroups.Header;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.stack.IpAddress;

import com.alex.logging.Logger;

/**
 * JGroupAdapter is an implementation of a GroupAdapter for working with javagroups.
 * @author Alex
 *
 */
public class JGroupAdapter extends ReceiverAdapter implements GroupAdapter {

	private JChannel channel;
	
	private ClientHandler clientHandler;
	
	public JGroupAdapter() throws Exception {
		channel = new JChannel();
		channel.setReceiver(this);
	}
	
	@Override
	public void joinGroup(String groupName) throws Exception {
		channel.connect(groupName);
	}

	@Override
	public void postMessage(com.alex.framework.Message msg) throws Exception {
		// Convert the message into a JGroups message and then post it.
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
	public void setClientHandler(ClientHandler handler) {
		clientHandler = handler;
	}

}
