package de.gsv.idm.shared.push.event.assettype;

import de.gsv.idm.client.push.CUDListener;
import de.gsv.idm.shared.dto.AssettypeDTO;

@SuppressWarnings("serial")
public class AssettypeDeletedPushEvent extends AssettypePushEvent {

	public AssettypeDeletedPushEvent() {
	}
	
	public AssettypeDeletedPushEvent(AssettypeDTO asset){
		super(asset);
	}

	@Override
	public void call(CUDListener<AssettypePushEvent> listener) {
		listener.onDeleted(this);		
	}

}
