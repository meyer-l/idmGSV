package de.gsv.idm.client.event.db.employee;

import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.ObjectEvent;
import de.gsv.idm.shared.dto.EmployeeDTO;

public class DeletedEmployeeEvent extends
		ObjectEvent<EmployeeDTO, DeletedEmployeeEvent> {
	public static Type<GeneralEventHandler<DeletedEmployeeEvent>> TYPE = new Type<GeneralEventHandler<DeletedEmployeeEvent>>();

	public DeletedEmployeeEvent(EmployeeDTO object) {
		super(object);
	}

	@Override
	public Type<GeneralEventHandler<DeletedEmployeeEvent>> getAssociatedType() {
		return TYPE;
	}

}
