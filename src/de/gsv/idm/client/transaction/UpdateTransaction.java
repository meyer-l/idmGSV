package de.gsv.idm.client.transaction;

import java.util.logging.Level;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.info.Info;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.RpcController.general.GeneralRpcController;
import de.gsv.idm.shared.dto.GeneralDTO;

public class UpdateTransaction<T extends GeneralDTO<T>> implements DBTransactionEvent {

	private T newDbObject;
	private T oldDbObject;
	private String text;
	private GeneralRpcController<T> generalService;

	public UpdateTransaction(GeneralRpcController<T> generalService, T oldDbObject, T newDbObject,
	        String text) {
		this.oldDbObject = oldDbObject;
		this.newDbObject = newDbObject;
		this.text = text;
		this.generalService = generalService;
	}

	@Override
	public String getType() {
		return newDbObject.getClassName() + " (ID: " + newDbObject.getId() + ") "
		        + "wurde aktualisiert";
	}

	public T getNewDbObject() {
		return newDbObject;
	}

	public T getOldDbObject() {
		return oldDbObject;
	}

	@Override
	public String getString() {
		return text;
	}

	@Override
	public void rollback() {
		generalService.update(getOldDbObject(), new AsyncCallback<T>() {
			public void onSuccess(T result) {
				if (result != null) {
					Info.display("Rollback:", getString());
				} else {
					Info.display("Rollback:", "Zu aktualisierendes Element nicht gefunden.");
				}
			}

			public void onFailure(Throwable caught) {
				DBController.getLogger().log(
				        Level.SEVERE,
				        "Error while rollbacking update transaction for "
				                + getOldDbObject().getClassName() + " Id: "
				                + getOldDbObject().getId());
			}
		});
	}

	@Override
	public void applyTransaction() {
		generalService.update(getNewDbObject(), new AsyncCallback<T>() {
			public void onSuccess(T result) {
			}

			public void onFailure(Throwable caught) {
				DBController.getLogger().log(
				        Level.SEVERE,
				        "Error while applying update transaction for "
				                + getNewDbObject().getClassName() + " Id: "
				                + getNewDbObject().getId());
			}
		});
	}

}
