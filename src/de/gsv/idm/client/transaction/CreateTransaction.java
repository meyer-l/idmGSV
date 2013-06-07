package de.gsv.idm.client.transaction;

import java.util.logging.Level;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.info.Info;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.RpcController.general.GeneralRpcController;
import de.gsv.idm.shared.dto.GeneralDTO;


public class CreateTransaction<T extends GeneralDTO<T>> implements DBTransactionEvent {

	private T dbObject;
	private String text;
	GeneralRpcController<T> generalService;
	
	public CreateTransaction(GeneralRpcController<T> generalService, T object){
		dbObject = object;
		this.text = object.getClassName() + " " +object.getName()+" wurde erstellt. (ID: "+object.getId()+")";
		this.generalService = generalService;
	}

	@Override
	public String getType() {
		return dbObject.getClassName() + " (ID: " + dbObject.getId() +") " + "wurde erstellt";
	}
	
	public T getCreatedObject(){
		return dbObject;
	}
	
	public String getString(){
		return text;
	}

	@Override
	public void rollback() {
		generalService.delete(getCreatedObject(), new AsyncCallback<T>() {			
			@Override
			public void onSuccess(T result) {
				Info.display("Rollback:", getString());
			}			
			@Override
			public void onFailure(Throwable caught) {
				DBController.getLogger().log(
				        Level.SEVERE,
				        "Error while rollbacking create transaction for "
				                + getCreatedObject().getClassName() + " Id: "
				                + getCreatedObject().getId());
			}
		});
	}

	@Override
	public void applyTransaction() {
		generalService.create(getCreatedObject(), new AsyncCallback<T>() {			
			@Override
			public void onSuccess(T result) {
				if(result != null) {
					
				}	
			}			
			@Override
			public void onFailure(Throwable caught) {
				DBController.getLogger().log(
				        Level.SEVERE,
				        "Error while applying create transaction for "
				                + getCreatedObject().getClassName() + " Id: "
				                + getCreatedObject().getId());
			}
		});
	}

}
