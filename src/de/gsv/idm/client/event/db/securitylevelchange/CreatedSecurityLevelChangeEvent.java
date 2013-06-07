package de.gsv.idm.client.event.db.securitylevelchange;

import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.ObjectEvent;
import de.gsv.idm.shared.dto.SecurityLevelChangeDTO;

public class CreatedSecurityLevelChangeEvent extends
        ObjectEvent<SecurityLevelChangeDTO, CreatedSecurityLevelChangeEvent> {

	public static Type<GeneralEventHandler<CreatedSecurityLevelChangeEvent>> TYPE = new Type<GeneralEventHandler<CreatedSecurityLevelChangeEvent>>();

	public CreatedSecurityLevelChangeEvent(SecurityLevelChangeDTO object) {
		super(object);
	}

	@Override
	public Type<GeneralEventHandler<CreatedSecurityLevelChangeEvent>> getAssociatedType() {
		return TYPE;
	}
}
