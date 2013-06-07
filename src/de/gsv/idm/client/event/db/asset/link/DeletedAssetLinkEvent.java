package de.gsv.idm.client.event.db.asset.link;

import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.ObjectEvent;
import de.gsv.idm.shared.dto.AssetLinkDTO;

public class DeletedAssetLinkEvent extends
        ObjectEvent<AssetLinkDTO, DeletedAssetLinkEvent> {

	public static Type<GeneralEventHandler<DeletedAssetLinkEvent>> TYPE = new Type<GeneralEventHandler<DeletedAssetLinkEvent>>();

	public DeletedAssetLinkEvent(AssetLinkDTO object) {
		super(object);
	}

	@Override
	public Type<GeneralEventHandler<DeletedAssetLinkEvent>> getAssociatedType() {
		return TYPE;
	}
}
