package de.gsv.idm.shared.push.event.threat;

import de.gsv.idm.shared.dto.ThreatDTO;
import de.gsv.idm.shared.push.event.PushEvent;

@SuppressWarnings("serial")
public abstract class ThreatPushEvent extends PushEvent<ThreatPushEvent,ThreatDTO> {
	public static final String CONVERSATION_DOMAIN = "threat_pushDomain";

	ThreatPushEvent() {

	}

	ThreatPushEvent(ThreatDTO threat) {
		super(threat);
	}

	public String getConservationDomain() {
		return CONVERSATION_DOMAIN;
	}

}
