package de.gsv.idm.shared.push.event.assettype;

import de.gsv.idm.client.push.CUDListener;
import de.gsv.idm.shared.dto.AssettypeDTO;

@SuppressWarnings("serial")
public class AssettypeCreatedPushEvent extends AssettypePushEvent {

	public AssettypeCreatedPushEvent() {
	}
	
	public AssettypeCreatedPushEvent(AssettypeDTO asset){
		super(asset);
	}

	@Override
	public void call(CUDListener<AssettypePushEvent> listener) {
		listener.onCreated(this);		
	}

}
