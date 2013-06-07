package de.gsv.idm.client.event.db.measure;

import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.ObjectEvent;
import de.gsv.idm.shared.dto.MeasureDTO;

public class UpdatedMeasureEvent extends ObjectEvent<MeasureDTO,UpdatedMeasureEvent> {
	public static Type<GeneralEventHandler<UpdatedMeasureEvent>> TYPE = new Type<GeneralEventHandler<UpdatedMeasureEvent>>();
	public UpdatedMeasureEvent(MeasureDTO dbObject) {
	    super(dbObject);
    }

	@Override
    public Type<GeneralEventHandler<UpdatedMeasureEvent>> getAssociatedType() {
	   return TYPE;
    }

}
