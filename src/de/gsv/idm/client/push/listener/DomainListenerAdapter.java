package de.gsv.idm.client.push.listener;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.event.db.domain.CreatedDomainEvent;
import de.gsv.idm.client.event.db.domain.DeletedDomainEvent;
import de.gsv.idm.client.event.db.domain.UpdatedDomainEvent;
import de.gsv.idm.client.push.CUDListenerAdapter;
import de.gsv.idm.shared.dto.DomainDTO;
import de.gsv.idm.shared.push.event.domain.DomainPushEvent;

public class DomainListenerAdapter extends CUDListenerAdapter<DomainPushEvent, DomainDTO> {
		
	private static DomainListenerAdapter instance;

	static public DomainListenerAdapter getInstance() {
		if (instance == null) {
			instance = new DomainListenerAdapter();
		}
		return instance;
	}
	
	
	public DomainListenerAdapter() {
		super(DBController.getInstance().getDomainController());
	}	

	@Override
	public void fireCreatedEvent(DomainPushEvent createdEvent) {
		eventBus.fireEvent(new CreatedDomainEvent(createdEvent.getObject()));		
	}

	@Override
	public void fireDeletedEvent(DomainPushEvent deletedEvent) {
		eventBus.fireEvent(new DeletedDomainEvent(deletedEvent.getObject()));		
	}

	@Override
	public void fireUpdatedEvent(DomainDTO updatedEvent) {
		eventBus.fireEvent(new UpdatedDomainEvent(updatedEvent));		
	}

}
