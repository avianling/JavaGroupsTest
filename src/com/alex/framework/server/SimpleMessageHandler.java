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
import com.alex.framework.MessageConstants;
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
			Logger.Log("Server: Received message '" + data + "'");
			Message m = JSON.parser.fromJson(data, Message.class);
			
			Logger.Log("Server: finished downloading message");
			
			// Now that we have the message, we must process it.
			// If it's a normal message, forward it to javagroups.
			// If it's a users registration, register them & reply with an okay.
			// Actually process the message...
			
			// At present, just reply with an 'ok' message and a blank ID token.
			
			Message response = new Message();
			
			switch ( m.Headers.get("code") ) {
			case MessageConstants.CODE_REGISTRATION:
				// Run the registration code.
				String clientIDToken = ClientRegistrar.get().registerClient();
				response.Headers.put("idToken",clientIDToken);
				response.Headers.put("code","ok");
				break;
				
			case MessageConstants.CODE_JOIN_GROUP:
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
				
			case MessageConstants.CODE_MESSAGE_STRING:
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
				
			case MessageConstants.CODE_UPDATE_REQUEST:
				// Work out which client we are working with.
				if ( m.Headers.containsKey(MessageConstants.FIELD_IDTOKEN)) {
					// Get a list of all of the messages we need to send back to the user.
					try {
						ClientHandler client = ClientRegistrar.get().findClient(m.Headers.get(MessageConstants.FIELD_IDTOKEN));
						
						for ( String groupName : client.getGroups() ) {
							GroupAdapter adapter = client.getGroup(groupName);
							for ( Message newMessage : adapter.getUpdates() ) {
								clientSocket.getOutputStream().write(newMessage.Serialize().getBytes());
								clientSocket.getOutputStream().write("\n".getBytes());
							}
						}
						
					} catch (NoSuchClientException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						response.setHeader(MessageConstants.FIELD_CODE, MessageConstants.CODE_FAIL);
						response.Payload = e.getMessage();
						break;
					}
					
				} else {
					response.setHeader(MessageConstants.FIELD_CODE, MessageConstants.CODE_FAIL);
				}
				
				
				break;
			}
			
			
			
			clientSocket.getOutputStream().write(response.Serialize().getBytes());
			clientSocket.getOutputStream().write("\n\n".getBytes());
			clientSocket.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
