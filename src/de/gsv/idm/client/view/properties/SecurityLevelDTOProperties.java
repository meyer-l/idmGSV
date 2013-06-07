package de.gsv.idm.client.view.properties;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;

import de.gsv.idm.shared.dto.SecurityLevelDTO;

public interface SecurityLevelDTOProperties extends HasKeyAndName<SecurityLevelDTO> {
	@Path("name")
	LabelProvider<SecurityLevelDTO> comboLabel();
	@Path("name")
	ValueProvider<SecurityLevelDTO,String> valueProviderLabel();
}
