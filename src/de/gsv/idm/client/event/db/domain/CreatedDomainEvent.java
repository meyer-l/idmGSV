package de.gsv.idm.client.event.db.domain;

import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.ObjectEvent;
import de.gsv.idm.shared.dto.DomainDTO;

public class CreatedDomainEvent extends ObjectEvent<DomainDTO,CreatedDomainEvent> {

	public static Type<GeneralEventHandler<CreatedDomainEvent>> TYPE =
			new  Type<GeneralEventHandler<CreatedDomainEvent>>();
	
	public CreatedDomainEvent(DomainDTO domain) {
		super(domain);
	}

	@Override
	public Type<GeneralEventHandler<CreatedDomainEvent>> getAssociatedType() {
		return TYPE;
	}
	
}
