package de.gsv.idm.client.event.db.employee;

import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.ObjectEvent;
import de.gsv.idm.shared.dto.EmployeeDTO;

public class CreatedEmployeeEvent extends
		ObjectEvent<EmployeeDTO, CreatedEmployeeEvent> {
	public static Type<GeneralEventHandler<CreatedEmployeeEvent>> TYPE = new Type<GeneralEventHandler<CreatedEmployeeEvent>>();

	public CreatedEmployeeEvent(EmployeeDTO object) {
		super(object);
	}

	@Override
	public Type<GeneralEventHandler<CreatedEmployeeEvent>> getAssociatedType() {
		return TYPE;
	}

}
