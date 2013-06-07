package de.gsv.idm.shared.push.event.change.event;

import de.gsv.idm.shared.dto.ChangeEventDTO;
import de.gsv.idm.shared.push.event.PushEvent;

@SuppressWarnings("serial")
public abstract class ChangeEventPushEvent extends PushEvent<ChangeEventPushEvent, ChangeEventDTO> {
	public static final String CONVERSATION_DOMAIN = "changeEvent_pushDomain";

	public ChangeEventPushEvent() {

	}

	public ChangeEventPushEvent(ChangeEventDTO object) {
		super(object);
	}

	public String getConservationDomain() {
		return CONVERSATION_DOMAIN;
	}
}
