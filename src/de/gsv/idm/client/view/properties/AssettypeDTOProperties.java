package de.gsv.idm.client.view.properties;

import com.sencha.gxt.core.client.ValueProvider;

import de.gsv.idm.shared.dto.AssettypeCategoryDTO;
import de.gsv.idm.shared.dto.AssettypeDTO;
import de.gsv.idm.shared.dto.EmployeeDTO;

public interface AssettypeDTOProperties extends HasKeyAndName<AssettypeDTO> {
	
	ValueProvider<AssettypeDTO, EmployeeDTO> responsiblePerson();
	ValueProvider<AssettypeDTO, AssettypeCategoryDTO> category();

}
