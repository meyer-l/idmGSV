package de.gsv.idm.client.event.db.asset.link;

import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.ObjectEvent;
import de.gsv.idm.shared.dto.AssetLinkDTO;

public class CreatedAssetLinkEvent extends
        ObjectEvent<AssetLinkDTO, CreatedAssetLinkEvent> {

	public static Type<GeneralEventHandler<CreatedAssetLinkEvent>> TYPE = new Type<GeneralEventHandler<CreatedAssetLinkEvent>>();

	public CreatedAssetLinkEvent(AssetLinkDTO object) {
		super(object);
	}

	@Override
	public Type<GeneralEventHandler<CreatedAssetLinkEvent>> getAssociatedType() {
		return TYPE;
	}

}
