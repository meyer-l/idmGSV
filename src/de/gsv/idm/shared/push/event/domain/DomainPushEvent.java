package de.gsv.idm.shared.push.event.domain;

import de.gsv.idm.shared.dto.DomainDTO;
import de.gsv.idm.shared.push.event.PushEvent;

@SuppressWarnings("serial")
public abstract class DomainPushEvent extends PushEvent<DomainPushEvent, DomainDTO> {
	public static final String CONVERSATION_DOMAIN = "domain_pushDomain";
	
	DomainPushEvent(){
		
	}
	
	DomainPushEvent(DomainDTO domain){
		super(domain);
	}
	
	public String getConservationDomain() {
        return CONVERSATION_DOMAIN;
    }

}
