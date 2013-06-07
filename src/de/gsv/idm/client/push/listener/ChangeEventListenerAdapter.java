package de.gsv.idm.client.push.listener;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.event.db.change.event.CreatedChangeEventEvent;
import de.gsv.idm.client.event.db.change.event.DeletedChangeEventEvent;
import de.gsv.idm.client.event.db.change.event.UpdatedChangeEventEvent;
import de.gsv.idm.client.push.CUDListenerAdapter;
import de.gsv.idm.shared.dto.ChangeEventDTO;
import de.gsv.idm.shared.push.event.change.event.ChangeEventPushEvent;

public class ChangeEventListenerAdapter extends
        CUDListenerAdapter<ChangeEventPushEvent, ChangeEventDTO> {

	private static ChangeEventListenerAdapter instance;

	static public ChangeEventListenerAdapter getInstance() {
		if (instance == null) {
			instance = new ChangeEventListenerAdapter();
		}
		return instance;
	}

	public ChangeEventListenerAdapter() {
		super(DBController.getInstance().getChangeEventController());
	}

	@Override
	protected void fireCreatedEvent(ChangeEventPushEvent createdEvent) {
		eventBus.fireEvent(new CreatedChangeEventEvent(createdEvent.getObject()));
	}

	@Override
	protected void fireDeletedEvent(ChangeEventPushEvent deletedEvent) {
		eventBus.fireEvent(new DeletedChangeEventEvent(deletedEvent.getObject()));
	}

	@Override
	public void fireUpdatedEvent(ChangeEventDTO updatedEvent) {
		eventBus.fireEvent(new UpdatedChangeEventEvent(updatedEvent));
	}

}
