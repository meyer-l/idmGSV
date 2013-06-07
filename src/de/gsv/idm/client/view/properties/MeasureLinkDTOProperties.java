package de.gsv.idm.client.view.properties;

import com.sencha.gxt.core.client.ValueProvider;

import de.gsv.idm.shared.dto.MeasureDTO;
import de.gsv.idm.shared.dto.MeasureLinkDTO;

public interface MeasureLinkDTOProperties extends HasKeyAndName<MeasureLinkDTO> {
	
	ValueProvider<MeasureLinkDTO, MeasureDTO> measure();	
	ValueProvider<MeasureLinkDTO, String> category();
}