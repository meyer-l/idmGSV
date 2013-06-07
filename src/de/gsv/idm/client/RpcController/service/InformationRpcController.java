package de.gsv.idm.client.RpcController.service;

import com.google.gwt.core.client.GWT;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.RpcController.general.GeneralDomainRpcController;
import de.gsv.idm.client.services.InformationService;
import de.gsv.idm.client.services.InformationServiceAsync;
import de.gsv.idm.shared.dto.GeneralDTO;
import de.gsv.idm.shared.dto.InformationDTO;

public class InformationRpcController extends GeneralDomainRpcController<InformationDTO> implements
        InformationServiceAsync {

	public InformationRpcController(DBController communicationController) {
		super((InformationServiceAsync) GWT.create(InformationService.class),
		        communicationController);
	}

	@Override
	public InformationDTO fixAssociations(InformationDTO toPut) {
		toPut.setSecurityzone(dbConnector.getSecurityzoneController()
		        .getFromMap(toPut.getSecurityzone()));
		
		return toPut;
	}

	@Override
    protected InformationDTO handleGeneralObject(GeneralDTO<?> updatedItem) {
		return putInMap((InformationDTO) updatedItem);
    }

}
