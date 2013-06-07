package de.gsv.idm.client.RpcController.service;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.RpcController.general.GeneralRpcController;
import de.gsv.idm.client.services.ThreatService;
import de.gsv.idm.client.services.ThreatServiceAsync;
import de.gsv.idm.shared.dto.GeneralDTO;
import de.gsv.idm.shared.dto.MeasureDTO;
import de.gsv.idm.shared.dto.ModuleDTO;
import de.gsv.idm.shared.dto.ThreatDTO;

public class ThreatRpcController extends GeneralRpcController<ThreatDTO> implements
        ThreatServiceAsync {

	public ThreatRpcController(DBController communicationController) {
		super((ThreatServiceAsync) GWT.create(ThreatService.class),communicationController);		
	}

	@Override
    public ThreatDTO fixAssociations(ThreatDTO toPut) {
		List<ModuleDTO> clonedModules = new ArrayList<ModuleDTO>();
		for (ModuleDTO module : toPut.getModules()) {
			clonedModules.add(dbConnector.getModuleController().getFromMap(module));
		}
		toPut.setModules(clonedModules);

		List<ThreatDTO> clonedThreats = new ArrayList<ThreatDTO>();
		for (ThreatDTO threat : toPut.getThreats()) {
			clonedThreats.add(getFromMap(threat));
		}
		toPut.setThreats(clonedThreats);

		List<MeasureDTO> clonedMeasures = new ArrayList<MeasureDTO>();
		for (MeasureDTO measure : toPut.getMeasures()) {
			clonedMeasures.add(dbConnector.getMeasureController().getFromMap(measure));
		}
		toPut.setMeasures(clonedMeasures);
	    return toPut;
    }

	@Override
    protected ThreatDTO handleGeneralObject(GeneralDTO<?> updatedItem) {
		return putInMap((ThreatDTO) updatedItem);
    }
}
