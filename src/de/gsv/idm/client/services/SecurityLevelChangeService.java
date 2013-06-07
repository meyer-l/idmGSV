package de.gsv.idm.client.services;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.gsv.idm.shared.dto.SecurityLevelChangeDTO;

@RemoteServiceRelativePath("securityLevelChangeService")
public interface SecurityLevelChangeService extends GeneralDomainService<SecurityLevelChangeDTO> {
	Boolean saveEditableList(Integer domain_id, List<SecurityLevelChangeDTO> changeEventsToSave, String sessionId);
}
