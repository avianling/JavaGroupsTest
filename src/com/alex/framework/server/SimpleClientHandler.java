package com.alex.framework.server;

import java.util.Map;
import java.util.TreeMap;

import com.alex.framework.Message;
import com.alex.logging.Logger;

public class SimpleClientHandler implements ClientHandler{

	// A treemap containing all of the groups this client is a part of.
	private Map<String, GroupAdapter> groups;
	
	public SimpleClientHandler() {
		groups = new TreeMap<String, GroupAdapter>();
	}
	
	@Override
	public void sendMessageToClient(Message msg) {
		Logger.Log("Server: Sending a message to the client: '" + msg.Serialize() + "'");
	}

	@Override
	public void messageReceivedFromGroup(Message msg) {
		sendMessageToClient(msg);
	}

	@Override
	public void joinGroup(String groupName) {
		try {
			GroupAdapter adapter = new JGroupAdapter();
			adapter.setClientHandler(this);
			adapter.joinGroup(groupName);
			groups.put(groupName, adapter);
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendMessageToGroup(Message msg) {
		// Lookup the group
		GroupAdapter groupAdapter = groups.get(msg.Headers.get("groupName"));
		
		try {
			groupAdapter.postMessage(msg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
