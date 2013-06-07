package de.gsv.idm.client.RpcController.service;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.RpcController.general.GeneralDomainRpcController;
import de.gsv.idm.client.services.OccupationService;
import de.gsv.idm.client.services.OccupationServiceAsync;
import de.gsv.idm.shared.dto.GeneralDTO;
import de.gsv.idm.shared.dto.InformationDTO;
import de.gsv.idm.shared.dto.OccupationDTO;

public class OccupationRpcController extends GeneralDomainRpcController<OccupationDTO> implements
        OccupationServiceAsync {

	public OccupationRpcController(DBController communicationController) {
		super((OccupationServiceAsync) GWT.create(OccupationService.class), communicationController);
	}

	@Override
	public OccupationDTO fixAssociations(OccupationDTO toPut) {
		List<InformationDTO> clonedInformation = new ArrayList<InformationDTO>();
		for (InformationDTO information : toPut.getInformations()) {
			clonedInformation.add(dbConnector.getInformationController().getFromMap(
			        information));
		}
		toPut.setInformations(clonedInformation);

		return toPut;
	}

	@Override
    protected OccupationDTO handleGeneralObject(GeneralDTO<?> updatedItem) {
		return putInMap((OccupationDTO) updatedItem);
    }
}
