package com.alex.framework;

import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import com.alex.json.JSON;
import com.alex.logging.TimingRecord;

/**
 * Base class for messages.
 * A Message is an atomic piece of data which will be sent from client to client.
 * @author abor036
 *
 */
public class Message {
	
	public Message() {
		Headers = new TreeMap< String, String >();
		
		Headers.put("uuid", UUID.randomUUID().toString());
		TimingRecord r = new TimingRecord(Headers.get("uuid"));
		r.startTime = System.nanoTime();
	}
	
	public Map< String, String > Headers;
	public String Payload;
	
	public String Serialize() {
		TimingRecord r = TimingRecord.records.get(Headers.get("uuid"));
		if (r!=null) {
		r.serializationStart = System.nanoTime();
		}
		String s = JSON.toJson(this);
		if ( r!=null )
			r.serializationFinished = System.nanoTime();
		return s;
	}
	
	public void setHeader( String header, String value ) {
		Headers.put(header, value);
	}
}
