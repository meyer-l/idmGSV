package de.gsv.idm.shared.push.event.asset.link;

import de.gsv.idm.client.push.CUDListener;
import de.gsv.idm.shared.dto.AssetLinkDTO;

@SuppressWarnings("serial")
public class AssetLinkCreatedPushEvent extends AssetLinkPushEvent {

	public AssetLinkCreatedPushEvent() {
    }

	public AssetLinkCreatedPushEvent(AssetLinkDTO object) {
		super(object);
    }
	
	@Override
	public void call(CUDListener<AssetLinkPushEvent> listener) {
		listener.onCreated(this);
	}

}
