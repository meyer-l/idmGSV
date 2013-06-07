package de.gsv.idm.server.push;

public class PushUpdateSender implements Runnable {

	private PushUpdateHandler handler;
	
	public PushUpdateSender(PushUpdateHandler handler){
		this.handler = handler;
	}
	
	@Override
	public void run() {
		while(true) {			
			try {
				// Vary if eventService becomes cluttered. The Event Service sends a approx 1 event
				//every second
				handler.flush();
	            Thread.sleep(100);
            } catch (InterruptedException e) {
	            e.printStackTrace();
            }
		}
	}

}
