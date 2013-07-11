package com.alex.framework.server;

import java.net.Socket;

/**
 * A MessageHandler is a class which processes incoming messages for the server. 
 * @author Alex
 *
 */
public interface MessageHandler {
	public void parse( Socket clientSocket );
}
