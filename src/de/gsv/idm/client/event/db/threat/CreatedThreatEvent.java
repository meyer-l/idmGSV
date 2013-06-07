package de.gsv.idm.client.event.db.threat;

import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.ObjectEvent;
import de.gsv.idm.shared.dto.ThreatDTO;

public class CreatedThreatEvent extends ObjectEvent<ThreatDTO,CreatedThreatEvent> {
	public static Type<GeneralEventHandler<CreatedThreatEvent>> TYPE = new Type<GeneralEventHandler<CreatedThreatEvent>>();
	public CreatedThreatEvent(ThreatDTO dbObject) {
	    super(dbObject);
    }

	@Override
    public Type<GeneralEventHandler<CreatedThreatEvent>> getAssociatedType() {
	   return TYPE;
    }

}
