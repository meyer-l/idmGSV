package de.gsv.idm.shared.push.event.occupation;

import de.gsv.idm.client.push.CUDListener;
import de.gsv.idm.shared.dto.OccupationDTO;

@SuppressWarnings("serial")
public class OccupationCreatedPushEvent extends OccupationPushEvent {
	public OccupationCreatedPushEvent(){

	}
	public OccupationCreatedPushEvent(OccupationDTO occupation){
		super(occupation);
	}
	@Override
	public void call(CUDListener<OccupationPushEvent> listener) {
		listener.onCreated(this);
	}
}
