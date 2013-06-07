package de.gsv.idm.client.event.db.securityzone;

import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.ObjectEvent;
import de.gsv.idm.shared.dto.SecurityzoneDTO;

public class DeletedSecurityzoneEvent extends
        ObjectEvent<SecurityzoneDTO, DeletedSecurityzoneEvent> {
	public static Type<GeneralEventHandler<DeletedSecurityzoneEvent>> TYPE = new Type<GeneralEventHandler<DeletedSecurityzoneEvent>>();

	public DeletedSecurityzoneEvent(SecurityzoneDTO dbObject) {
		super(dbObject);
	}

	@Override
	public Type<GeneralEventHandler<DeletedSecurityzoneEvent>> getAssociatedType() {
		return TYPE;
	}
}