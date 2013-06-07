package de.gsv.idm.client.event.db.change.event;

import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.ObjectEvent;
import de.gsv.idm.shared.dto.ChangeEventDTO;

public class CreatedChangeEventEvent extends ObjectEvent<ChangeEventDTO, CreatedChangeEventEvent> {

	public static Type<GeneralEventHandler<CreatedChangeEventEvent>> TYPE = new Type<GeneralEventHandler<CreatedChangeEventEvent>>();
	
	public CreatedChangeEventEvent(ChangeEventDTO dbObject) {
	    super(dbObject);
    }

	@Override
    public Type<GeneralEventHandler<CreatedChangeEventEvent>> getAssociatedType() {
	    return TYPE;
    }

}
