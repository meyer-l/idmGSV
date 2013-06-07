package de.gsv.idm.client.event.db.securitylevelchange;

import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.ObjectEvent;
import de.gsv.idm.shared.dto.SecurityLevelChangeDTO;

public class UpdatedSecurityLevelChangeEvent extends
        ObjectEvent<SecurityLevelChangeDTO, UpdatedSecurityLevelChangeEvent> {

	public static Type<GeneralEventHandler<UpdatedSecurityLevelChangeEvent>> TYPE = new Type<GeneralEventHandler<UpdatedSecurityLevelChangeEvent>>();

	public UpdatedSecurityLevelChangeEvent(SecurityLevelChangeDTO object) {
		super(object);
	}

	@Override
	public Type<GeneralEventHandler<UpdatedSecurityLevelChangeEvent>> getAssociatedType() {
		return TYPE;
	}

}
