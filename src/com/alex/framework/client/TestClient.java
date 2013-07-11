package com.alex.framework.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import com.alex.framework.Message;
import com.alex.json.JSON;
import com.alex.logging.Logger;

public class TestClient implements Client {

	private String username;
	
	private Socket _socket;
	
	private MessageHandler handler;
	
	private String idToken;
	
	public TestClient() {
		// Get a unique username.
		username = "alexander";
		JSON json = new JSON();
	}
	
	@Override
	public void Connect() {
		_socket = new Socket();
		try {
			_socket.connect( new InetSocketAddress(InetAddress.getLocalHost(), 50512) );
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void SetMessageHandler(MessageHandler msgHandler) {
		handler = msgHandler;
	}

	@Override
	public void Close() {
		// TODO Auto-generated method stub
		
	}

	public Message SendMessage(Message msg) {
		Connect();
		
		Logger.Log("Client: Starting to send message.");
		try {
			OutputStream out = _socket.getOutputStream();
			String data = msg.Serialize();
			out.write(data.getBytes());
			out.write("\n".getBytes());
			
			Logger.Log("Client: Message Sent.");
			
			// Wait for the okay response.
			// If we don't get one within a certain time limit, retry sending this message.
			InputStream in = _socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			
			Message response = (Message) JSON.fromJson(reader.readLine(), Message.class);
			Logger.Log("Client: Received response '" + response.Serialize() + "' from server.");
			
			out.close();
			in.close();
			_socket.close();
			
			return response;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void Register() {
		// Construct a registration message.
		Message msg = new Message();
		msg.Headers.put("code", "registration");
		
		Logger.Log("Client: Starting to send registration message");
		
		Message response = SendMessage(msg);
		
		if ( response != null ) {
			// Check the response to see if we were able to register successfully.
			switch ( response.Headers.get("code") ) {
			case "ok":
				// Registered successfully.
				idToken = response.Headers.get("idToken");
				break;
				
			case "fail":
				// We failed to register
				//TODO: Handle this case here. 
				break;
			}
		}
		//TODO: Handle what happens if we don't get a response from sendMessage.
		//TODO: Should also probably handle timeouts etc.
		
	}

	public String getidToken() {
		return idToken;
	}

	
	@Override
	public void JoinGroup(String groupName) {
		// Generate the message to send to the server.
		Message msg = new Message();
		//TODO: The method construction should be handled by a factory. Applied to all message construction.
		msg.Headers.put("code", "joinGroup");
		msg.Headers.put("groupName", groupName);
		msg.Headers.put("idToken", idToken);
		
		Message response = SendMessage(msg);
	}
	
	/**
	 * A method for sending a given message to the server.
	 */
	@Override
	public void Post(String message, String group) {
		Message msg = new Message();
		msg.Payload = message;
		msg.Headers.put("code", "message");
		msg.Headers.put("idToken", idToken);
		msg.Headers.put("groupName", group);
		
		Message response = SendMessage(msg);
	}

}
