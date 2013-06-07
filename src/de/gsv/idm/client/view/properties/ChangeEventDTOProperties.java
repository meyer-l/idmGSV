package de.gsv.idm.client.view.properties;

import java.util.Date;

import com.sencha.gxt.core.client.ValueProvider;

import de.gsv.idm.shared.dto.ChangeEventDTO;

public interface ChangeEventDTOProperties extends HasKeyAndName<ChangeEventDTO>{	

	ValueProvider<ChangeEventDTO, String> employeeName();
	ValueProvider<ChangeEventDTO, String> oldValue();
	ValueProvider<ChangeEventDTO, String> newValue();
	ValueProvider<ChangeEventDTO, Date> date();
	ValueProvider<ChangeEventDTO, ChangeEventDTO> instance();
	
}
