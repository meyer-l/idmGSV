package de.gsv.idm.client.RpcController.service;

import com.google.gwt.core.client.GWT;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.RpcController.general.GeneralRpcController;
import de.gsv.idm.client.services.DomainsService;
import de.gsv.idm.client.services.DomainsServiceAsync;
import de.gsv.idm.shared.dto.DomainDTO;
import de.gsv.idm.shared.dto.GeneralDTO;

public class DomainRpcController extends GeneralRpcController<DomainDTO> implements DomainsServiceAsync {


	

	public DomainRpcController(DBController communicationController) {
		super((DomainsServiceAsync) GWT.create(DomainsService.class), communicationController);		
	}

	@Override
    public DomainDTO fixAssociations(DomainDTO toPut) {
	    return toPut;
    }

	@Override
    protected DomainDTO handleGeneralObject(GeneralDTO<?> updatedItem) {
		return putInMap((DomainDTO) updatedItem);
    }
}
