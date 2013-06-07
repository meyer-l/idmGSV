package de.gsv.idm.client.RpcController.service;

import java.util.ArrayList;
import java.util.List;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.RpcController.general.GeneralRpcController;
import de.gsv.idm.shared.dto.AssetDTO;
import de.gsv.idm.shared.dto.ChangeItemDTO;
import de.gsv.idm.shared.dto.GeneralDTO;
import de.gsv.idm.shared.dto.OccupationDTO;

public class ChangeItemRpcController extends GeneralRpcController<ChangeItemDTO> {

	public ChangeItemRpcController(DBController communicationController) {
		super(null, communicationController);
	}

	@Override
    public ChangeItemDTO fixAssociations(ChangeItemDTO toPut) {
		toPut.setEmployee(dbConnector.getEmployeeController().getFromMap(toPut.getEmployee()));

		List<AssetDTO> mappedOldAssets = new ArrayList<AssetDTO>();
		for (AssetDTO asset : toPut.getOldAssets()) {
			mappedOldAssets.add(dbConnector.getAssetController().getFromMap(asset));
		}
		toPut.setOldAssets(mappedOldAssets);

		List<AssetDTO> mappedNewAssets = new ArrayList<AssetDTO>();
		for (AssetDTO asset : toPut.getNewAssets()) {
			mappedNewAssets.add(dbConnector.getAssetController().getFromMap(asset));
		}
		toPut.setNewAssets(mappedNewAssets);

		List<OccupationDTO> mappedOldOccupations = new ArrayList<OccupationDTO>();
		for (OccupationDTO occupation : toPut.getOldOccupations()) {
			mappedOldOccupations.add(dbConnector.getOccupationController().getFromMap(occupation));
		}
		toPut.setOldOccupations(mappedOldOccupations);

		List<OccupationDTO> mappedNewOccupations = new ArrayList<OccupationDTO>();
		for (OccupationDTO occupation : toPut.getNewOccupations()) {
			mappedNewOccupations.add(dbConnector.getOccupationController().getFromMap(occupation));
		}
		toPut.setNewOccupations(mappedNewOccupations);
		
		return toPut;
    }

	@Override
    protected ChangeItemDTO handleGeneralObject(GeneralDTO<?> updatedItem) {
		return putInMap((ChangeItemDTO) updatedItem);
    }

}
