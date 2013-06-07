package de.gsv.idm.shared.push.event.assettype;

import de.gsv.idm.shared.dto.AssettypeDTO;
import de.gsv.idm.shared.push.event.PushEvent;

@SuppressWarnings("serial")
public abstract class AssettypePushEvent extends PushEvent<AssettypePushEvent, AssettypeDTO> {
	public static final String CONVERSATION_DOMAIN = "assettype_pushDomain";
	
	AssettypePushEvent(){
		
	}
	
	AssettypePushEvent(AssettypeDTO object){
		super(object);
	}
	
	public String getConservationDomain() {
        return CONVERSATION_DOMAIN;
    }
	
}
