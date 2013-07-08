package com.alex.framework;

import java.util.Map;

/**
 * Base class for messages.
 * A Message is an atomic piece of data which will be sent from client to client.
 * @author abor036
 *
 */
public class Message {
	public Map< String, String > Headers;
	public String Payload; 
}
