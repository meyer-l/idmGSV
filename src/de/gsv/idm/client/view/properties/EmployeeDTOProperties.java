package de.gsv.idm.client.view.properties;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;

import de.gsv.idm.shared.dto.EmployeeDTO;

public interface EmployeeDTOProperties extends HasKeyAndName<EmployeeDTO>{  
	
	@Path("label")
	LabelProvider<EmployeeDTO> fullNameLabel();
	ValueProvider<EmployeeDTO, String> surname();
	ValueProvider<EmployeeDTO, String> assignedSecurityzoneName();
	ValueProvider<EmployeeDTO, String> calculatedSecurityAssesmentName();
}
