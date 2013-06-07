package de.gsv.idm.shared.push.event.securityzone;

import de.gsv.idm.shared.dto.SecurityzoneDTO;
import de.gsv.idm.shared.push.event.PushEvent;

@SuppressWarnings("serial")
public abstract class SecurityzonePushEvent extends PushEvent<SecurityzonePushEvent, SecurityzoneDTO> {
	public static final String CONVERSATION_DOMAIN = "securityzone_pushDomain";
	
	public SecurityzonePushEvent(){
		
	}
	
	SecurityzonePushEvent(SecurityzoneDTO object) {
		super(object);
	}
	
	public String getConservationDomain() {
		return CONVERSATION_DOMAIN;
	}

}
