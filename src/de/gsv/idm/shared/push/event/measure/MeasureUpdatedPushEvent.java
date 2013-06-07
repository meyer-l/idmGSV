package de.gsv.idm.shared.push.event.measure;

import de.gsv.idm.client.push.CUDListener;
import de.gsv.idm.shared.dto.MeasureDTO;

@SuppressWarnings("serial")
public class MeasureUpdatedPushEvent extends MeasurePushEvent {
	
	public MeasureUpdatedPushEvent() {
	}
	
	public MeasureUpdatedPushEvent(MeasureDTO measure){
		super(measure);
	}
	public void call(CUDListener<MeasurePushEvent> listener) {
		listener.onUpdated(this);
	}

}
