package de.gsv.idm.client.RpcController.service;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.RpcController.general.GeneralDomainRpcController;
import de.gsv.idm.client.services.AssetLinkService;
import de.gsv.idm.client.services.AssetLinkServiceAsync;
import de.gsv.idm.shared.dto.AssetDTO;
import de.gsv.idm.shared.dto.AssetLinkDTO;
import de.gsv.idm.shared.dto.AssettypeDTO;
import de.gsv.idm.shared.dto.GeneralDTO;

public class AssetLinkRpcController extends GeneralDomainRpcController<AssetLinkDTO> implements
        AssetLinkServiceAsync {

	private final AssetLinkServiceAsync assetmodelLinkService;

	public AssetLinkRpcController(DBController communicationController) {
		this((AssetLinkServiceAsync) GWT.create(AssetLinkService.class), communicationController);
	}

	public AssetLinkRpcController(AssetLinkServiceAsync assetmodelLinkService,
	        DBController communicationController) {
		super(assetmodelLinkService, communicationController);
		this.assetmodelLinkService = assetmodelLinkService;
	}

	public void create(AssetDTO model1, AssetDTO model2,
	        final AsyncCallback<List<AssetLinkDTO>> callback) {
		create(model1, model2, dbConnector.getSessionId(), callback);
	}

	@Override
	public void create(AssetDTO model1, AssetDTO model2, String sessionId,
	        final AsyncCallback<List<AssetLinkDTO>> callback) {
		assetmodelLinkService.create(model1, model2, sessionId,
		        new AsyncCallback<List<AssetLinkDTO>>() {
			        public void onFailure(Throwable caught) {
				        callback.onFailure(caught);
			        }

			        public void onSuccess(List<AssetLinkDTO> result) {
				        callback.onSuccess(result);

			        }
		        });

	}

	public void create(AssettypeDTO asset, AssetDTO model,
	        final AsyncCallback<List<AssetLinkDTO>> callback) {
		create(asset, model, dbConnector.getSessionId(), callback);
	}

	@Override
	public void create(AssettypeDTO asset, AssetDTO model, String sessionId,
	        final AsyncCallback<List<AssetLinkDTO>> callback) {
		assetmodelLinkService.create(asset, model, sessionId,
		        new AsyncCallback<List<AssetLinkDTO>>() {
			        public void onFailure(Throwable caught) {
				        callback.onFailure(caught);
			        }

			        public void onSuccess(List<AssetLinkDTO> result) {
				        callback.onSuccess(result);

			        }
		        });
	}

	@Override
	public AssetLinkDTO fixAssociations(AssetLinkDTO toPut) {
		toPut.setAsset(DBController.getInstance().getAssetController().getFromMap(toPut.getAsset()));
		if (toPut.getParent() != null) {
			toPut.setParent(DBController.getInstance().getAssetController()
			        .getFromMap(toPut.getParent()));
		}
		return toPut;
	}

	@Override
    protected AssetLinkDTO handleGeneralObject(GeneralDTO<?> updatedItem) {	   
	    return putInMap((AssetLinkDTO) updatedItem);
    }

}
