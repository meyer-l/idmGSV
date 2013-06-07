package de.gsv.idm.client.push.listener;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.event.db.assettype.category.CreatedAssettypeCategoryEvent;
import de.gsv.idm.client.event.db.assettype.category.DeletedAssettypeCategoryEvent;
import de.gsv.idm.client.event.db.assettype.category.UpdatedAssettypeCategoryEvent;
import de.gsv.idm.client.push.CUDListenerAdapter;
import de.gsv.idm.shared.dto.AssettypeCategoryDTO;
import de.gsv.idm.shared.push.event.assettype.category.AssettypeCategoryPushEvent;

public class AssettypeCategoryListenerAdapter extends
        CUDListenerAdapter<AssettypeCategoryPushEvent, AssettypeCategoryDTO> {

	private static AssettypeCategoryListenerAdapter instance;

	static public AssettypeCategoryListenerAdapter getInstance() {
		if (instance == null) {
			instance = new AssettypeCategoryListenerAdapter();
		}
		return instance;
	}

	public AssettypeCategoryListenerAdapter() {
		super(DBController.getInstance().getAssettypeCategoryController());
	}

	@Override
	protected void fireCreatedEvent(AssettypeCategoryPushEvent createdEvent) {
		eventBus.fireEvent(new CreatedAssettypeCategoryEvent(createdEvent.getObject()));
	}

	@Override
	protected void fireDeletedEvent(AssettypeCategoryPushEvent deletedEvent) {
		eventBus.fireEvent(new DeletedAssettypeCategoryEvent(deletedEvent.getObject()));
	}

	@Override
    public void fireUpdatedEvent(AssettypeCategoryDTO updatedEvent) {
		eventBus.fireEvent(new UpdatedAssettypeCategoryEvent(updatedEvent));
	}

}
