package de.gsv.idm.client.view.properties;

import com.sencha.gxt.core.client.ValueProvider;

import de.gsv.idm.shared.dto.AssetModuleLinkDTO;
import de.gsv.idm.shared.dto.EmployeeDTO;

public interface AssetAssettypeModuleLinkDTOProperties extends
        HasKeyAndName<AssetModuleLinkDTO> {
	ValueProvider<AssetModuleLinkDTO, EmployeeDTO> responsiblePerson();
}
