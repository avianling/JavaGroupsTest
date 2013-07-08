package com.alex.framework.client;

import com.alex.framework.Message;

public interface MessageHandler {
	public void onMessageReceived( Message newMessage );
}
