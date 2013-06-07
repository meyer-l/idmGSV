package de.gsv.idm.client.transaction;

import java.util.logging.Level;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.info.Info;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.RpcController.general.GeneralRpcController;
import de.gsv.idm.shared.dto.GeneralDTO;


public class DeleteTransaction<T extends GeneralDTO<T>> implements DBTransactionEvent {

	private String text;
	private T dbObject;
	GeneralRpcController<T> generalService;

	public DeleteTransaction(GeneralRpcController<T> generalService, T deletedObject) {
		this.text = deletedObject.getClassName() + " " +deletedObject.getName()+" wurde gelöscht.";
		this.dbObject = deletedObject;
		this.generalService = generalService;
	}

	@Override
	public String getType() {
		return dbObject.getClassName() + " (ID: " + dbObject.getId() +") " + "wurde gelöscht";
	}
	
	public T getDeletedObject(){
		return dbObject;
	}

	@Override
	public String getString() {
		return text;
	}
	
	@Override
	public void rollback() {
		generalService.create(getDeletedObject(), new AsyncCallback<T>() {			
			@Override
			public void onSuccess(T result) {
				if(result != null) {
					Info.display("Rollback:", getString());
				}	
			}			
			@Override
			public void onFailure(Throwable caught) {	
				DBController.getLogger().log(
				        Level.SEVERE,
				        "Error while rollbacking delete transaction for "
				                + getDeletedObject().getClassName() + " Id: "
				                + getDeletedObject().getId());
			}
		});
	}

	@Override
	public void applyTransaction() {
		generalService.delete(getDeletedObject(), new AsyncCallback<T>() {			
			@Override
			public void onSuccess(T result) {		
			}			
			@Override
			public void onFailure(Throwable caught) {
				DBController.getLogger().log(
				        Level.SEVERE,
				        "Error while applying delete transaction for "
				                + getDeletedObject().getClassName() + " Id: "
				                + getDeletedObject().getId());
			}
		});
	}

}
