package de.gsv.idm.client.push.listener;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.event.db.securityzone.CreatedSecurityzoneEvent;
import de.gsv.idm.client.event.db.securityzone.DeletedSecurityzoneEvent;
import de.gsv.idm.client.event.db.securityzone.UpdatedSecurityzoneEvent;
import de.gsv.idm.client.push.CUDListenerAdapter;
import de.gsv.idm.shared.dto.SecurityzoneDTO;
import de.gsv.idm.shared.push.event.securityzone.SecurityzonePushEvent;

public class SecurityzoneListenerAdapter extends
        CUDListenerAdapter<SecurityzonePushEvent, SecurityzoneDTO> {

	private static SecurityzoneListenerAdapter instance;

	static public SecurityzoneListenerAdapter getInstance() {
		if (instance == null) {
			instance = new SecurityzoneListenerAdapter();
		}
		return instance;
	}

	public SecurityzoneListenerAdapter() {
		super(DBController.getInstance().getSecurityzoneController());
	}

	@Override
	protected void fireCreatedEvent(SecurityzonePushEvent createdEvent) {
		eventBus.fireEvent(new CreatedSecurityzoneEvent(createdEvent.getObject()));
	}

	@Override
	protected void fireDeletedEvent(SecurityzonePushEvent deletedEvent) {
		eventBus.fireEvent(new DeletedSecurityzoneEvent(deletedEvent.getObject()));
	}

	@Override
	public void fireUpdatedEvent(SecurityzoneDTO updatedEvent) {
		eventBus.fireEvent(new UpdatedSecurityzoneEvent(updatedEvent));
	}

}
