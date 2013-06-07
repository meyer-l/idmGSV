package de.gsv.idm.client.push.listener;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.event.db.asset.link.CreatedAssetLinkEvent;
import de.gsv.idm.client.event.db.asset.link.DeletedAssetLinkEvent;
import de.gsv.idm.client.event.db.asset.link.UpdatedAssetLinkEvent;
import de.gsv.idm.client.push.CUDListenerAdapter;
import de.gsv.idm.shared.dto.AssetLinkDTO;
import de.gsv.idm.shared.push.event.asset.link.AssetLinkPushEvent;

public class AssetLinkListenerAdapter extends CUDListenerAdapter<AssetLinkPushEvent, AssetLinkDTO> {

	private static AssetLinkListenerAdapter instance;

	static public AssetLinkListenerAdapter getInstance() {
		if (instance == null) {
			instance = new AssetLinkListenerAdapter();
		}
		return instance;
	}

	public AssetLinkListenerAdapter() {
		super(DBController.getInstance().getAssetLinkController());
	}

	@Override
	public void fireCreatedEvent(AssetLinkPushEvent createdEvent) {
		eventBus.fireEvent(new CreatedAssetLinkEvent(createdEvent.getObject()));
	}

	@Override
	public void fireDeletedEvent(AssetLinkPushEvent deletedEvent) {
		eventBus.fireEvent(new DeletedAssetLinkEvent(deletedEvent.getObject()));
	}

	@Override
	public void fireUpdatedEvent(AssetLinkDTO updatedEvent) {
		eventBus.fireEvent(new UpdatedAssetLinkEvent(updatedEvent));
	}

}
