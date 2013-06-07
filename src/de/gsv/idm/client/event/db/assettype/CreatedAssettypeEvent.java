package de.gsv.idm.client.event.db.assettype;

import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.ObjectEvent;
import de.gsv.idm.shared.dto.AssettypeDTO;

public class CreatedAssettypeEvent extends ObjectEvent<AssettypeDTO, CreatedAssettypeEvent> {

	public static Type<GeneralEventHandler<CreatedAssettypeEvent>> TYPE = new Type<GeneralEventHandler<CreatedAssettypeEvent>>();

	public CreatedAssettypeEvent(AssettypeDTO object) {
		super(object);
	}

	@Override
	public Type<GeneralEventHandler<CreatedAssettypeEvent>> getAssociatedType() {
		return TYPE;
	}

}
