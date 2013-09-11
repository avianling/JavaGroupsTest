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
import java.util.LinkedList;
import java.util.List;

import com.alex.framework.Message;
import com.alex.framework.MessageConstants;
import com.alex.framework.MessageFactory;
import com.alex.json.JSON;
import com.alex.logging.Logger;
import com.alex.logging.TimingRecord;

public class TestClient implements ServerHandler {

	private String username;
	
	private Socket _socket;
	
	private MessageHandler handler;
	
	private String idToken;
	
	private InetSocketAddress serverAddress;
	
	
	
	public TestClient( InetSocketAddress server ) {
		// Get a unique username.
		username = "alexander";
		JSON json = new JSON();
		
		
		//serverAddress = new InetSocketAddress(InetAddress.getByName(serverIP), port);
		serverAddress = server;
	}
	
	@Override
	public boolean Connect() {
		int attempts = 0;
		while ( attempts < 10 ) {
			try {
				_socket = new Socket();
				_socket.connect( serverAddress );
				return true;
			} catch (IOException e) {
				attempts += 1;
				//TODO: Remove this line. It will make output look messy.
				e.printStackTrace();
				try {
					Thread.sleep(40);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
			attempts++;
		}
		return false;
	}

	@Override
	public void SetMessageHandler(MessageHandler msgHandler) {
		handler = msgHandler;
	}

	@Override
	public void Close() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * A Helped function to send a message to the server
	 * and return an array of all the messages we received.
	 * @param msg
	 * @return
	 */
	public List<Message> SendMessage(Message msg) {
		//Logger.LogTiming("Starting to connect");
		TimingRecord r = TimingRecord.records.get(msg.Headers.get("uuid"));
		long temp = System.nanoTime();
		
		// Connect will return true if we were able to connect, false otherwise.
		if ( !Connect() ) {
			
			System.out.println("We had a booboo :(");
			return null;
		}
		
		if ( r != null ) {
			r.connectionTime = System.nanoTime() - temp;
		}
		
		Logger.Log("Client: Starting to send message.");
		try {
			OutputStream out = _socket.getOutputStream();
			
			temp = System.nanoTime();
			String data = msg.Serialize();
			if ( r!=null )
				r.serializationTime = System.nanoTime() - temp;
			
			temp = System.nanoTime();
			out.write(data.getBytes());
			out.write("\n\n".getBytes()); // one new line to signal the end of the message and one to signal the end of the sequence.
			if ( r!=null )
				r.sendingTime = System.nanoTime() - temp;
			
			temp = System.nanoTime();
			
			Logger.Log("Client: Message Sent.");
			
			
			
			// Wait for the okay response.
			// If we don't get one within a certain time limit, retry sending this message.
			InputStream in = _socket.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			
			// ALEX: Modifying to allow the client to read multiple messages in one go.
			// Message sequences will be terminated with a blank line. 
			LinkedList<Message> receivedMessages = new LinkedList<Message>();
			

			String line = reader.readLine();
			if ( r!=null )
				r.waitingForResponseTime = System.nanoTime() - temp;
			while ( line.length() > 0 ) {
				temp = System.nanoTime();
				Message response = (Message) JSON.fromJson(line, Message.class);
				if ( r!=null ) r.serializationTime += System.nanoTime() - temp;
				
				// If the message is valid, add it to the list of received messages.
				if ( response != null ) {
					receivedMessages.addLast(response);
				}
				
				temp = System.nanoTime();
				line = reader.readLine();
				if ( r!=null ) r.downloadingTime = System.nanoTime() - temp;
			}
			
			out.close();
			in.close();
			_socket.close();
			
			return receivedMessages;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void Register() {
		// Construct a registration message.
		Message msg = MessageFactory.makeRegisterMessage();
		
		Logger.Log("Client: Starting to send registration message");
		
		LinkedList<Message> response = (LinkedList)SendMessage(msg);
		
		// We only expect one response from the server.
		String msgCode = response.getFirst().Headers.get(MessageConstants.FIELD_CODE);
		if ( response.size() == 1 ) {
			// Check the response to see if we were able to register successfully.
			if ( response.getFirst().Headers.get(MessageConstants.FIELD_CODE).equalsIgnoreCase(MessageConstants.CODE_SUCCESS)  ) {
				// Registered successfully.
				idToken = response.getFirst().Headers.get(MessageConstants.FIELD_IDTOKEN);
			} else {
				
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
		Message msg = MessageFactory.makeJoinGroupMessage(idToken, groupName);
		/*msg.Headers.put("code", "joinGroup");
		msg.Headers.put("groupName", groupName);
		msg.Headers.put("idToken", idToken);*/
		
		List<Message> response = SendMessage(msg);
		
		
		//TODO: Handle message responses / failures.
		// check the first response. This should have the fail / success codes.
		Message m = response.get(0);
		if ( m.Headers.get(MessageConstants.FIELD_CODE).equals(MessageConstants.CODE_FAIL)) { 
			// if the reason is because we weren't registered, register and then try this again.
			if ( m.Headers.get(MessageConstants.FIELD_FAILURE_CAUSE).equals(MessageConstants.FAIL_CAUSE_NOT_REGISTERED) ) {
				System.out.println("001: Tried to join a group, but we weren't registered");
				Register();
				JoinGroup(groupName);
			}
		}
	}
	
	/**
	 * A method for sending a given message to the server.
	 */
	@Override
	public void Post(String message, String group) {
		//Logger.LogTiming("Starting to build message");
		Message msg = MessageFactory.makeStringMessage(idToken, group, message);
		//Logger.LogTiming("Message Built");
		/*msg.Payload = message;
		msg.Headers.put("code", "message");
		msg.Headers.put("idToken", idToken);
		msg.Headers.put("groupName", group);*/
		
		List<Message> response = SendMessage(msg);
		
		Message m = response.get(0);
		if ( m!=null ) {
			if ( m.Headers.get(MessageConstants.FIELD_CODE).equals(MessageConstants.CODE_FAIL)) { 
				// if the reason is because we weren't registered, register and then try this again.
				if ( m.Headers.get(MessageConstants.FIELD_FAILURE_CAUSE).equals(MessageConstants.FAIL_CAUSE_NOT_REGISTERED) ) {
					System.out.println("002: Tried to post, but we weren't registered");
					Register();
					JoinGroup(group);
					Post(message, group);
				}
			}
		}
	}

	@Override
	public void GetUpdates() {
		Message msg = MessageFactory.makeUpdateRequestMessage(idToken);
		
		List<Message> response = SendMessage(msg);
		
		for ( int i=1; i < response.size(); i++ ) {
			Message m = response.get(i);
			System.out.println(m.Serialize());
			handler.messageReceived(m);
		}
	}

}
