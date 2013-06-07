package de.gsv.idm.client.event.db.change.event;

import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.ObjectEvent;
import de.gsv.idm.shared.dto.ChangeEventDTO;

public class DeletedChangeEventEvent extends ObjectEvent<ChangeEventDTO, DeletedChangeEventEvent> {

	public static Type<GeneralEventHandler<DeletedChangeEventEvent>> TYPE = new Type<GeneralEventHandler<DeletedChangeEventEvent>>();
	
	public DeletedChangeEventEvent(ChangeEventDTO dbObject) {
	    super(dbObject);
    }

	@Override
    public Type<GeneralEventHandler<DeletedChangeEventEvent>> getAssociatedType() {
	    return TYPE;
    }

}
