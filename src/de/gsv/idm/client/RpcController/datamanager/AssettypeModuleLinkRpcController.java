package de.gsv.idm.client.RpcController.datamanager;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.RpcController.general.GeneralRpcController;
import de.gsv.idm.shared.dto.AssettypeModuleLinkDTO;
import de.gsv.idm.shared.dto.GeneralDTO;

public class AssettypeModuleLinkRpcController extends GeneralRpcController<AssettypeModuleLinkDTO> {

	public AssettypeModuleLinkRpcController(
            DBController communicationController) {
	    super(null, communicationController);
    }

	@Override
	public AssettypeModuleLinkDTO fixAssociations(AssettypeModuleLinkDTO toPut) {
		toPut.setResponsiblePerson(dbConnector.getEmployeeController().getFromMap(
				toPut.getResponsiblePerson()));
		toPut.setModule(dbConnector.getModuleController().getFromMap(
				toPut.getModule()));
		
		return toPut;
	}

	@Override
    protected AssettypeModuleLinkDTO handleGeneralObject(GeneralDTO<?> updatedItem) {
		return putInMap((AssettypeModuleLinkDTO) updatedItem);
    }

}
