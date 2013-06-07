package de.gsv.idm.shared.push.event.employee;

import de.gsv.idm.client.push.CUDListener;
import de.gsv.idm.shared.dto.EmployeeDTO;

@SuppressWarnings("serial")
public class EmployeeUpdatedPushEvent extends EmployeePushEvent {
	public EmployeeUpdatedPushEvent(){

	}
	public EmployeeUpdatedPushEvent(EmployeeDTO employee){
		super(employee);
	}
	@Override
	public void call(CUDListener<EmployeePushEvent> listener) {
		listener.onUpdated(this);
	}
}
