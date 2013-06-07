package de.gsv.idm.client.services;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.gsv.idm.shared.dto.AssetDTO;
import de.gsv.idm.shared.dto.AssetLinkDTO;
import de.gsv.idm.shared.dto.AssettypeDTO;

public interface AssetLinkServiceAsync extends GeneralDomainServiceAsync<AssetLinkDTO> {
	public void create(AssetDTO model1, AssetDTO model2, String sessionId,
	        AsyncCallback<List<AssetLinkDTO>> callback);

	public void create(AssettypeDTO asset, AssetDTO model, String sessionId,
	        AsyncCallback<List<AssetLinkDTO>> callback);

}
