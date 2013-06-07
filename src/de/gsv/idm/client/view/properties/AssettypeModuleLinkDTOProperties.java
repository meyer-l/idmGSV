package de.gsv.idm.client.view.properties;

import com.sencha.gxt.core.client.ValueProvider;

import de.gsv.idm.shared.dto.AssettypeModuleLinkDTO;
import de.gsv.idm.shared.dto.EmployeeDTO;

public interface AssettypeModuleLinkDTOProperties extends HasKeyAndName<AssettypeModuleLinkDTO> {
	ValueProvider<AssettypeModuleLinkDTO, EmployeeDTO> responsiblePerson();
}
