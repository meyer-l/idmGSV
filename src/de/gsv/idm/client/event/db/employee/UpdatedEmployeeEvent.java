package de.gsv.idm.client.event.db.employee;

import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.ObjectEvent;
import de.gsv.idm.shared.dto.EmployeeDTO;

public class UpdatedEmployeeEvent extends
		ObjectEvent<EmployeeDTO, UpdatedEmployeeEvent> {
	public static Type<GeneralEventHandler<UpdatedEmployeeEvent>> TYPE = new Type<GeneralEventHandler<UpdatedEmployeeEvent>>();

	public UpdatedEmployeeEvent(EmployeeDTO object) {
		super(object);
	}

	@Override
	public Type<GeneralEventHandler<UpdatedEmployeeEvent>> getAssociatedType() {
		return TYPE;
	}

}
