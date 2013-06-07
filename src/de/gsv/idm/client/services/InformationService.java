package de.gsv.idm.client.services;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.gsv.idm.shared.dto.InformationDTO;

@RemoteServiceRelativePath("informationService")
public interface InformationService extends GeneralDomainService<InformationDTO> {
}
