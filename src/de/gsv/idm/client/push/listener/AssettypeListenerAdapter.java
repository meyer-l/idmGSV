package de.gsv.idm.client.push.listener;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.event.db.assettype.CreatedAssettypeEvent;
import de.gsv.idm.client.event.db.assettype.DeletedAssettypeEvent;
import de.gsv.idm.client.event.db.assettype.UpdatedAssettypeEvent;
import de.gsv.idm.client.push.CUDListenerAdapter;
import de.gsv.idm.shared.dto.AssettypeDTO;
import de.gsv.idm.shared.push.event.assettype.AssettypePushEvent;

public class AssettypeListenerAdapter extends CUDListenerAdapter<AssettypePushEvent, AssettypeDTO> {

	private static AssettypeListenerAdapter instance;

	static public AssettypeListenerAdapter getInstance() {
		if (instance == null) {
			instance = new AssettypeListenerAdapter();
		}
		return instance;
	}

	public AssettypeListenerAdapter() {
		super(DBController.getInstance().getAssettypeController());
	}

	@Override
	public void fireCreatedEvent(AssettypePushEvent createdEvent) {
		eventBus.fireEvent(new CreatedAssettypeEvent(createdEvent.getObject()));
	}

	@Override
	public void fireDeletedEvent(AssettypePushEvent deletedEvent) {
		eventBus.fireEvent(new DeletedAssettypeEvent(deletedEvent.getObject()));
	}

	@Override
	public void fireUpdatedEvent(AssettypeDTO updatedEvent) {
		eventBus.fireEvent(new UpdatedAssettypeEvent(updatedEvent));
	}

}
