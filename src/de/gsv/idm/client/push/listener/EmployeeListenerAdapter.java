package de.gsv.idm.client.push.listener;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.event.db.employee.CreatedEmployeeEvent;
import de.gsv.idm.client.event.db.employee.DeletedEmployeeEvent;
import de.gsv.idm.client.event.db.employee.UpdatedEmployeeEvent;
import de.gsv.idm.client.push.CUDListenerAdapter;
import de.gsv.idm.shared.dto.EmployeeDTO;
import de.gsv.idm.shared.push.event.employee.EmployeePushEvent;

public class EmployeeListenerAdapter extends CUDListenerAdapter<EmployeePushEvent, EmployeeDTO> {

	private static EmployeeListenerAdapter instance;

	static public EmployeeListenerAdapter getInstance() {
		if (instance == null) {
			instance = new EmployeeListenerAdapter();
		}
		return instance;
	}
	
	public EmployeeListenerAdapter() {
		super(DBController.getInstance().getEmployeeController());
	}

	@Override
	public void fireCreatedEvent(EmployeePushEvent pushEvent) {
		eventBus.fireEvent(new CreatedEmployeeEvent(pushEvent.getObject()));
	}

	@Override
	public void fireDeletedEvent(EmployeePushEvent pushEvent) {
		eventBus.fireEvent(new DeletedEmployeeEvent(pushEvent.getObject()));
	}

	@Override
	public void fireUpdatedEvent(EmployeeDTO pushEvent) {
		eventBus.fireEvent(new UpdatedEmployeeEvent(pushEvent));
	}

}
