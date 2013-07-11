package client;

import com.alex.framework.Message;
import com.alex.framework.client.Client;
import com.alex.framework.server.Server;
import com.alex.framework.server.SimpleServer;

public class FrameworkClientTest {
	public static void main(String[] args ) throws Exception {
		Client client = new com.alex.framework.client.TestClient();
		
		// Hack - launch the server on this machine. 
		Server s = new SimpleServer();
		s.Start();
		
		Message m = new Message();
		m.Headers.put("test", "something");
		m.Payload = "This is a test message";
		
		//client.SendMessage(m);
	}
}
