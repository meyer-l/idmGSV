package de.gsv.idm.client.view.properties;

import com.sencha.gxt.core.client.ValueProvider;

import de.gsv.idm.shared.dto.ChangeAppliedDTO;

public interface ChangeAppliedDTOProperties extends HasKeyAndName<ChangeAppliedDTO> {

	ValueProvider<ChangeAppliedDTO,String> objectClassName();
	ValueProvider<ChangeAppliedDTO,String> statusLabel();
}
