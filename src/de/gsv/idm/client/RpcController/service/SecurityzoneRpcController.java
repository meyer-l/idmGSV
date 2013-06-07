package de.gsv.idm.client.RpcController.service;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.RpcController.general.GeneralRpcController;
import de.gsv.idm.client.services.SecurityzoneService;
import de.gsv.idm.client.services.SecurityzoneServiceAsync;
import de.gsv.idm.shared.dto.GeneralDTO;
import de.gsv.idm.shared.dto.MeasureDTO;
import de.gsv.idm.shared.dto.SecurityzoneDTO;

public class SecurityzoneRpcController extends GeneralRpcController<SecurityzoneDTO> implements
        SecurityzoneServiceAsync {
	
	public SecurityzoneRpcController(SecurityzoneServiceAsync securityzoneServiceAsync,
	        DBController communicationController) {
		super(securityzoneServiceAsync, communicationController);
	}

	public SecurityzoneRpcController(DBController communicationController) {
		this((SecurityzoneServiceAsync) GWT.create(SecurityzoneService.class),
		        communicationController);
	}

	@Override
	public SecurityzoneDTO fixAssociations(SecurityzoneDTO toPut) {
		List<MeasureDTO> mappedMeasures = new ArrayList<MeasureDTO>();
		for (MeasureDTO measureDto : toPut.getMeasures()) {
			mappedMeasures.add(dbConnector.getMeasureController().getFromMap(measureDto));
		}
		toPut.setMeasures(mappedMeasures);
		toPut.setChild(dbConnector.getSecurityzoneController().getFromMap(toPut.getChild()));
		return toPut;
	}

	@Override
    protected SecurityzoneDTO handleGeneralObject(GeneralDTO<?> updatedItem) {
		return putInMap((SecurityzoneDTO) updatedItem);
    }

	



}
