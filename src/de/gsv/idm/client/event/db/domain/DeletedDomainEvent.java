package de.gsv.idm.client.event.db.domain;

import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.ObjectEvent;
import de.gsv.idm.shared.dto.DomainDTO;

public class DeletedDomainEvent extends ObjectEvent<DomainDTO,DeletedDomainEvent> {

	public static Type<GeneralEventHandler<DeletedDomainEvent>> TYPE = new  Type<GeneralEventHandler<DeletedDomainEvent>>();
	
	public DeletedDomainEvent(DomainDTO domain) {
		super(domain);
	}
	
	
	public Type<GeneralEventHandler<DeletedDomainEvent>> getAssociatedType() {
		return TYPE;
	}

}
