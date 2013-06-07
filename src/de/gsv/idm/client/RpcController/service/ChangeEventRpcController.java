package de.gsv.idm.client.RpcController.service;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.RpcController.general.GeneralDomainRpcController;
import de.gsv.idm.client.services.ChangeEventService;
import de.gsv.idm.client.services.ChangeEventServiceAsync;
import de.gsv.idm.shared.dto.AssetDTO;
import de.gsv.idm.shared.dto.ChangeAppliedDTO;
import de.gsv.idm.shared.dto.ChangeEventDTO;
import de.gsv.idm.shared.dto.EmployeeDTO;
import de.gsv.idm.shared.dto.GeneralDTO;
import de.gsv.idm.shared.dto.OccupationDTO;

public class ChangeEventRpcController extends GeneralDomainRpcController<ChangeEventDTO> implements
        ChangeEventServiceAsync {

	private final ChangeEventServiceAsync changeEventService;

	public ChangeEventRpcController(ChangeEventServiceAsync changeEventService,
	        DBController communicationController) {
		super(changeEventService, communicationController);
		this.changeEventService = changeEventService;

	}

	public ChangeEventRpcController(DBController communicationController) {
		this((ChangeEventServiceAsync) GWT.create(ChangeEventService.class),
		        communicationController);

	}

	public void getAllNotProcessed(Integer domain_id,
	        final AsyncCallback<List<ChangeEventDTO>> callback) {
		getAllNotProcessed(domain_id, dbConnector.getSessionId(), callback);
	}

	@Override
	public void getAllNotProcessed(Integer domain_id, String sessionId,
	        final AsyncCallback<List<ChangeEventDTO>> callback) {
		changeEventService.getAllNotProcessed(domain_id, sessionId,
		        new AsyncCallback<List<ChangeEventDTO>>() {
			        public void onSuccess(List<ChangeEventDTO> result) {
				        ArrayList<ChangeEventDTO> mappedList = new ArrayList<ChangeEventDTO>();
				        for (ChangeEventDTO object : result) {
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
	
	public void getAllManual(Integer domain_id,
	        final AsyncCallback<List<ChangeEventDTO>> callback) {
		getAllManual(domain_id, dbConnector.getSessionId(), callback);
	}

	@Override
	public void getAllManual(Integer domain_id, String sessionId,
	        final AsyncCallback<List<ChangeEventDTO>> callback) {
		changeEventService.getAllManual(domain_id, sessionId,
		        new AsyncCallback<List<ChangeEventDTO>>() {
			        public void onSuccess(List<ChangeEventDTO> result) {
				        ArrayList<ChangeEventDTO> mappedList = new ArrayList<ChangeEventDTO>();
				        for (ChangeEventDTO object : result) {
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
	
	public void getAllNotNeeded(Integer domain_id,
	        final AsyncCallback<List<ChangeEventDTO>> callback) {
		getAllNotNeeded(domain_id, dbConnector.getSessionId(), callback);
	}

	@Override
	public void getAllNotNeeded(Integer domain_id, String sessionId,
	        final AsyncCallback<List<ChangeEventDTO>> callback) {
		changeEventService.getAllNotNeeded(domain_id, sessionId,
		        new AsyncCallback<List<ChangeEventDTO>>() {
			        public void onSuccess(List<ChangeEventDTO> result) {
				        ArrayList<ChangeEventDTO> mappedList = new ArrayList<ChangeEventDTO>();
				        for (ChangeEventDTO object : result) {
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
	
	public void getAllApplied(Integer domain_id,
	        final AsyncCallback<List<ChangeEventDTO>> callback) {
		getAllApplied(domain_id, dbConnector.getSessionId(), callback);
	}

	@Override
	public void getAllApplied(Integer domain_id, String sessionId,
	        final AsyncCallback<List<ChangeEventDTO>> callback) {
		changeEventService.getAllApplied(domain_id, sessionId,
		        new AsyncCallback<List<ChangeEventDTO>>() {
			        public void onSuccess(List<ChangeEventDTO> result) {
				        ArrayList<ChangeEventDTO> mappedList = new ArrayList<ChangeEventDTO>();
				        for (ChangeEventDTO object : result) {
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
	
	public void saveEditableList(Integer domain_id, List<ChangeEventDTO> changeEventsToSave,
	       final AsyncCallback<List<ChangeAppliedDTO>> callback) {
		saveEditableList(domain_id, changeEventsToSave, dbConnector.getSessionId(), callback);
	}

	@Override
	public void saveEditableList(Integer domain_id, List<ChangeEventDTO> changeEventsToSave,
	        String sessionId, final AsyncCallback<List<ChangeAppliedDTO>> callback) {
		changeEventService.saveEditableList(domain_id, changeEventsToSave, sessionId,
		        new AsyncCallback<List<ChangeAppliedDTO>>() {
			        public void onSuccess(List<ChangeAppliedDTO> result) {
				        ArrayList<ChangeAppliedDTO> mappedList = new ArrayList<ChangeAppliedDTO>();
				        for (ChangeAppliedDTO applied : result) {
					        if (applied.getObject() instanceof ChangeEventDTO) {
						        applied.setObject(dbConnector.getChangeEventController()
						                .getFromMap((ChangeEventDTO) applied.getObject()));
						        mappedList.add(applied);
					        } else if (applied.getObject() instanceof EmployeeDTO) {
						        applied.setObject(dbConnector.getEmployeeController().getFromMap(
						                (EmployeeDTO) applied.getObject()));
						        mappedList.add(applied);
					        } else if (applied.getObject() instanceof AssetDTO) {
						        applied.setObject(dbConnector.getAssetController().getFromMap(
						                (AssetDTO) applied.getObject()));
						        mappedList.add(applied);
					        } else if (applied.getObject() instanceof OccupationDTO) {
						        applied.setObject(dbConnector.getOccupationController().getFromMap(
						                (OccupationDTO) applied.getObject()));
						        mappedList.add(applied);
					        }

				        }
				        callback.onSuccess(mappedList);
			        }

			        public void onFailure(Throwable caught) {
				        callback.onFailure(caught);
			        }
		        });
	}

	@Override
	public ChangeEventDTO fixAssociations(ChangeEventDTO toPut) {

		toPut.setChangeItem(dbConnector.getChangeItemController().getFromMap(toPut.getChangeItem()));

		return toPut;
	}

	@Override
    protected ChangeEventDTO handleGeneralObject(GeneralDTO<?> updatedItem) {
	    return putInMap((ChangeEventDTO) updatedItem);
    }

}
