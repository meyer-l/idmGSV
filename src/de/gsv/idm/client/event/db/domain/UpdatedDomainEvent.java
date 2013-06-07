package de.gsv.idm.client.event.db.domain;

import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.ObjectEvent;
import de.gsv.idm.shared.dto.DomainDTO;

public class UpdatedDomainEvent extends ObjectEvent<DomainDTO,UpdatedDomainEvent> {

	public static Type<GeneralEventHandler<UpdatedDomainEvent>> TYPE = new Type<GeneralEventHandler<UpdatedDomainEvent>>();

	public UpdatedDomainEvent(DomainDTO domain) {
		super(domain);
	}

	@Override
	public Type<GeneralEventHandler<UpdatedDomainEvent>> getAssociatedType() {
		return TYPE;
	}

}
