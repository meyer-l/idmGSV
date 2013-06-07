package de.gsv.idm.client.event.db.measure;

import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.ObjectEvent;
import de.gsv.idm.shared.dto.MeasureDTO;

public class DeletedMeasureEvent extends ObjectEvent<MeasureDTO,DeletedMeasureEvent> {
	public static Type<GeneralEventHandler<DeletedMeasureEvent>> TYPE = new Type<GeneralEventHandler<DeletedMeasureEvent>>();
	public DeletedMeasureEvent(MeasureDTO dbObject) {
	    super(dbObject);
    }

	@Override
    public Type<GeneralEventHandler<DeletedMeasureEvent>> getAssociatedType() {
	   return TYPE;
    }

}
