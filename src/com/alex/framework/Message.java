package com.alex.framework;

import java.util.Map;
import java.util.TreeMap;

import com.alex.json.JSON;

/**
 * Base class for messages.
 * A Message is an atomic piece of data which will be sent from client to client.
 * @author abor036
 *
 */
public class Message {
	
	public Message() {
		Headers = new TreeMap< String, String >();
	}
	
	public Map< String, String > Headers;
	public String Payload;
	
	public String Serialize() {
		return JSON.toJson(this);
	}
}
