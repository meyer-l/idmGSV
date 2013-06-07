package de.gsv.idm.client.RpcController.service;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.RpcController.general.GeneralRpcController;
import de.gsv.idm.client.services.ModuleService;
import de.gsv.idm.client.services.ModuleServiceAsync;
import de.gsv.idm.shared.dto.GeneralDTO;
import de.gsv.idm.shared.dto.MeasureDTO;
import de.gsv.idm.shared.dto.MeasureLinkDTO;
import de.gsv.idm.shared.dto.ModuleDTO;
import de.gsv.idm.shared.dto.ThreatDTO;

public class ModuleRpcController extends GeneralRpcController<ModuleDTO> implements
        ModuleServiceAsync {

	public ModuleRpcController(DBController communicationController) {
		super((ModuleServiceAsync) GWT.create(ModuleService.class), communicationController);
	}

	@Override
	public ModuleDTO fixAssociations(ModuleDTO toPut) {
		List<ModuleDTO> clonedModules = new ArrayList<ModuleDTO>();
		if (toPut.getModulesAdd() != null) {
			for (ModuleDTO module : toPut.getModulesAdd()) {
				clonedModules.add(getFromMap(module));
			}
		}
		toPut.setModulesAdd(clonedModules);

		List<ThreatDTO> clonedThreats = new ArrayList<ThreatDTO>();
		if (toPut.getThreats() != null) {
			for (ThreatDTO threat : toPut.getThreats()) {
				clonedThreats.add(dbConnector.getThreatController().getFromMap(threat));
			}
		}
		toPut.setThreats(clonedThreats);

		List<ThreatDTO> clonedThreatsAdd = new ArrayList<ThreatDTO>();
		if (toPut.getThreatsAdd() != null) {
			for (ThreatDTO threat : toPut.getThreatsAdd()) {
				clonedThreatsAdd.add(dbConnector.getThreatController().getFromMap(threat));
			}
		}
		toPut.setThreatsAdd(clonedThreatsAdd);

		List<MeasureLinkDTO> clonedMeasures = new ArrayList<MeasureLinkDTO>();
		if (toPut.getMeasures() != null) {
			for (MeasureLinkDTO measureLink : toPut.getMeasures()) {
				measureLink.setMeasure(dbConnector.getMeasureController().getFromMap(
				        measureLink.getMeasure()));
				clonedMeasures.add(measureLink);
			}
		}
		toPut.setMeasures(clonedMeasures);

		List<MeasureDTO> clonedMeasuresAdd = new ArrayList<MeasureDTO>();
		if (toPut.getMeasuresAdd() != null) {
			for (MeasureDTO measure : toPut.getMeasuresAdd()) {
				clonedMeasuresAdd.add(dbConnector.getMeasureController().getFromMap(measure));
			}
		}
		toPut.setMeasuresAdd(clonedMeasuresAdd);

		return toPut;
	}

	@Override
    protected ModuleDTO handleGeneralObject(GeneralDTO<?> updatedItem) {
		return putInMap((ModuleDTO) updatedItem);
    }
}
