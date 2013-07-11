package com.alex.framework.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.Socket;
import java.util.Map;

import com.alex.framework.Message;
import com.alex.framework.server.exceptions.NoSuchClientException;
import com.alex.framework.server.registrar.ClientRegistrar;
import com.alex.json.JSON;
import com.alex.logging.Logger;
import com.google.gson.Gson;

public class SimpleMessageHandler implements MessageHandler {
	

	@Override
	public void parse(Socket clientSocket) {
		// Download the message
		try {
			InputStream input = clientSocket.getInputStream();
			
			InputStreamReader r = new InputStreamReader(input);
			Logger.Log("Server: Starting to download message");
			
			// Manually read the string... :(
			BufferedReader reader = new BufferedReader(r);
			String data = reader.readLine();
			System.out.println("Server: Received message '" + data + "'");
			Message m = JSON.parser.fromJson(data, Message.class);
			
			Logger.Log("Server: finished downloading message");
			
			// Now that we have the message, we must process it.
			// If it's a normal message, forward it to javagroups.
			// If it's a users registration, register them & reply with an okay.
			// Actually process the message...
			
			// At present, just reply with an 'ok' message and a blank ID token.
			
			Message response = new Message();
			
			switch ( m.Headers.get("code") ) {
			case "registration":
				// Run the registration code.
				String clientIDToken = ClientRegistrar.get().registerClient();
				response.Headers.put("idToken",clientIDToken);
				response.Headers.put("code","ok");
				break;
				
			case "joinGroup":
				// Run the join group code.
				// Check if we have an idtoken.
				// If we don't, fail.
				if ( m.Headers.containsKey("idToken") ) {
					if ( m.Headers.containsKey("groupName" ) )
					{
						try {
							ClientHandler handler = ClientRegistrar.get().findClient(m.Headers.get("idToken"));
							handler.joinGroup(m.Headers.get("groupName"));
							
							response.Headers.put("code", "ok");
							response.Payload = "Joined the group " + m.Headers.get("groupName");
						} catch ( NoSuchClientException e ) {
							response.Headers.put("code", "fail");
							response.Payload = e.getMessage();
						}
					} else {
						response.Headers.put("code", "fail");
						response.Payload = "Failed - no group name was supplied";
						break;
					}
				} else {
					response.Headers.put("code", "fail");
					response.Payload = "Failed - no id token supplied. Try registering first?";
					break;
				}
				
				break;
				
			case "message":
				// Post a message.
				// check if this is a valid client.
				if ( m.Headers.containsKey("idToken") ) {
					if ( m.Headers.containsKey("groupName")) {
						try {
							ClientHandler clientHandler = ClientRegistrar.get().findClient(m.Headers.get("idToken"));
							clientHandler.sendMessageToGroup(m);
							
						} catch ( NoSuchClientException e )
						{
							response.Headers.put("code","fail");
							response.Payload = e.getMessage();
							break;
						}
					} else {
						response.Headers.put("code", "fail");
						response.Payload = "Failed - no group name was supplied";
						break;
					}
				} else {
					response.Headers.put("code", "fail");
					response.Payload = "Failed - no id token supplied. Try registering first?";
					break;
				}
				
				
				
				break;
			}
			
			
			
			clientSocket.getOutputStream().write(response.Serialize().getBytes());
			clientSocket.getOutputStream().write("\n".getBytes());
			clientSocket.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}


}
