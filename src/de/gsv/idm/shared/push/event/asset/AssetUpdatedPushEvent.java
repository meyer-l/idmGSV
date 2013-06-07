package de.gsv.idm.shared.push.event.asset;

import de.gsv.idm.client.push.CUDListener;
import de.gsv.idm.shared.dto.AssetDTO;

@SuppressWarnings("serial")
public class AssetUpdatedPushEvent extends AssetPushEvent {

	public AssetUpdatedPushEvent() {
    }
	public AssetUpdatedPushEvent(AssetDTO object) {
		super(object);
    }
	
	@Override
	public void call(CUDListener<AssetPushEvent> listener) {
		listener.onUpdated(this);
	}

}
