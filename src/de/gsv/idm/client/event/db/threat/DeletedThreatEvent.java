package de.gsv.idm.client.event.db.threat;

import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.ObjectEvent;
import de.gsv.idm.shared.dto.ThreatDTO;

public class DeletedThreatEvent extends ObjectEvent<ThreatDTO,DeletedThreatEvent> {
	public static Type<GeneralEventHandler<DeletedThreatEvent>> TYPE = new Type<GeneralEventHandler<DeletedThreatEvent>>();
	public DeletedThreatEvent(ThreatDTO dbObject) {
	    super(dbObject);
    }

	@Override
    public Type<GeneralEventHandler<DeletedThreatEvent>> getAssociatedType() {
	   return TYPE;
    }

}
