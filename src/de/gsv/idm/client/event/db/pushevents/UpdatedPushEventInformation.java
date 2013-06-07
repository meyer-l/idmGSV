package de.gsv.idm.client.event.db.pushevents;

import de.gsv.idm.shared.dto.GeneralDTO;

public class UpdatedPushEventInformation extends PushEventInformation {

	public UpdatedPushEventInformation(GeneralDTO<?> event) {
		super(event);
	}

	@Override
	public String getText() {
		return getObject().getClassName() + " (ID: " + getObject().getId() + ") wurde aktualisiert";
	}

}
