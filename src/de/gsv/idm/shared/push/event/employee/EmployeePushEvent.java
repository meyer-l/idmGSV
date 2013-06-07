package de.gsv.idm.shared.push.event.employee;

import de.gsv.idm.shared.dto.EmployeeDTO;
import de.gsv.idm.shared.push.event.PushEvent;

@SuppressWarnings("serial")
public abstract class EmployeePushEvent extends PushEvent<EmployeePushEvent, EmployeeDTO> {
	public static final String CONVERSATION_DOMAIN = "employee_pushDomain";
	
	EmployeePushEvent(){
		
	}
	
	EmployeePushEvent(EmployeeDTO employee){
		super(employee);
	}
	
	public String getConservationDomain() {
        return CONVERSATION_DOMAIN;
    }
	
}	
