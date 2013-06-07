package de.gsv.idm.client.event.db.occupation;

import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.ObjectEvent;
import de.gsv.idm.shared.dto.OccupationDTO;

public class UpdatedOccupationEvent extends
ObjectEvent<OccupationDTO, UpdatedOccupationEvent> {
public static Type<GeneralEventHandler<UpdatedOccupationEvent>> TYPE = new Type<GeneralEventHandler<UpdatedOccupationEvent>>();

public UpdatedOccupationEvent(OccupationDTO object) {
super(object);
}

@Override
public Type<GeneralEventHandler<UpdatedOccupationEvent>> getAssociatedType() {
return TYPE;
}

}
