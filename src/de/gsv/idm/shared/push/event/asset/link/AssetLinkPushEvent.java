package de.gsv.idm.shared.push.event.asset.link;

import de.gsv.idm.shared.dto.AssetLinkDTO;
import de.gsv.idm.shared.push.event.PushEvent;

@SuppressWarnings("serial")
public abstract class AssetLinkPushEvent extends PushEvent<AssetLinkPushEvent, AssetLinkDTO> {
	public static final String CONVERSATION_DOMAIN = "assetLink_pushDomain";

	AssetLinkPushEvent() {

	}

	AssetLinkPushEvent(AssetLinkDTO object) {
		super(object);
	}

	public String getConservationDomain() {
		return CONVERSATION_DOMAIN;
	}

}
