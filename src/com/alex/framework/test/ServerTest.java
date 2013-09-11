package com.alex.framework.test;

import com.alex.framework.server.Server;
import com.alex.framework.server.SimpleServer;
import com.alex.logging.Logger;

public class ServerTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Start a simple server.
		Logger.LogToConsole = false;
		Server s = new SimpleServer();
		
		s.Start();
	}

}
