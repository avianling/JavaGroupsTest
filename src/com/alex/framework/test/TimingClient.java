package com.alex.framework.test;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;

import com.alex.framework.Message;
import com.alex.framework.client.AsyncGroup;
import com.alex.logging.Logger;

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
		msgSendTimes.put(msgCount+"", System.nanoTime());
		sendMessage(msgCount+"");
		msgCount++;
	}
	
	@Override
	public void onMessageReceived(Message msg) {
		// Get the time when the message was sent.
		if ( msgSendTimes.containsKey(""+msg.Payload)) {
			long startTime = msgSendTimes.get((String)msg.Payload);
			long elapsedTime = System.nanoTime() - startTime;
			msgSendTimes.remove((String)msg.Payload);
			msgTimes.add(elapsedTime);
			System.out.println(elapsedTime / 1000000000f);
		}
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
