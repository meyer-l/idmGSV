package de.gsv.idm.client.services;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.gsv.idm.shared.dto.AssetDTO;
import de.gsv.idm.shared.dto.AssetLinkDTO;
import de.gsv.idm.shared.dto.AssettypeDTO;

@RemoteServiceRelativePath("assetLinkService")
public interface AssetLinkService extends GeneralDomainService<AssetLinkDTO> {
	List<AssetLinkDTO> create(AssetDTO model1, AssetDTO model2, String sessionId);
	List<AssetLinkDTO> create(AssettypeDTO asset, AssetDTO model, String sessionId);
} 
