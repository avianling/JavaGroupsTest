package com.alex.logging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/** 
 * Assumes that each message has a unique uuid header.
 * this is used to identify the message for timing issues.
 * @author Alex
 *
 */
public class TimingRecord {

	public static Map<String,TimingRecord> records;
	
	public TimingRecord( String msg ) {
		
		connectionTime = 0;
		serializationTime = 0;
		sendingTime = 0;
		waitingForResponseTime = 0;
		downloadingTime = 0;
		
		if ( records == null ) {
			records = new HashMap<String,TimingRecord>();
		}
		
		records.put(msg, this);
	}
	
	
	// The times we are going to record
	public long startTime;
	public long connectionInit; // when we start trying to connect
	public long connectionMade; // when we actually connect to the server
	public long serializationFinished;
	public long serializationStart;
	public long sendingStart;
	public long sendingFinish;
	public long receivingStart;
	public long responseEnd;
	
	public long connectionTime;
	public long serializationTime;
	public long sendingTime;
	public long waitingForResponseTime;
	public long downloadingTime;
	
	public String print() {
		//return "" + connectionTime + "	" + serializationTime + "	" + sendingTime + "	" + waitingForResponseTime + "	" + downloadingTime;
		return "" + ( connectionTime + serializationTime + sendingTime + waitingForResponseTime + downloadingTime);
		//return startTime + "	" + connectionInit + "	" + connectionMade + "	" + serializationStart + "	" + serializationFinished + "	" + sendingStart + "	" + sendingFinish + "	" + receivingStart + "	" + responseEnd;
	}
};
