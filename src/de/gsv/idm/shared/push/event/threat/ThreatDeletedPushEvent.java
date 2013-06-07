package de.gsv.idm.shared.push.event.threat;

import de.gsv.idm.client.push.CUDListener;
import de.gsv.idm.shared.dto.ThreatDTO;

@SuppressWarnings("serial")
public class ThreatDeletedPushEvent extends ThreatPushEvent {

	public ThreatDeletedPushEvent() {
	}
	
	public ThreatDeletedPushEvent(ThreatDTO measure){
		super(measure);
	}

	public void call(CUDListener<ThreatPushEvent> listener) {
		listener.onDeleted(this);		
	}

}
