package de.gsv.idm.client.services;

import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.gsv.idm.shared.dto.ThreatDTO;

@RemoteServiceRelativePath("threatService")
public interface ThreatService extends GeneralService<ThreatDTO> {

}
