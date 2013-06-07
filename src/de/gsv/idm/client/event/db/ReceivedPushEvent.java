package de.gsv.idm.client.event.db;

import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.pushevents.PushEventInformation;

public class ReceivedPushEvent extends ObjectEvent<PushEventInformation, ReceivedPushEvent> {

	public ReceivedPushEvent(PushEventInformation event) {
		super(event);
	}

	public static Type<GeneralEventHandler<ReceivedPushEvent>> TYPE = new Type<GeneralEventHandler<ReceivedPushEvent>>();

	@Override
	public Type<GeneralEventHandler<ReceivedPushEvent>> getAssociatedType() {
		return TYPE;
	}
}
