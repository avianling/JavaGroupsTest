package com.alex.framework.test;

import com.alex.framework.server.Server;
import com.alex.framework.server.SimpleServer;

public class ServerTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Start a simple server.
		Server s = new SimpleServer();
		s.Start();
	}

}
