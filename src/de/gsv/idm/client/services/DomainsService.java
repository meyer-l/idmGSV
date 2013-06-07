package de.gsv.idm.client.services;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.gsv.idm.shared.dto.DomainDTO;

@RemoteServiceRelativePath("domainsService")
public interface DomainsService extends GeneralService<DomainDTO> {
	
}
