package de.gsv.idm.shared.push.event.change.event;

import de.gsv.idm.client.push.CUDListener;
import de.gsv.idm.shared.dto.ChangeEventDTO;

@SuppressWarnings("serial")
public class ChangeEventCreatedPushEvent extends ChangeEventPushEvent {

	public ChangeEventCreatedPushEvent(){

	}
	public ChangeEventCreatedPushEvent(ChangeEventDTO changeEvent){
		super(changeEvent);
	}
	
	@Override
	public void call(CUDListener<ChangeEventPushEvent> listener) {
		listener.onCreated(this);
	}

}
