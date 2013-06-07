package de.gsv.idm.client.presenter.general;

import java.util.HashMap;
import java.util.logging.Level;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.widget.core.client.info.Info;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.RpcController.general.GeneralRpcController;
import de.gsv.idm.client.event.db.TransactionBufferEvent;
import de.gsv.idm.client.event.db.securitylevelchange.AssetSecurityLevelChangedEvent;
import de.gsv.idm.client.presenter.securitylevelchange.data.SecurityLevelBundle;
import de.gsv.idm.client.transaction.DeleteTransaction;
import de.gsv.idm.client.transaction.UpdateTransaction;
import de.gsv.idm.shared.dto.AssetDTO;
import de.gsv.idm.shared.dto.GeneralDTO;
import de.gsv.idm.shared.dto.SecurityLevelDTO;

public abstract class GeneralAssetCheckPresenter<T extends GeneralDTO<T>> extends
        GeneralEditorPresenter<T> {

	public GeneralAssetCheckPresenter(
	        GeneralRpcController<T> generalRpcController,
	        de.gsv.idm.client.presenter.general.GeneralEditorPresenter.GeneralEditorDisplay<T> generalEditorDisplay) {
		super(generalRpcController, generalEditorDisplay);
	}

	@Override
	public void updateObject(final T object, final T oldEditObject) {
		HashMap<Integer, SecurityLevelBundle> oldSecuriytLevels = new HashMap<Integer, SecurityLevelBundle>();
		for (AssetDTO asset : DBController.getInstance().getAssetController().getMapValues()) {
			oldSecuriytLevels.put(
			        asset.getId(),
			        new SecurityLevelBundle(asset.getCalculatedSecurityAssesment(), asset
			                .getCalculatedAvailability(), asset.getCalculatedConfidentiality(),
			                asset.getCalculatedIntegrity()));
		}
		addUpdateEventHandler(oldSecuriytLevels);

		generalRpcController.update(object, new AsyncCallback<T>() {
			public void onSuccess(T result) {
				String text = result.getClassName() + " " + result.getLabel()
				        + " wurde aktualisiert. (ID: " + result.getId() + ")";
				TransactionBufferEvent event = new TransactionBufferEvent(new UpdateTransaction<T>(
				        generalRpcController, oldEditObject, result, text));
				eventBus.fireEvent(event);
				Info.display("RPC:", text);
			}

			public void onFailure(Throwable caught) {
				DBController.getLogger().log(Level.SEVERE,
				        "Error while updating " + object.getClassName() + " Id: " + object.getId());
			}
		});
	}

	@Override
	public void deleteObject(final T object) {
		HashMap<Integer, SecurityLevelBundle> oldSecuriytLevels = new HashMap<Integer, SecurityLevelBundle>();
		for (AssetDTO asset : DBController.getInstance().getAssetController().getMapValues()) {
			oldSecuriytLevels.put(
			        asset.getId(),
			        new SecurityLevelBundle(asset.getCalculatedSecurityAssesment(), asset
			                .getCalculatedAvailability(), asset.getCalculatedConfidentiality(),
			                asset.getCalculatedIntegrity()));
		}
		addDeleteEventHandler(oldSecuriytLevels, object.getId());

		generalRpcController.delete(object, new AsyncCallback<T>() {
			public void onFailure(Throwable caught) {
				DBController.getLogger().log(Level.SEVERE,
				        "Error while deleting " + object.getClassName() + " Id: " + object.getId());
			}

			public void onSuccess(T result) {
				String text = result.getClassName() + " " + result.getLabel() + " wurde gelöscht.";
				eventBus.fireEvent(new TransactionBufferEvent(new DeleteTransaction<T>(
				        generalRpcController, object)));
				Info.display("RPC:", text);
			}
		});
	}

	protected abstract void addDeleteEventHandler(
	        final HashMap<Integer, SecurityLevelBundle> oldSecuriytLevels, Integer deletedObjectId);

	protected abstract void addUpdateEventHandler(
	        final HashMap<Integer, SecurityLevelBundle> oldSecuriytLevels);

	protected void checkForChanges(HashMap<Integer, SecurityLevelBundle> oldSecuriytLevelMap) {
		checkForChanges(oldSecuriytLevelMap, null);
	}

	protected void checkForChanges(HashMap<Integer, SecurityLevelBundle> oldSecuriytLevelMap,
	        AssetDTO updatedAsset) {
		for (AssetDTO asset : DBController.getInstance().getAssetController().getMapValues()) {
			if (updatedAsset == null || !asset.equals(updatedAsset)) {
				SecurityLevelDTO newSecurityLevel = asset.getCalculatedSecurityAssesment();
				SecurityLevelBundle oldSecurityLevels = oldSecuriytLevelMap.get(asset.getId());
				if (!newSecurityLevel.getId().equals(
				        oldSecurityLevels.getOldSecurityAssesment().getId())) {
					eventBus.fireEvent(new AssetSecurityLevelChangedEvent(asset, oldSecurityLevels));
				}
			}

		}
	}
}
