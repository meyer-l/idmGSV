package de.gsv.idm.client.services;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.gsv.idm.shared.dto.AssetDTO;

public interface AssetServiceAsync extends GeneralDomainServiceAsync<AssetDTO> {
	public void getTreeRoot(Integer domain_id, String sessionId, AsyncCallback<ArrayList<AssetDTO>> callback);
}
