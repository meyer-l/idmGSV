package de.gsv.idm.client.RpcController.service;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.RpcController.general.GeneralDomainRpcController;
import de.gsv.idm.client.services.EmployeeService;
import de.gsv.idm.client.services.EmployeeServiceAsync;
import de.gsv.idm.shared.dto.AssetDTO;
import de.gsv.idm.shared.dto.EmployeeDTO;
import de.gsv.idm.shared.dto.GeneralDTO;
import de.gsv.idm.shared.dto.InformationDTO;
import de.gsv.idm.shared.dto.OccupationDTO;

public class EmployeeRpcController extends GeneralDomainRpcController<EmployeeDTO> implements
        EmployeeServiceAsync {

	public EmployeeRpcController(DBController communicationController) {
		super((EmployeeServiceAsync) GWT.create(EmployeeService.class), communicationController);
	}

	@Override
    public EmployeeDTO fixAssociations(EmployeeDTO toPut) {
		List<OccupationDTO> clonedOccupations = new ArrayList<OccupationDTO>();
		for (OccupationDTO occupation : toPut.getOccupations()) {
			clonedOccupations.add(dbConnector.getOccupationController().getFromMap(
			        occupation));
		}
		toPut.setOccupations(clonedOccupations);
		
		List<InformationDTO> clonedInformation = new ArrayList<InformationDTO>();
		for (InformationDTO information : toPut.getInformations()) {
			clonedInformation.add(dbConnector.getInformationController().getFromMap(
			        information));
		}
		toPut.setInformations(clonedInformation);
		
		List<AssetDTO> clonedAssetmodel = new ArrayList<AssetDTO>();
		for (AssetDTO assetmodel : toPut.getAssetmodels()) {
			clonedAssetmodel.add(dbConnector.getAssetController().getFromMap(
			        assetmodel));
		}
		toPut.setAssetmodels(clonedAssetmodel);		
		
	    return toPut;
    }

	@Override
    protected EmployeeDTO handleGeneralObject(GeneralDTO<?> updatedItem) {
		return putInMap((EmployeeDTO) updatedItem);
    }

}
