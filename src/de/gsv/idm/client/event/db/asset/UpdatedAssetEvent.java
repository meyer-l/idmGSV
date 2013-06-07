package de.gsv.idm.client.event.db.asset;

import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.ObjectEvent;
import de.gsv.idm.shared.dto.AssetDTO;

public class UpdatedAssetEvent extends ObjectEvent<AssetDTO,UpdatedAssetEvent > {

	public static Type<GeneralEventHandler<UpdatedAssetEvent>> TYPE = new Type<GeneralEventHandler<UpdatedAssetEvent>>();

	public UpdatedAssetEvent(AssetDTO object) {
		super(object);
	}

	@Override
	public Type<GeneralEventHandler<UpdatedAssetEvent>> getAssociatedType() {
		return TYPE;
	}

}
