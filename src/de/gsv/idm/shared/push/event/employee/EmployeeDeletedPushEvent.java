package de.gsv.idm.shared.push.event.employee;

import de.gsv.idm.client.push.CUDListener;
import de.gsv.idm.shared.dto.EmployeeDTO;

@SuppressWarnings("serial")
public class EmployeeDeletedPushEvent extends EmployeePushEvent {
	public EmployeeDeletedPushEvent(){

	}
	public EmployeeDeletedPushEvent(EmployeeDTO employee){
		super(employee);
	}
	@Override
	public void call(CUDListener<EmployeePushEvent> listener) {
		listener.onDeleted(this);
	}
}
