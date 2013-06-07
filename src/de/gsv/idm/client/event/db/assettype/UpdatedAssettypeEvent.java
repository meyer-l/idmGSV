package de.gsv.idm.client.event.db.assettype;

import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.ObjectEvent;
import de.gsv.idm.shared.dto.AssettypeDTO;

public class UpdatedAssettypeEvent extends ObjectEvent<AssettypeDTO, UpdatedAssettypeEvent> {

	public static Type<GeneralEventHandler<UpdatedAssettypeEvent>> TYPE = new Type<GeneralEventHandler<UpdatedAssettypeEvent>>();

	public UpdatedAssettypeEvent(AssettypeDTO object) {
		super(object);
	}

	@Override
	public Type<GeneralEventHandler<UpdatedAssettypeEvent>> getAssociatedType() {
		return TYPE;
	}

}
