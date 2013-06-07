package de.gsv.idm.client.view.properties;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;

import de.gsv.idm.shared.dto.GeneralDTO;

public interface GeneralDTOProperties extends HasKeyAndName<GeneralDTO<?>> {
	
	ModelKeyProvider<GeneralDTO<?>> treeKey();
	ValueProvider<GeneralDTO<?>,String> treeToListLabel();
}
