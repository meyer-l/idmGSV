package de.gsv.idm.client.view.properties;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;

import de.gsv.idm.shared.dto.DomainDTO;

public interface DomainDTOProperties extends HasKeyAndName<DomainDTO>{	
	   	
	@Path("name")
	LabelProvider<DomainDTO> nameLabel();
	ValueProvider<DomainDTO, String> ident();
}
