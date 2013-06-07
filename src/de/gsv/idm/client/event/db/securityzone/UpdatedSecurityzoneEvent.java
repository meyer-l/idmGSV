package de.gsv.idm.client.event.db.securityzone;

import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.ObjectEvent;
import de.gsv.idm.shared.dto.SecurityzoneDTO;

public class UpdatedSecurityzoneEvent extends
        ObjectEvent<SecurityzoneDTO, UpdatedSecurityzoneEvent> {
	public static Type<GeneralEventHandler<UpdatedSecurityzoneEvent>> TYPE = new Type<GeneralEventHandler<UpdatedSecurityzoneEvent>>();

	public UpdatedSecurityzoneEvent(SecurityzoneDTO dbObject) {
		super(dbObject);
	}

	@Override
	public Type<GeneralEventHandler<UpdatedSecurityzoneEvent>> getAssociatedType() {
		return TYPE;
	}
}