package de.gsv.idm.client.services;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.gsv.idm.shared.dto.ChangeAppliedDTO;
import de.gsv.idm.shared.dto.ChangeEventDTO;

@RemoteServiceRelativePath("changeEventService")
public interface ChangeEventService extends GeneralDomainService<ChangeEventDTO> {
	List<ChangeEventDTO> getAllNotProcessed(Integer domain_id, String sessionId);
	List<ChangeEventDTO> getAllManual(Integer domain_id, String sessionId);
	List<ChangeEventDTO> getAllNotNeeded(Integer domain_id, String sessionId);
	List<ChangeEventDTO> getAllApplied(Integer domain_id, String sessionId);
	List<ChangeAppliedDTO> saveEditableList(Integer domain_id, List<ChangeEventDTO> changeEventsToSave, String sessionId);
}
