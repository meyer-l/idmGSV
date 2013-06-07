package de.gsv.idm.client.RpcController.general;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.services.GeneralDomainServiceAsync;
import de.gsv.idm.shared.dto.GeneralDTO;

public abstract class GeneralDomainRpcController<T extends GeneralDTO<T>> extends
        GeneralRpcController<T> implements GeneralDomainServiceAsync<T> {

	private final GeneralDomainServiceAsync<T> generalDomainService;

	public GeneralDomainRpcController(GeneralDomainServiceAsync<T> generalDomainService,
	        DBController communicationController) {
		super(generalDomainService, communicationController);
		this.generalDomainService = generalDomainService;
	}
	public void getAll(Integer domain_id, final AsyncCallback<ArrayList<T>> callback) {
		getAll(domain_id, dbConnector.getSessionId(),callback);
	}

	public void getAll(Integer domain_id, String sessionId, final AsyncCallback<ArrayList<T>> callback) {
		generalDomainService.getAll(domain_id,sessionId, new AsyncCallback<ArrayList<T>>() {
			public void onSuccess(ArrayList<T> result) {
				ArrayList<T> mappedList = new ArrayList<T>();
				for (T object : result) {
					object = putInMap(object);
					mappedList.add(object);
				}
				callback.onSuccess(mappedList);
			}

			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}
		});
	}

}
