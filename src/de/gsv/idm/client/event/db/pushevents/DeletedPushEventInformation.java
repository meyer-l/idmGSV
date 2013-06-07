package de.gsv.idm.client.event.db.pushevents;

import de.gsv.idm.shared.dto.GeneralDTO;

public class DeletedPushEventInformation extends PushEventInformation {

	public DeletedPushEventInformation(GeneralDTO<?> event) {
	    super(event);
    }

	@Override
    public String getText() {
	    return getObject().getClassName() + " (ID: " + getObject().getId() + ") wurde gelöscht";
    }

}
