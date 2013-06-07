package de.gsv.idm.client.push.listener;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.event.db.information.CreatedInformationEvent;
import de.gsv.idm.client.event.db.information.DeletedInformationEvent;
import de.gsv.idm.client.event.db.information.UpdatedInformationEvent;
import de.gsv.idm.client.push.CUDListenerAdapter;
import de.gsv.idm.shared.dto.InformationDTO;
import de.gsv.idm.shared.push.event.information.InformationPushEvent;

public class InformationListenerAdapter extends
        CUDListenerAdapter<InformationPushEvent, InformationDTO> {

	private static InformationListenerAdapter instance;

	static public InformationListenerAdapter getInstance() {
		if (instance == null) {
			instance = new InformationListenerAdapter();
		}
		return instance;
	}
	
	public InformationListenerAdapter() {
		super(DBController.getInstance().getInformationController());
	}

	@Override
	public void fireCreatedEvent(InformationPushEvent pushEvent) {
		eventBus.fireEvent(new CreatedInformationEvent(pushEvent.getObject()));
	}

	@Override
	public void fireDeletedEvent(InformationPushEvent pushEvent) {
		eventBus.fireEvent(new DeletedInformationEvent(pushEvent.getObject()));
	}

	@Override
	public void fireUpdatedEvent(InformationDTO pushEvent) {
		eventBus.fireEvent(new UpdatedInformationEvent(pushEvent));
	}

}
