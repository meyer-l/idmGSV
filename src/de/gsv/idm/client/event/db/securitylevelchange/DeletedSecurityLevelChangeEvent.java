package de.gsv.idm.client.event.db.securitylevelchange;

import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.ObjectEvent;
import de.gsv.idm.shared.dto.SecurityLevelChangeDTO;

public class DeletedSecurityLevelChangeEvent extends
        ObjectEvent<SecurityLevelChangeDTO, DeletedSecurityLevelChangeEvent> {

	public static Type<GeneralEventHandler<DeletedSecurityLevelChangeEvent>> TYPE = new Type<GeneralEventHandler<DeletedSecurityLevelChangeEvent>>();

	public DeletedSecurityLevelChangeEvent(SecurityLevelChangeDTO object) {
		super(object);
	}

	@Override
	public Type<GeneralEventHandler<DeletedSecurityLevelChangeEvent>> getAssociatedType() {
		return TYPE;
	}

}
