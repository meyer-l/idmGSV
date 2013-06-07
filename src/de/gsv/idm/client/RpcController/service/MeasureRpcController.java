package de.gsv.idm.client.RpcController.service;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.RpcController.general.GeneralRpcController;
import de.gsv.idm.client.services.MeasureService;
import de.gsv.idm.client.services.MeasureServiceAsync;
import de.gsv.idm.shared.dto.GeneralDTO;
import de.gsv.idm.shared.dto.MeasureDTO;
import de.gsv.idm.shared.dto.ModuleDTO;
import de.gsv.idm.shared.dto.ThreatDTO;

public class MeasureRpcController extends GeneralRpcController<MeasureDTO> implements
        MeasureServiceAsync {
	public MeasureRpcController(DBController communicationController) {
		super((MeasureServiceAsync) GWT.create(MeasureService.class), communicationController);
	}

	@Override
	public MeasureDTO fixAssociations(MeasureDTO toPut) {

		List<ModuleDTO> clonedModules = new ArrayList<ModuleDTO>();
		for (ModuleDTO module : toPut.getModules()) {
			clonedModules.add(dbConnector.getModuleController().getFromMap(module));
		}
		toPut.setModules(clonedModules);

		List<ThreatDTO> clonedThreats = new ArrayList<ThreatDTO>();
		for (ThreatDTO threat : toPut.getThreats()) {
			clonedThreats.add(dbConnector.getThreatController().getFromMap(threat));
		}
		toPut.setThreats(clonedThreats);

		List<MeasureDTO> clonedMeasures = new ArrayList<MeasureDTO>();
		for (MeasureDTO measure : toPut.getMeasures()) {
			clonedMeasures.add(getFromMap(measure));
		}
		toPut.setMeasures(clonedMeasures);

		return toPut;
	}

	@Override
    protected MeasureDTO handleGeneralObject(GeneralDTO<?> updatedItem) {
		return putInMap((MeasureDTO) updatedItem);
    }
}
