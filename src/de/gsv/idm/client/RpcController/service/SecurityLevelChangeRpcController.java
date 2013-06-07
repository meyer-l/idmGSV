package de.gsv.idm.client.RpcController.service;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.RpcController.general.GeneralDomainRpcController;
import de.gsv.idm.client.services.SecurityLevelChangeService;
import de.gsv.idm.client.services.SecurityLevelChangeServiceAsync;
import de.gsv.idm.shared.dto.GeneralDTO;
import de.gsv.idm.shared.dto.SecurityLevelChangeDTO;

public class SecurityLevelChangeRpcController extends
        GeneralDomainRpcController<SecurityLevelChangeDTO> implements
        SecurityLevelChangeServiceAsync {
	
	private final SecurityLevelChangeServiceAsync securityLevelChangeService;

	public SecurityLevelChangeRpcController(DBController communicationController) {
		this((SecurityLevelChangeServiceAsync) GWT.create(SecurityLevelChangeService.class),
		        communicationController);
	}

	public SecurityLevelChangeRpcController(SecurityLevelChangeServiceAsync create,
            DBController communicationController) {
	    super(create,communicationController);
	    securityLevelChangeService = create;
    }
	public void saveEditableList(Integer domain_id,
	        List<SecurityLevelChangeDTO> changeEventsToSave, final AsyncCallback<Boolean> callback) {
		saveEditableList(domain_id, changeEventsToSave, dbConnector.getSessionId(), callback);
	}

	@Override
	public void saveEditableList(Integer domain_id,
	        List<SecurityLevelChangeDTO> changeEventsToSave,String sessionId, final AsyncCallback<Boolean> callback) {
		securityLevelChangeService.saveEditableList(domain_id, changeEventsToSave, sessionId,
	  		        new AsyncCallback<Boolean>() {

						@Override
                        public void onFailure(Throwable caught) {
	                        callback.onFailure(caught);
                        }

						@Override
                        public void onSuccess(Boolean result) {
	                        callback.onSuccess(result);
                        }
	  			
	  		});

	}

	@Override
	public SecurityLevelChangeDTO fixAssociations(SecurityLevelChangeDTO toPut) {
		toPut.setAsset(dbConnector.getAssetController().getFromMap(toPut.getAsset()));
		return toPut;
	}

	@Override
    protected SecurityLevelChangeDTO handleGeneralObject(GeneralDTO<?> updatedItem) {
		return putInMap((SecurityLevelChangeDTO) updatedItem);
    }

}
