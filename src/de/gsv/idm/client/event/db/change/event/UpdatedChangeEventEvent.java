package de.gsv.idm.client.event.db.change.event;

import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.ObjectEvent;
import de.gsv.idm.shared.dto.ChangeEventDTO;

public class UpdatedChangeEventEvent extends ObjectEvent<ChangeEventDTO, UpdatedChangeEventEvent> {

	public static Type<GeneralEventHandler<UpdatedChangeEventEvent>> TYPE = new Type<GeneralEventHandler<UpdatedChangeEventEvent>>();
	
	public UpdatedChangeEventEvent(ChangeEventDTO dbObject) {
	    super(dbObject);
    }

	@Override
    public Type<GeneralEventHandler<UpdatedChangeEventEvent>> getAssociatedType() {
	    return TYPE;
    }

}
