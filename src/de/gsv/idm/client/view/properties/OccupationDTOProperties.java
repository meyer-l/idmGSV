package de.gsv.idm.client.view.properties;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;

import de.gsv.idm.shared.dto.OccupationDTO;

public interface OccupationDTOProperties extends HasKeyAndName<OccupationDTO> {
	
	@Path("name")
	LabelProvider<OccupationDTO> nameLabel();
	
	ValueProvider<OccupationDTO, String> ident();
	ValueProvider<OccupationDTO, String> calculatedAvailabilityName();
	ValueProvider<OccupationDTO, String> calculatedConfidentialityName();
	ValueProvider<OccupationDTO, String> calculatedIntegrityName();

	ValueProvider<OccupationDTO, String> assignedSecurityzoneName();
}