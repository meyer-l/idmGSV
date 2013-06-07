package de.gsv.idm.shared.push.event.securitylevelchange;

import de.gsv.idm.shared.dto.SecurityLevelChangeDTO;
import de.gsv.idm.shared.push.event.PushEvent;

@SuppressWarnings("serial")
public abstract class SecurityLevelChangePushEvent  extends PushEvent<SecurityLevelChangePushEvent, SecurityLevelChangeDTO> {
	public static final String CONVERSATION_DOMAIN = "securityLevelChange_pushDomain";
	
	SecurityLevelChangePushEvent(){
		
	}
	
	SecurityLevelChangePushEvent(SecurityLevelChangeDTO occupation){
		super(occupation);
	}
	
	public String getConservationDomain() {
        return CONVERSATION_DOMAIN;
    }
}
