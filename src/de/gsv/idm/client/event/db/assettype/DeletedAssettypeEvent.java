package de.gsv.idm.client.event.db.assettype;

import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.ObjectEvent;
import de.gsv.idm.shared.dto.AssettypeDTO;

public class DeletedAssettypeEvent extends
        ObjectEvent<AssettypeDTO, DeletedAssettypeEvent> {

	public static Type<GeneralEventHandler<DeletedAssettypeEvent>> TYPE = new Type<GeneralEventHandler<DeletedAssettypeEvent>>();

	public DeletedAssettypeEvent(AssettypeDTO object) {
		super(object);
	}

	@Override
	public Type<GeneralEventHandler<DeletedAssettypeEvent>> getAssociatedType() {
		return TYPE;
	}
}
