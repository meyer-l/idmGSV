package de.gsv.idm.shared.push.event.asset.link;

import de.gsv.idm.client.push.CUDListener;
import de.gsv.idm.shared.dto.AssetLinkDTO;

@SuppressWarnings("serial")
public class AssetLinkUpdatedPushEvent extends AssetLinkPushEvent {

	public AssetLinkUpdatedPushEvent() {
    }

	public AssetLinkUpdatedPushEvent(AssetLinkDTO object) {
		super(object);
    }
	
	@Override
	public void call(CUDListener<AssetLinkPushEvent> listener) {
		listener.onUpdated(this);
	}

}
