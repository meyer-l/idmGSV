package de.gsv.idm.client.RpcController.datamanager;

import java.util.ArrayList;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.RpcController.general.GeneralRpcController;
import de.gsv.idm.shared.dto.AssettypeMeasureLinkDTO;
import de.gsv.idm.shared.dto.GeneralDTO;
import de.gsv.idm.shared.dto.ModuleDTO;
import de.gsv.idm.shared.dto.SecurityzoneDTO;

public class AssettypeMeasureLinkRpcController extends
        GeneralRpcController<AssettypeMeasureLinkDTO> {

	public AssettypeMeasureLinkRpcController(DBController communicationController) {
		super(null, communicationController);
	}

	@Override
	public AssettypeMeasureLinkDTO fixAssociations(AssettypeMeasureLinkDTO toPut) {
		toPut.setResponsiblePerson(dbConnector.getEmployeeController().getFromMap(
		        toPut.getResponsiblePerson()));
		toPut.setMeasure(dbConnector.getMeasureController().getFromMap(toPut.getMeasure()));

		ArrayList<ModuleDTO> clonedLinkedModules = new ArrayList<ModuleDTO>();
		for (ModuleDTO module : toPut.getLinkedModules()) {
			clonedLinkedModules.add(dbConnector.getModuleController().putInMap(module));
		}
		toPut.setLinkedModules(clonedLinkedModules);

		ArrayList<SecurityzoneDTO> clonedLinkedSecurityzones = new ArrayList<SecurityzoneDTO>();
		for (SecurityzoneDTO securityzone : toPut.getLinkedSecurityzones()) {
			clonedLinkedSecurityzones.add(dbConnector.getSecurityzoneController().putInMap(securityzone));
		}
		toPut.setLinkedSecurityzones(clonedLinkedSecurityzones);

		return toPut;
	}

	@Override
    protected AssettypeMeasureLinkDTO handleGeneralObject(GeneralDTO<?> updatedItem) {
		return putInMap((AssettypeMeasureLinkDTO) updatedItem);
    }

}
