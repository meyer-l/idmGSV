package de.gsv.idm.client.services;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.gsv.idm.shared.dto.SecurityLevelChangeDTO;

public interface SecurityLevelChangeServiceAsync extends
        GeneralDomainServiceAsync<SecurityLevelChangeDTO> {
	public void saveEditableList(Integer domain_id,
	        List<SecurityLevelChangeDTO> changeEventsToSave, String sessionId,
	        AsyncCallback<Boolean> callback);
}
