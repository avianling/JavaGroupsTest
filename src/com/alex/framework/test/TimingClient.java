package com.alex.framework.test;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.alex.framework.Message;
import com.alex.framework.client.AsyncGroup;
import com.alex.logging.Logger;
import com.alex.logging.TimingRecord;

public class TimingClient extends AsyncGroup {

	private HashMap<String,Long> msgSendTimes;
	
	private int msgCount;
	
	public LinkedList<Long> msgTimes;
	
	public TimingClient(String groupName) throws UnknownHostException {
		super(groupName);
		
		msgSendTimes = new HashMap<String,Long>();
		msgTimes = new LinkedList<Long>();
		msgCount = 0;
	}

	public void sendTimedMessage() {
		sendMessage(msgCount+"");
	}
	
	@Override
	public void onMessageReceived(Message msg) {
		// Get the time when the message was sent.
		/*if ( msgSendTimes.containsKey(""+msg.Payload)) {
			long startTime = msgSendTimes.get((String)msg.Payload);
			long elapsedTime = System.nanoTime() - startTime;
			msgSendTimes.remove((String)msg.Payload);
			msgTimes.add(elapsedTime);
			System.out.println(elapsedTime / 1000000000f);
		}*/
		
		// if the message is one which we have been collecting timing information,
		// display that timing information.
		for (Map.Entry<String, com.alex.logging.TimingRecord> entry : TimingRecord.records.entrySet())
		{
			if ( entry.getValue().serializationTime != 0 ) {
				System.out.println(entry.getValue().print());
			}
		}
		System.out.println("----------");
	}

	
	public static void main( String[] args ) throws UnknownHostException, InterruptedException {
		TimingClient t = new TimingClient("timingGroup");
		
		Logger.LogToConsole=false;
		
		while(true) {
			t.sendTimedMessage();
			Thread.sleep(2000);
		}
	}
}
