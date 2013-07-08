package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.Map;

import org.jgroups.Address;
import org.jgroups.Header;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.stack.IpAddress;

public class TestClient extends ReceiverAdapter {

	private JChannel channel;
	private String userName;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		new TestClient().start();
	}
	
	public TestClient() throws Exception {
		userName = System.getProperty("user.name","n/a");
		channel = new JChannel();
		channel.setReceiver(this);
	}

	
	public void start() throws Exception
	{
		Address serverAddress = new IpAddress( InetAddress.getByName("ec2-54-252-187-83.ap-southeast-2.compute.amazonaws.com") , 50512 );
		channel.connect("test", serverAddress, (long)2000);
		
		process();
	}
	
	// a method to read input and send it out as a message.
	private void process() {
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		
		while (true) {
			try {
				
				System.out.print("> ");
				System.out.flush();
				
				String line = input.readLine();
				Message msg = new Message(null, null, line);
				
				channel.send(msg);
				
			} catch (Exception e ) {
				e.printStackTrace();
			}
		}
	}
	
	
	// method to read incoming messages?
	// this must be threaded in some way.
	public void viewAccepted(View new_view) {
	    System.out.println("** view: " + new_view);
	}


	public void receive(Message msg) {
		System.out.println(msg.dest());
	    System.out.println(msg.getSrc() + ": " + msg.getObject());
	}
}
