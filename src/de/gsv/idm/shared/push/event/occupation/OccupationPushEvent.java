package de.gsv.idm.shared.push.event.occupation;

import de.gsv.idm.shared.dto.OccupationDTO;
import de.gsv.idm.shared.push.event.PushEvent;

@SuppressWarnings("serial")
public abstract class OccupationPushEvent extends PushEvent<OccupationPushEvent, OccupationDTO> {
	public static final String CONVERSATION_DOMAIN = "occupation_pushDomain";
	
	OccupationPushEvent(){
		
	}
	
	OccupationPushEvent(OccupationDTO occupation){
		super(occupation);
	}
	
	public String getConservationDomain() {
        return CONVERSATION_DOMAIN;
    }

}
