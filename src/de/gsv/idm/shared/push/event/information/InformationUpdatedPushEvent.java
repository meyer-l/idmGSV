package de.gsv.idm.shared.push.event.information;

import de.gsv.idm.client.push.CUDListener;
import de.gsv.idm.shared.dto.InformationDTO;

@SuppressWarnings("serial")
public class InformationUpdatedPushEvent extends InformationPushEvent {
	public InformationUpdatedPushEvent(){

	}
	public InformationUpdatedPushEvent(InformationDTO information){
		super(information);
	}
	@Override
	public void call(CUDListener<InformationPushEvent> listener) {
		listener.onUpdated(this);
	}
}
