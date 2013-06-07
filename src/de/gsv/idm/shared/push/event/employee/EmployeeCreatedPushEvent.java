package de.gsv.idm.shared.push.event.employee;

import de.gsv.idm.client.push.CUDListener;
import de.gsv.idm.shared.dto.EmployeeDTO;

@SuppressWarnings("serial")
public class EmployeeCreatedPushEvent extends EmployeePushEvent {
	public EmployeeCreatedPushEvent(){

	}
	public EmployeeCreatedPushEvent(EmployeeDTO employee){
		super(employee);
	}
	@Override
	public void call(CUDListener<EmployeePushEvent> listener) {
		listener.onCreated(this);
	}
}
