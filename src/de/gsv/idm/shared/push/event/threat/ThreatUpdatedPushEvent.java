package de.gsv.idm.shared.push.event.threat;

import de.gsv.idm.client.push.CUDListener;
import de.gsv.idm.shared.dto.ThreatDTO;

@SuppressWarnings("serial")
public class ThreatUpdatedPushEvent extends ThreatPushEvent {

	public ThreatUpdatedPushEvent() {
	}
	
	public ThreatUpdatedPushEvent(ThreatDTO measure){
		super(measure);
	}

	public void call(CUDListener<ThreatPushEvent> listener) {
		listener.onUpdated(this);		
	}

}
