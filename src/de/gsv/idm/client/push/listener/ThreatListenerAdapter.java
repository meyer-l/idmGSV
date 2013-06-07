package de.gsv.idm.client.push.listener;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.event.db.threat.CreatedThreatEvent;
import de.gsv.idm.client.event.db.threat.DeletedThreatEvent;
import de.gsv.idm.client.event.db.threat.UpdatedThreatEvent;
import de.gsv.idm.client.push.CUDListenerAdapter;
import de.gsv.idm.shared.dto.ThreatDTO;
import de.gsv.idm.shared.push.event.threat.ThreatPushEvent;

public class ThreatListenerAdapter extends CUDListenerAdapter<ThreatPushEvent, ThreatDTO> {

	private static ThreatListenerAdapter instance;

	static public ThreatListenerAdapter getInstance() {
		if (instance == null) {
			instance = new ThreatListenerAdapter();
		}
		return instance;
	}
	
	public ThreatListenerAdapter() {
		super(DBController.getInstance().getThreatController());
	}

	@Override
	public void fireCreatedEvent(ThreatPushEvent createdEvent) {
		eventBus.fireEvent(new CreatedThreatEvent(createdEvent.getObject()));
	}

	@Override
	public void fireDeletedEvent(ThreatPushEvent deletedEvent) {
		eventBus.fireEvent(new DeletedThreatEvent(deletedEvent.getObject()));
	}

	@Override
	public void fireUpdatedEvent(ThreatDTO updatedEvent) {
		eventBus.fireEvent(new UpdatedThreatEvent(updatedEvent));
	}

}
