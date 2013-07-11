package com.alex.framework.server;

/**
 * base interface for all servers.
 * Servers have three roles:
 * 1 - Host JavaGroups proccesses.
 * 2 - Retrieve Client connections & send these to JavaGroups.
 * 3 - Retrieve messages from JavaGroups & forward these to the appropriate Client over GCM.
 * @author Alex
 *
 */
public interface Server {
	/**
	 * Start the server.
	 */
	public void Start();
	
	public void SetMessageHandler( MessageHandler msgHandler );
}
