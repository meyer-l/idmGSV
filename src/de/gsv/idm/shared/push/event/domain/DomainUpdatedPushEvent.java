package de.gsv.idm.shared.push.event.domain;

import de.gsv.idm.client.push.CUDListener;
import de.gsv.idm.shared.dto.DomainDTO;

@SuppressWarnings("serial")
public class DomainUpdatedPushEvent extends DomainPushEvent {

	public DomainUpdatedPushEvent(){

	}
	public DomainUpdatedPushEvent(DomainDTO domain){
		super(domain);
	}
	@Override
	public void call(CUDListener<DomainPushEvent> listener) {
		listener.onUpdated(this);		
	}

}
