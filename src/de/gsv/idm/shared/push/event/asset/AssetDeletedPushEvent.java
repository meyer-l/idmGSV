package de.gsv.idm.shared.push.event.asset;

import de.gsv.idm.client.push.CUDListener;
import de.gsv.idm.shared.dto.AssetDTO;

@SuppressWarnings("serial")
public class AssetDeletedPushEvent extends AssetPushEvent {

	public AssetDeletedPushEvent() {
    }
	public AssetDeletedPushEvent(AssetDTO object) {
		super(object);
    }
	
	@Override
	public void call(CUDListener<AssetPushEvent> listener) {
		listener.onDeleted(this);
	}

}
