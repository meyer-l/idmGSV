package de.gsv.idm.server.service;

import java.util.List;

import de.gsv.idm.client.services.EmployeeService;
import de.gsv.idm.server.general.GeneralDomainServiceImpl;
import de.gsv.idm.shared.dto.EmployeeDTO;
import de.gsv.idm.shared.model.Employee;
import de.gsv.idm.shared.push.event.PushEvent;
import de.gsv.idm.shared.push.event.employee.EmployeeCreatedPushEvent;
import de.gsv.idm.shared.push.event.employee.EmployeeDeletedPushEvent;
import de.gsv.idm.shared.push.event.employee.EmployeePushEvent;
import de.gsv.idm.shared.push.event.employee.EmployeeUpdatedPushEvent;
import de.novanic.eventservice.client.event.domain.DomainFactory;

@SuppressWarnings("serial")
public class EmployeeServiceImpl extends GeneralDomainServiceImpl<EmployeeDTO, Employee> implements
        EmployeeService {

	public static final de.novanic.eventservice.client.event.domain.Domain CONVERSATION_DOMAIN;
	static {
		CONVERSATION_DOMAIN = DomainFactory.getDomain(EmployeePushEvent.CONVERSATION_DOMAIN);
	}

	@Override
	protected Class<Employee> getDBClass() {
		return Employee.class;
	}

	@Override
	protected List<Employee> findAll() {
		return Employee.findAll();
	}

	@Override
	protected Employee findById(Integer id) {
		return Employee.findById(id);
	}

	@Override
	protected Employee createObject() {
		return Employee.createIt();
	}

	@Override
	protected de.novanic.eventservice.client.event.domain.Domain getConversationDomain() {
		return CONVERSATION_DOMAIN;
	}

	@Override
	protected PushEvent<?, ?> getUpdatedPushEvent(EmployeeDTO updated) {
		return new EmployeeUpdatedPushEvent(updated);
	}

	@Override
	protected PushEvent<?, ?> getCreatedPushEvent(EmployeeDTO created) {
		return new EmployeeCreatedPushEvent(created);
	}

	@Override
	protected PushEvent<?, ?> getDeletedPushEvent(EmployeeDTO deleted) {
		return new EmployeeDeletedPushEvent(deleted);
	}

}
