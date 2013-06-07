package de.gsv.idm.client.event.db.threat;

import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.ObjectEvent;
import de.gsv.idm.shared.dto.ThreatDTO;

public class UpdatedThreatEvent extends ObjectEvent<ThreatDTO,UpdatedThreatEvent> {
	public static Type<GeneralEventHandler<UpdatedThreatEvent>> TYPE = new Type<GeneralEventHandler<UpdatedThreatEvent>>();
	public UpdatedThreatEvent(ThreatDTO dbObject) {
	    super(dbObject);
    }

	@Override
    public Type<GeneralEventHandler<UpdatedThreatEvent>> getAssociatedType() {
	   return TYPE;
    }

}
