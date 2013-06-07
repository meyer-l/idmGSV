package de.gsv.idm.client.RpcController.service;

import com.google.gwt.core.client.GWT;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.RpcController.general.GeneralRpcController;
import de.gsv.idm.client.services.AssettypeCategoryService;
import de.gsv.idm.client.services.AssettypeCategoryServiceAsync;
import de.gsv.idm.shared.dto.AssettypeCategoryDTO;
import de.gsv.idm.shared.dto.GeneralDTO;

public class AssettypeCategoryRpcController extends GeneralRpcController<AssettypeCategoryDTO> implements
        AssettypeCategoryServiceAsync {

	public AssettypeCategoryRpcController(DBController communicationController) {
		super((AssettypeCategoryServiceAsync) GWT.create(AssettypeCategoryService.class),
		        communicationController);
	}

	@Override
	public AssettypeCategoryDTO fixAssociations(AssettypeCategoryDTO toPut) {
		return toPut;
	}

	@Override
    protected AssettypeCategoryDTO handleGeneralObject(GeneralDTO<?> updatedItem) {
		return putInMap((AssettypeCategoryDTO) updatedItem);
    }

}
