package de.gsv.idm.shared.push.event.threat;

import de.gsv.idm.client.push.CUDListener;
import de.gsv.idm.shared.dto.ThreatDTO;

@SuppressWarnings("serial")
public class ThreatCreatedPushEvent extends ThreatPushEvent {

	public ThreatCreatedPushEvent() {
	}
	
	public ThreatCreatedPushEvent(ThreatDTO measure){
		super(measure);
	}

	public void call(CUDListener<ThreatPushEvent> listener) {
		listener.onCreated(this);		
	}

}