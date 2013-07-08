package com.alex.framework.client;

import com.alex.framework.Message;

/**
 * Client objects to handle connection & communication with the server.
 * Contains options to send messages & set retrival options.
 * @author Alex
 *
 */
public interface Client {
	public void Connect();
	
	public void SendMessage( Message msg );
	
	public void SetMessageHandler( MessageHandler msgHandler );
	
	public void Close();
	
}
