package com.alex.framework.test;

import static org.junit.Assert.*;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import org.junit.Test;

import com.alex.framework.client.ServerHandler;
import com.alex.framework.server.Server;
import com.alex.framework.server.SimpleServer;
import com.alex.framework.client.TestClient;

public class RegistrationTest {

	//@Test
	public void testRegistration() throws UnknownHostException {
		Server s = new SimpleServer();
		s.Start();
		
		ServerHandler client = new com.alex.framework.client.TestClient(new InetSocketAddress("localhost",50512));
		
		client.Register();
		
		if ( ((TestClient)client).getidToken() == null )
		{
			fail();
		}
	}
	
	//@Test
	public void testGroupJoining() throws UnknownHostException {
		Server s = new SimpleServer();
		s.Start();
		
		ServerHandler client = new com.alex.framework.client.TestClient(new InetSocketAddress("localhost",50512));
		
		client.Register();
		
		client.JoinGroup("testGroup");
	}
	
	@Test
	public void testMessageSending() throws UnknownHostException {
		Server s = new SimpleServer();
		s.Start();
		
		// build two clients.
		ServerHandler client1 = new com.alex.framework.client.TestClient(new InetSocketAddress("localhost",50512));
		ServerHandler client2 = new com.alex.framework.client.TestClient(new InetSocketAddress("localhost",50512));
		
		// Make them both register and join the same group.
		client1.Register();
		client2.Register();
		
		client1.JoinGroup("testGroup");
		client2.JoinGroup("testGroup");
		
		client1.Post("Hello everyone!", "testGroup");
	}
	
	@Test
	public void testGettingUpdates() {
	}

}
