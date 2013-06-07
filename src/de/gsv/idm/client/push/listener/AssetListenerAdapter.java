package de.gsv.idm.client.push.listener;

import java.util.List;
import java.util.logging.Level;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.event.db.asset.CreatedAssetEvent;
import de.gsv.idm.client.event.db.asset.DeletedAssetEvent;
import de.gsv.idm.client.event.db.asset.UpdatedAssetEvent;
import de.gsv.idm.client.push.AssociatedObjectsProcessor;
import de.gsv.idm.client.push.CUDListenerAdapter;
import de.gsv.idm.shared.dto.AssetDTO;
import de.gsv.idm.shared.dto.GeneralDTO;
import de.gsv.idm.shared.push.event.asset.AssetCreatedPushEvent;
import de.gsv.idm.shared.push.event.asset.AssetPushEvent;

public class AssetListenerAdapter extends CUDListenerAdapter<AssetPushEvent, AssetDTO> {

	private static AssetListenerAdapter instance;

	static public AssetListenerAdapter getInstance() {
		if (instance == null) {
			instance = new AssetListenerAdapter();
		}
		return instance;
	}

	public AssetListenerAdapter() {
		super(DBController.getInstance().getAssetController());
	}

	@Override
	public void fireCreatedEvent(final AssetPushEvent createdEvent) {
		modelController.getAssociatedObjects(createdEvent.getObject().getId(),
		        new AsyncCallback<List<GeneralDTO<?>>>() {
			        @Override
			        public void onFailure(Throwable caught) {
				        DBController.getLogger().log(
				                Level.SEVERE,
				                "Error while fetching AssociatedObjects for "
				                        + createdEvent.getObject().getClassName() + " (ID: "
				                        + createdEvent.getObject().getId() + ")");
			        }

			        @Override
			        public void onSuccess(List<GeneralDTO<?>> result) {
                        new AssociatedObjectsProcessor(result);
				        eventBus.fireEvent(new CreatedAssetEvent(createdEvent.getObject(),
						        ((AssetCreatedPushEvent) createdEvent).getTempId()));
			        }
		        });
		
	}

	@Override
	public void fireDeletedEvent(AssetPushEvent deletedEvent) {
		eventBus.fireEvent(new DeletedAssetEvent(deletedEvent.getObject()));
	}

	@Override
	public void fireUpdatedEvent(AssetDTO updatedEvent) {
		eventBus.fireEvent(new UpdatedAssetEvent(updatedEvent));
	}

}
