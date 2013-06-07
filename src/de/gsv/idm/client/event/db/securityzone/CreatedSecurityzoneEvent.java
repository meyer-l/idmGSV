package de.gsv.idm.client.event.db.securityzone;

import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.ObjectEvent;
import de.gsv.idm.shared.dto.SecurityzoneDTO;

public class CreatedSecurityzoneEvent extends
        ObjectEvent<SecurityzoneDTO, CreatedSecurityzoneEvent> {
	public static Type<GeneralEventHandler<CreatedSecurityzoneEvent>> TYPE = new Type<GeneralEventHandler<CreatedSecurityzoneEvent>>();

	public CreatedSecurityzoneEvent(SecurityzoneDTO dbObject) {
		super(dbObject);
	}

	@Override
	public Type<GeneralEventHandler<CreatedSecurityzoneEvent>> getAssociatedType() {
		return TYPE;
	}
}
