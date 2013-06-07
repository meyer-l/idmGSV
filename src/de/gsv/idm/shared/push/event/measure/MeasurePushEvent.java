package de.gsv.idm.shared.push.event.measure;

import de.gsv.idm.shared.dto.MeasureDTO;
import de.gsv.idm.shared.push.event.PushEvent;

@SuppressWarnings("serial")
public abstract class MeasurePushEvent extends PushEvent<MeasurePushEvent, MeasureDTO> {
	public static final String CONVERSATION_DOMAIN = "measure_pushDomain";

	MeasurePushEvent() {

	}

	MeasurePushEvent(MeasureDTO measure) {
		super(measure);
	}

	public String getConservationDomain() {
		return CONVERSATION_DOMAIN;
	}
}
