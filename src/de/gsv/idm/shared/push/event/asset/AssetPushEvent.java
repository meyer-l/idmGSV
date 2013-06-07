package de.gsv.idm.shared.push.event.asset;

import de.gsv.idm.shared.dto.AssetDTO;
import de.gsv.idm.shared.push.event.PushEvent;

@SuppressWarnings("serial")
public abstract class AssetPushEvent extends PushEvent<AssetPushEvent, AssetDTO> {
	public static final String CONVERSATION_DOMAIN = "asset_pushDomain";
	
	AssetPushEvent(){
		
	}
	
	AssetPushEvent(AssetDTO object){
		super(object);
	}
	
	public String getConservationDomain() {
        return CONVERSATION_DOMAIN;
    }
	
}
