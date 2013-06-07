package de.gsv.idm.shared.push.event.change.event;

import de.gsv.idm.client.push.CUDListener;
import de.gsv.idm.shared.dto.ChangeEventDTO;

@SuppressWarnings("serial")
public class ChangeEventDeletedPushEvent extends ChangeEventPushEvent {

	public ChangeEventDeletedPushEvent(){

	}
	public ChangeEventDeletedPushEvent(ChangeEventDTO changeEvent){
		super(changeEvent);
	}
	
	@Override
	public void call(CUDListener<ChangeEventPushEvent> listener) {
		listener.onDeleted(this);
	}

}
