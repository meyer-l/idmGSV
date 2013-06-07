package de.gsv.idm.client.view.properties;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;

import de.gsv.idm.shared.dto.InformationDTO;

public interface InformationDTOProperties extends HasKeyAndName<InformationDTO> {
		   
	@Path("name")
	LabelProvider<InformationDTO> nameLabel();
	ValueProvider<InformationDTO, String> ident();
	ValueProvider<InformationDTO, String> availabilityName();
	ValueProvider<InformationDTO, String> confidentialityName();
	ValueProvider<InformationDTO, String> integrityName();
	ValueProvider<InformationDTO, String> securityzoneName();
}
