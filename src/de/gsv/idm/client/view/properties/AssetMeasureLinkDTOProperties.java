package de.gsv.idm.client.view.properties;

import com.sencha.gxt.core.client.ValueProvider;

import de.gsv.idm.shared.dto.AssetMeasureLinkDTO;
import de.gsv.idm.shared.dto.EmployeeDTO;

public interface AssetMeasureLinkDTOProperties extends
        HasKeyAndName<AssetMeasureLinkDTO> {
	ValueProvider<AssetMeasureLinkDTO, EmployeeDTO> responsiblePerson();

	ValueProvider<AssetMeasureLinkDTO, String> status();

	ValueProvider<AssetMeasureLinkDTO, String> cost();
	ValueProvider<AssetMeasureLinkDTO, String> category();
}
