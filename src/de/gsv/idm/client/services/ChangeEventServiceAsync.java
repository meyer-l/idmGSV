package de.gsv.idm.client.services;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.gsv.idm.shared.dto.ChangeAppliedDTO;
import de.gsv.idm.shared.dto.ChangeEventDTO;

public interface ChangeEventServiceAsync extends GeneralDomainServiceAsync<ChangeEventDTO> {
	public void getAllNotProcessed(Integer domain_id, String sessionId,
	        AsyncCallback<List<ChangeEventDTO>> callback);

	public void getAllManual(Integer domain_id, String sessionId,
	        AsyncCallback<List<ChangeEventDTO>> callback);

	public void getAllNotNeeded(Integer domain_id, String sessionId,
	        AsyncCallback<List<ChangeEventDTO>> callback);

	public void getAllApplied(Integer domain_id, String sessionId,
	        AsyncCallback<List<ChangeEventDTO>> callback);

	public void saveEditableList(Integer domain_id, List<ChangeEventDTO> changeEventsToSave,
	        String sessionId, AsyncCallback<List<ChangeAppliedDTO>> callback);
}
