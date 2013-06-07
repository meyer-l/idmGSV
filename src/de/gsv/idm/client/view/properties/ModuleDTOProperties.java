package de.gsv.idm.client.view.properties;

import com.sencha.gxt.core.client.ValueProvider;

import de.gsv.idm.shared.dto.ModuleDTO;

public interface ModuleDTOProperties  extends HasKeyAndName<ModuleDTO>{
	ValueProvider<ModuleDTO, String> version();
}
