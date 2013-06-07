package de.gsv.idm.client.event.db.pushevents;

import de.gsv.idm.shared.dto.GeneralDTO;

public class CreatedPushEventInformation extends PushEventInformation {

	public CreatedPushEventInformation(GeneralDTO<?> v) {
	    super(v);
    }
	
	@Override
    public String getText() {
	    return getObject().getClassName() + " (ID: " + getObject().getId() + ") wurde erstellt";
    }

}
