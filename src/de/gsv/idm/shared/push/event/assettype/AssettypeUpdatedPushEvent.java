package de.gsv.idm.shared.push.event.assettype;

import de.gsv.idm.client.push.CUDListener;
import de.gsv.idm.shared.dto.AssettypeDTO;

@SuppressWarnings("serial")
public class AssettypeUpdatedPushEvent extends AssettypePushEvent {

	public AssettypeUpdatedPushEvent() {
	}
	
	public AssettypeUpdatedPushEvent(AssettypeDTO asset){
		super(asset);
	}

	
	public void call(CUDListener<AssettypePushEvent> listener) {
		listener.onUpdated(this);		
	}

}
