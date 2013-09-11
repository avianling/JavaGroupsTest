package com.alex.framework.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import com.alex.framework.Message;
import com.alex.framework.client.MessageHandler;
import com.alex.framework.client.ServerHandler;
import com.alex.logging.Logger;
import com.alex.logging.TimingRecord;

/**
 * A class to endlessly populate the server with random messages.
 * @author Alex
 *
 */
public class JabberClient implements Runnable {
	
	
	public static void main( String[] args ) throws InterruptedException {
		
		Logger.LogToConsole = true;
		
		for ( int i=0; i < 60; i++ ) {
			JabberClient j = new JabberClient();
			Thread t = new Thread(j);
			t.start();
			Thread.sleep(100);
		}
	}

	@Override
	public void run() {
		try {
			GroupTest g = new GroupTest("jabberGroup");
			
			
			
			while ( true ) {
				g.sendMessage("jabber client says no");
				
				Thread.sleep(15000);
			}
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	
	}
	
}
