package de.gsv.idm.client.view.properties;

import com.sencha.gxt.core.client.ValueProvider;

import de.gsv.idm.shared.dto.AssetDTO;
import de.gsv.idm.shared.dto.AssettypeDTO;
import de.gsv.idm.shared.dto.SecurityLevelDTO;

public interface AssetDTOProperties extends HasKeyAndName<AssetDTO> {

	ValueProvider<AssetDTO, SecurityLevelDTO> calculatedSecurityAssesment();
	ValueProvider<AssetDTO, SecurityLevelDTO> calculatedAvailability();
	ValueProvider<AssetDTO, SecurityLevelDTO> calculatedConfidentiality();
	ValueProvider<AssetDTO, SecurityLevelDTO> calculatedIntegrity();
	ValueProvider<AssetDTO, AssettypeDTO> assettype();
}
