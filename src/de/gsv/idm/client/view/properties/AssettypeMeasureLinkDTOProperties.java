package de.gsv.idm.client.view.properties;

import com.sencha.gxt.core.client.ValueProvider;

import de.gsv.idm.shared.dto.AssettypeMeasureLinkDTO;
import de.gsv.idm.shared.dto.EmployeeDTO;

public interface AssettypeMeasureLinkDTOProperties extends HasKeyAndName<AssettypeMeasureLinkDTO> {
	ValueProvider<AssettypeMeasureLinkDTO, EmployeeDTO> responsiblePerson();
	ValueProvider<AssettypeMeasureLinkDTO, String> status();
	ValueProvider<AssettypeMeasureLinkDTO, String> cost();
	ValueProvider<AssettypeMeasureLinkDTO, Boolean> manualAdd();
	ValueProvider<AssettypeMeasureLinkDTO, Boolean> passOn();
	ValueProvider<AssettypeMeasureLinkDTO, String> category();
}