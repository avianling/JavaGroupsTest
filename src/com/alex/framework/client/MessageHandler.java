package com.alex.framework.client;

import com.alex.framework.Message;

/**
 * A Message Handler is a class which responds to messages.
 * A Message handler should invoke events in an eventHander object.
 * custom clients should aim to extend eventHandler to prevent the loss of system functions. 
 * @author Alex
 *
 */
public interface MessageHandler {
	public void messageReceived( Message newMessage );
}
