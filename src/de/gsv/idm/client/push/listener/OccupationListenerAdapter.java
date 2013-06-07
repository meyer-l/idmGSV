package de.gsv.idm.client.push.listener;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.event.db.occupation.CreatedOccupationEvent;
import de.gsv.idm.client.event.db.occupation.DeletedOccupationEvent;
import de.gsv.idm.client.event.db.occupation.UpdatedOccupationEvent;
import de.gsv.idm.client.push.CUDListenerAdapter;
import de.gsv.idm.shared.dto.OccupationDTO;
import de.gsv.idm.shared.push.event.occupation.OccupationPushEvent;

public class OccupationListenerAdapter extends
        CUDListenerAdapter<OccupationPushEvent, OccupationDTO> {

	private static OccupationListenerAdapter instance;

	static public OccupationListenerAdapter getInstance() {
		if (instance == null) {
			instance = new OccupationListenerAdapter();
		}
		return instance;
	}
	
	public OccupationListenerAdapter() {
		super(DBController.getInstance().getOccupationController());
	}

	@Override
	public void fireCreatedEvent(OccupationPushEvent pushEvent) {
		eventBus.fireEvent(new CreatedOccupationEvent(pushEvent.getObject()));
	}

	@Override
	public void fireDeletedEvent(OccupationPushEvent pushEvent) {
		eventBus.fireEvent(new DeletedOccupationEvent(pushEvent.getObject()));
	}

	@Override
	public void fireUpdatedEvent(OccupationDTO pushEvent) {
		eventBus.fireEvent(new UpdatedOccupationEvent(pushEvent));
	}

}
