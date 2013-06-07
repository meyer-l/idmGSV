package de.gsv.idm.shared.push.event.occupation;

import de.gsv.idm.client.push.CUDListener;
import de.gsv.idm.shared.dto.OccupationDTO;

@SuppressWarnings("serial")
public class OccupationDeletedPushEvent extends OccupationPushEvent {
	public OccupationDeletedPushEvent(){

	}
	public OccupationDeletedPushEvent(OccupationDTO occupation){
		super(occupation);
	}
	@Override
	public void call(CUDListener<OccupationPushEvent> listener) {
		listener.onDeleted(this);
	}
}
