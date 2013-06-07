package de.gsv.idm.client.event.db.occupation;

import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.ObjectEvent;
import de.gsv.idm.shared.dto.OccupationDTO;

public class CreatedOccupationEvent extends
ObjectEvent<OccupationDTO, CreatedOccupationEvent> {
public static Type<GeneralEventHandler<CreatedOccupationEvent>> TYPE = new Type<GeneralEventHandler<CreatedOccupationEvent>>();

public CreatedOccupationEvent(OccupationDTO object) {
super(object);
}

@Override
public Type<GeneralEventHandler<CreatedOccupationEvent>> getAssociatedType() {
return TYPE;
}

}
