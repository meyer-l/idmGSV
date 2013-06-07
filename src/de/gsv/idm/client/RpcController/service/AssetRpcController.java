package de.gsv.idm.client.RpcController.service;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.RpcController.general.GeneralDomainRpcController;
import de.gsv.idm.client.services.AssetService;
import de.gsv.idm.client.services.AssetServiceAsync;
import de.gsv.idm.shared.dto.AssetDTO;
import de.gsv.idm.shared.dto.AssetLinkDTO;
import de.gsv.idm.shared.dto.AssetMeasureLinkDTO;
import de.gsv.idm.shared.dto.AssetModuleLinkDTO;
import de.gsv.idm.shared.dto.EmployeeDTO;
import de.gsv.idm.shared.dto.GeneralDTO;

public class AssetRpcController extends GeneralDomainRpcController<AssetDTO> implements
        AssetServiceAsync {

	private final AssetServiceAsync assetService;

	public AssetRpcController(AssetServiceAsync assetService,
	        DBController communicationController) {
		super(assetService, communicationController);
		this.assetService = assetService;

	}

	public AssetRpcController(DBController communicationController) {
		this((AssetServiceAsync) GWT.create(AssetService.class), communicationController);

	}
	public void getTreeRoot(Integer domainId, final AsyncCallback<ArrayList<AssetDTO>> callback) {
		getTreeRoot(domainId, dbConnector.getSessionId(), callback);
	}

	public void getTreeRoot(Integer domainId, String sessionId, final AsyncCallback<ArrayList<AssetDTO>> callback) {
		assetService.getTreeRoot(domainId, sessionId, new AsyncCallback<ArrayList<AssetDTO>>() {
			public void onSuccess(ArrayList<AssetDTO> result) {
				ArrayList<AssetDTO> mappedList = new ArrayList<AssetDTO>();
				for (AssetDTO object : result) {
					object = putInMap(object);
					processAssetmodelTreeChildren(object);
					mappedList.add(object);
				}
				callback.onSuccess(mappedList);
			}

			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}
		});
	}

	public void processAssetmodelTreeChildren(AssetDTO parent) {
		ArrayList<AssetDTO> mappedChildren = new ArrayList<AssetDTO>();
		for (AssetDTO child : parent.getChildren()) {
			AssetDTO inMap = putInMap(child);
			processAssetmodelTreeChildren(inMap);
			mappedChildren.add(inMap);
		}
		parent.setChildren(mappedChildren);
	}

	@Override
	public AssetDTO fixAssociations(AssetDTO toPut) {
		toPut.setAssettype(dbConnector.getAssettypeController().getFromMap(toPut.getAssettype()));

		List<AssetDTO> clonedChildren = new ArrayList<AssetDTO>();
		for (AssetDTO child : toPut.getChildren()) {
			clonedChildren.add(getFromMap(child));
		}
		toPut.setChildren(clonedChildren);

		List<AssetLinkDTO> clonedLinkedChildren = new ArrayList<AssetLinkDTO>();
		for (AssetLinkDTO child : toPut.getLinkedAssets()) {
			AssetLinkDTO fromMap = dbConnector.getAssetLinkController().getFromMap(child);
			fromMap.setParent(getFromMap(fromMap.getParent()));
			fromMap.setAsset(getFromMap(fromMap.getAsset()));
			clonedLinkedChildren.add(fromMap);
		}
		toPut.setLinkedChildren(clonedLinkedChildren);

		toPut.setParent(getFromMap(toPut.getParent()));

		List<AssetMeasureLinkDTO> clonedMeasureLinks = new ArrayList<AssetMeasureLinkDTO>();
		for (AssetMeasureLinkDTO child : toPut.getMeasureLinks()) {
			child.setAssettypeMeasureLink(dbConnector.getAssettypeMeasureLinkController().getFromMap(
			        child.getAssettypeMeasureLink()));
			child.setMeasure(dbConnector.getMeasureController().getFromMap(child.getMeasure()));
			clonedMeasureLinks.add(child);
		}
		toPut.setMeasureLinks(clonedMeasureLinks);

		List<AssetModuleLinkDTO> clonedModuleLinks = new ArrayList<AssetModuleLinkDTO>();
		for (AssetModuleLinkDTO child : toPut.getModuleLinks()) {
			child.setAssettypeModuleLink(dbConnector.getAssettypeModuleLinkController().getFromMap(
			        child.getAssettypeModuleLink()));
			clonedModuleLinks.add(child);
		}
		toPut.setModuleLinks(clonedModuleLinks);

		toPut.setResponsiblePerson(dbConnector.getEmployeeController().getFromMap(
		        toPut.getResponsiblePerson()));

		List<EmployeeDTO> clonedEmployees = new ArrayList<EmployeeDTO>();
		for (EmployeeDTO child : toPut.getAssociatedPersons()) {
			clonedEmployees.add(dbConnector.getEmployeeController().getFromMap(child));
		}
		toPut.setAssociatedPersons(clonedEmployees);

		return toPut;
	}

	@Override
    protected AssetDTO handleGeneralObject(GeneralDTO<?> updatedItem) {
		return putInMap((AssetDTO) updatedItem);
    }

}
