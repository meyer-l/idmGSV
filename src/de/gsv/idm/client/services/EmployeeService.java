package de.gsv.idm.client.services;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.gsv.idm.shared.dto.EmployeeDTO;

@RemoteServiceRelativePath("employeeService")
public interface EmployeeService extends GeneralDomainService<EmployeeDTO> {
}
