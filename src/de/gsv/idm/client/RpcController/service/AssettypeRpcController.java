package de.gsv.idm.client.RpcController.service;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.RpcController.general.GeneralDomainRpcController;
import de.gsv.idm.client.services.AssettypeService;
import de.gsv.idm.client.services.AssettypeServiceAsync;
import de.gsv.idm.shared.dto.AssetDTO;
import de.gsv.idm.shared.dto.AssettypeDTO;
import de.gsv.idm.shared.dto.AssettypeMeasureLinkDTO;
import de.gsv.idm.shared.dto.AssettypeModuleLinkDTO;
import de.gsv.idm.shared.dto.GeneralDTO;

public class AssettypeRpcController extends GeneralDomainRpcController<AssettypeDTO> implements
        AssettypeServiceAsync {

	public AssettypeRpcController(DBController communicationController) {
		super((AssettypeServiceAsync) GWT.create(AssettypeService.class), communicationController);
	}

	@Override
	public AssettypeDTO fixAssociations(AssettypeDTO toPut) {

		toPut.setCategory(dbConnector.getAssettypeCategoryController().getFromMap(toPut.getCategory()));

		toPut.setResponsiblePerson(dbConnector.getEmployeeController().getFromMap(
		        toPut.getResponsiblePerson()));

		List<AssettypeModuleLinkDTO> clonedModules = new ArrayList<AssettypeModuleLinkDTO>();
		for (AssettypeModuleLinkDTO moduleLink : toPut.getModules()) {
			clonedModules.add(dbConnector.getAssettypeModuleLinkController().putInMap(moduleLink));
		}
		toPut.setModules(clonedModules);

		List<AssettypeMeasureLinkDTO> clonedMeasures = new ArrayList<AssettypeMeasureLinkDTO>();
		for (AssettypeMeasureLinkDTO measureLink : toPut.getMeasures()) {
			clonedMeasures.add(dbConnector.getAssettypeMeasureLinkController().putInMap(measureLink));
		}
		toPut.setMeasures(clonedMeasures);

		List<AssetDTO> clonedModels = new ArrayList<AssetDTO>();
		for (AssetDTO linkedModel : toPut.getLinkedAssets()) {
			clonedModels.add(dbConnector.getAssetController().getFromMap(linkedModel));
		}
		toPut.setLinkedAssets(clonedModels);

		return toPut;
	}

	public void removeFromMap(AssettypeDTO toRemove) {
		super.removeFromMap(toRemove);
		for (AssettypeMeasureLinkDTO measureLink : toRemove.getMeasures()) {
			dbConnector.getAssettypeMeasureLinkController().removeFromMap(measureLink);
		}
		for (AssettypeModuleLinkDTO moduleLink : toRemove.getModules()) {
			dbConnector.getAssettypeModuleLinkController().removeFromMap(moduleLink);
		}
	}

	@Override
    protected AssettypeDTO handleGeneralObject(GeneralDTO<?> updatedItem) {
		return putInMap((AssettypeDTO) updatedItem);
    }

}
