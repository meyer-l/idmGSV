package de.gsv.idm.client.event.db.pushevents;

import de.gsv.idm.shared.dto.GeneralDTO;

public abstract class PushEventInformation {

	private GeneralDTO<?> object;

	public PushEventInformation(GeneralDTO<?> object) {
		this.object = object;
	}

	public abstract String getText();

	public GeneralDTO<?> getObject() {
		return object;
	}
}
