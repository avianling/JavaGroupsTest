package com.alex.framework.server;

import com.alex.framework.server.registrar.ClientRegistrar;

public class LeaseController implements Runnable {

	public static final long leaseTime = 60000;
	
	@Override
	public void run() {
		// Check that for every group adapter, it isn't past their lease expiration time.
		/*while ( true ) {
		
			ClientRegistrar.get().checkClientLeases();
			
			System.out.println("Checking Leases");
			
			// Now check if there are any groups without members.
			// If there aren't, destroy that group.
			
			
			try {
				Thread.sleep(leaseTime);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
	}
	
}
