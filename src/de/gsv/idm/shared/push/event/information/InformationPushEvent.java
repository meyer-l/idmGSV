package de.gsv.idm.shared.push.event.information;

import de.gsv.idm.shared.dto.InformationDTO;
import de.gsv.idm.shared.push.event.PushEvent;

@SuppressWarnings("serial")
public abstract class InformationPushEvent extends PushEvent<InformationPushEvent, InformationDTO> {
	public static final String CONVERSATION_DOMAIN = "information_pushDomain";

	InformationPushEvent() {

	}

	InformationPushEvent(InformationDTO information) {
		super(information);
	}

	public String getConservationDomain() {
		return CONVERSATION_DOMAIN;
	}

}
