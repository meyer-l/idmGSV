package de.gsv.idm.client.view.properties;

import com.sencha.gxt.core.client.ValueProvider;

import de.gsv.idm.shared.dto.SecurityzoneDTO;

public interface SecurityzoneDTOProperties extends HasKeyAndName<SecurityzoneDTO>{

	ValueProvider<SecurityzoneDTO, Integer> orderNumber();
}
