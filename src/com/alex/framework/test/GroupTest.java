package com.alex.framework.test;

import java.net.UnknownHostException;

import com.alex.framework.Message;
import com.alex.framework.client.AsyncGroup;
import com.alex.framework.client.MessageHandler;

public class GroupTest extends AsyncGroup {

	public GroupTest(String groupName) throws UnknownHostException {
		super(groupName);
	}

	@Override
	public void onMessageReceived(Message msg) {
		
	}

}
