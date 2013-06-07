package de.gsv.idm.shared.push.event.information;

import de.gsv.idm.client.push.CUDListener;
import de.gsv.idm.shared.dto.InformationDTO;

@SuppressWarnings("serial")
public class InformationDeletedPushEvent extends InformationPushEvent {

	public InformationDeletedPushEvent() {
	}
	
	public InformationDeletedPushEvent(InformationDTO information){
		super(information);
	}

	@Override
	public void call(CUDListener<InformationPushEvent> listener) {
		listener.onDeleted(this);
	}
}