package de.gsv.idm.shared.push.event.domain;

import de.gsv.idm.client.push.CUDListener;
import de.gsv.idm.shared.dto.DomainDTO;

@SuppressWarnings("serial")
public class DomainCreatedPushEvent extends DomainPushEvent {
	
	public DomainCreatedPushEvent() {
	}
	
	public DomainCreatedPushEvent(DomainDTO domain){
		super(domain);
	}

	public void call(CUDListener<DomainPushEvent> listener) {
		listener.onCreated(this);		
	}

}
