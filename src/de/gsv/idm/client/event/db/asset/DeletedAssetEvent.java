package de.gsv.idm.client.event.db.asset;

import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.ObjectEvent;
import de.gsv.idm.shared.dto.AssetDTO;

public class DeletedAssetEvent extends ObjectEvent<AssetDTO,DeletedAssetEvent > {

	public static Type<GeneralEventHandler<DeletedAssetEvent>> TYPE = new Type<GeneralEventHandler<DeletedAssetEvent>>();

	public DeletedAssetEvent(AssetDTO object) {
		super(object);
	}

	@Override
	public Type<GeneralEventHandler<DeletedAssetEvent>> getAssociatedType() {
		return TYPE;
	}

}
