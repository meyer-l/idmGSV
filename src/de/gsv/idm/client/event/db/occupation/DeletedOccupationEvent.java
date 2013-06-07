package de.gsv.idm.client.event.db.occupation;

import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.ObjectEvent;
import de.gsv.idm.shared.dto.OccupationDTO;

public class DeletedOccupationEvent extends
		ObjectEvent<OccupationDTO, DeletedOccupationEvent> {
	public static Type<GeneralEventHandler<DeletedOccupationEvent>> TYPE = new Type<GeneralEventHandler<DeletedOccupationEvent>>();

	public DeletedOccupationEvent(OccupationDTO object) {
		super(object);
	}

	@Override
	public Type<GeneralEventHandler<DeletedOccupationEvent>> getAssociatedType() {
		return TYPE;
	}

}
