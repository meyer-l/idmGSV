package de.gsv.idm.client.services;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.gsv.idm.shared.dto.OccupationDTO;

@RemoteServiceRelativePath("occupationService")
public interface OccupationService extends GeneralDomainService<OccupationDTO> {
}
