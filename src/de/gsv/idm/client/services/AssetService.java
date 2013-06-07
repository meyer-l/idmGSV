package de.gsv.idm.client.services;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.gsv.idm.shared.dto.AssetDTO;

@RemoteServiceRelativePath("assetService")
public interface AssetService extends GeneralDomainService<AssetDTO> {
	ArrayList<AssetDTO> getTreeRoot(Integer domain_id, String sessionId);
}
