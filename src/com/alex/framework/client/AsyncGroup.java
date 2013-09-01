package com.alex.framework.client;

import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.alex.framework.Message;

public abstract class AsyncGroup extends BaseGroup implements Runnable {

	private BlockingQueue<String> outgoingQueue;
	
	public AsyncGroup(String groupName) throws UnknownHostException {
		super(groupName);
		outgoingQueue = new LinkedBlockingQueue<String>();
		
		new Thread(this).start();
	}
	
	public void sendMessage( String message ) {
		synchronized(this) {
			if ( outgoingQueue.peek() == null ) {
				outgoingQueue.add(message);
				notify();
			} else {
				outgoingQueue.add(message);
			}
		}
	}

	@Override
	public void run() {
		try {
			this.connect();
			
			while(true) {
				synchronized (this) {
					String m = outgoingQueue.poll();
					if ( m != null ) {
						this.serverHandler.Post(m, groupName);
					} else {
						this.wait();
					}
				}
			}
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
