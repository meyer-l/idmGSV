package de.gsv.idm.client.view.properties;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.data.shared.ModelKeyProvider;

import de.gsv.idm.shared.dto.AssettypeCategoryDTO;
import de.gsv.idm.shared.dto.GeneralDTO;

public interface AssettypeCategoryDTOProperties extends HasKeyAndName<AssettypeCategoryDTO> {
	@Path("name")
	ModelKeyProvider<GeneralDTO<?>> comboBoxKey();
}
