package de.gsv.idm.server.push;

import de.gsv.idm.shared.push.event.PushEvent;
import de.novanic.eventservice.client.event.domain.Domain;

public class PushHandlerData {

	private Domain eventDomain;
	private PushEvent<?, ?> event;

	public PushHandlerData(Domain eventDomain,
	        PushEvent<?, ?> event) {
		this.eventDomain = eventDomain;
		this.event = event;
	}

	public Domain getEventDomain() {
    	return eventDomain;
    }

	public void setEventDomain(Domain eventDomain) {
    	this.eventDomain = eventDomain;
    }

	public PushEvent<?, ?> getEvent() {
    	return event;
    }

	public void setEvent(PushEvent<?, ?> event) {
    	this.event = event;
    }
	
	
}
