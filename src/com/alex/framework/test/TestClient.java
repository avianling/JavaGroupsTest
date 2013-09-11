package com.alex.framework.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;

import com.alex.framework.Message;
import com.alex.framework.client.MessageHandler;
import com.alex.framework.client.ServerHandler;
import com.alex.logging.Logger;
import com.alex.logging.TimingRecord;

public class TestClient {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		// create a test client which gets text input from stdin & sends it to the server.
		GroupTest g = new GroupTest("testGroup");
		Logger.LogToConsole = false;
		//BufferedReader inputReader = new BufferedReader( new InputStreamReader( System.in ) );
		
		while(true) {
			//String input = inputReader.readLine();
			g.sendMessage("test");
			//Thread.sleep(500);
			//handler.GetUpdates();
		}
	}

}
