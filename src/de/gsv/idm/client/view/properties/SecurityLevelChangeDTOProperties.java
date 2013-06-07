package de.gsv.idm.client.view.properties;

import com.sencha.gxt.core.client.ValueProvider;

import de.gsv.idm.shared.dto.SecurityLevelChangeDTO;
import de.gsv.idm.shared.dto.SecurityLevelDTO;

public interface SecurityLevelChangeDTOProperties extends HasKeyAndName<SecurityLevelChangeDTO> {

	ValueProvider<SecurityLevelChangeDTO, Boolean> reviewed();
	ValueProvider<SecurityLevelChangeDTO, SecurityLevelChangeDTO> instance();
	ValueProvider<SecurityLevelChangeDTO, SecurityLevelDTO> securityAssesment();
	ValueProvider<SecurityLevelChangeDTO, SecurityLevelDTO> availability();
	ValueProvider<SecurityLevelChangeDTO, SecurityLevelDTO> confidentiality();
	ValueProvider<SecurityLevelChangeDTO, SecurityLevelDTO> integrity();
	ValueProvider<SecurityLevelChangeDTO, String> securityAssesmentName();
	ValueProvider<SecurityLevelChangeDTO, String> availabilityName();
	ValueProvider<SecurityLevelChangeDTO, String> confidentialityName();
	ValueProvider<SecurityLevelChangeDTO, String> integrityName();

}
