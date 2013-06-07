package de.gsv.idm.client.event.db.measure;

import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.ObjectEvent;
import de.gsv.idm.shared.dto.MeasureDTO;

public class CreatedMeasureEvent extends ObjectEvent<MeasureDTO,CreatedMeasureEvent> {
	public static Type<GeneralEventHandler<CreatedMeasureEvent>> TYPE = new Type<GeneralEventHandler<CreatedMeasureEvent>>();
	public CreatedMeasureEvent(MeasureDTO dbObject) {
	    super(dbObject);
    }

	@Override
    public Type<GeneralEventHandler<CreatedMeasureEvent>> getAssociatedType() {
	   return TYPE;
    }

}
