package de.gsv.idm.client.push.listener;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.event.db.measure.CreatedMeasureEvent;
import de.gsv.idm.client.event.db.measure.DeletedMeasureEvent;
import de.gsv.idm.client.event.db.measure.UpdatedMeasureEvent;
import de.gsv.idm.client.push.CUDListenerAdapter;
import de.gsv.idm.shared.dto.MeasureDTO;
import de.gsv.idm.shared.push.event.measure.MeasurePushEvent;

public class MeasureListenerAdapter extends CUDListenerAdapter<MeasurePushEvent, MeasureDTO> {

	private static MeasureListenerAdapter instance;

	static public MeasureListenerAdapter getInstance() {
		if (instance == null) {
			instance = new MeasureListenerAdapter();
		}
		return instance;
	}
	
	public MeasureListenerAdapter() {
		super(DBController.getInstance().getMeasureController());
	}

	@Override
	public void fireCreatedEvent(MeasurePushEvent createdEvent) {
		eventBus.fireEvent(new CreatedMeasureEvent(createdEvent.getObject()));
	}

	@Override
	public void fireDeletedEvent(MeasurePushEvent deletedEvent) {
		eventBus.fireEvent(new DeletedMeasureEvent(deletedEvent.getObject()));
	}

	@Override
	public void fireUpdatedEvent(MeasureDTO updatedEvent) {
		eventBus.fireEvent(new UpdatedMeasureEvent(updatedEvent));

	}

}
