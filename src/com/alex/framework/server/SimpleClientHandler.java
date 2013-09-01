package com.alex.framework.server;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import com.alex.framework.Message;
import com.alex.framework.server.registrar.ClientRegistrar;
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
			adapter.Start(this, groupName);
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
	
	@Override
	public Collection<String> getGroups() {
		return groups.keySet();
	}
	
	@Override
	public GroupAdapter getGroup(String groupName) {
		return groups.get(groupName);
	}

	@Override
	public void leaveGroup(String groupName) {
		// leave the group with the specified name.
		// If we're not a part of any groups, destroy this object. 
		if ( groups.containsKey(groupName) ) {
			groups.remove(groupName);
		}
		
		if ( groups.size() == 0 ) {
			this.destroy();
		}
	}

	@Override
	public void destroy() {
		// Remove all references to this object?
		ClientRegistrar.get().removeClient(this);
	}

	@Override
	public void checkLeases() {
		// For every group we are in, check the lease of it.
		Collection<GroupAdapter> preGroups = groups.values();
		for ( GroupAdapter group : preGroups ) {
			long t = System.currentTimeMillis();
			if ( group.getLeaseExpiryTime() < t ) {
				group.destroy();
			}
		}
	}
	
	

}
