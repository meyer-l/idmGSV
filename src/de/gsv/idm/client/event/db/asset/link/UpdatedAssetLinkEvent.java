package de.gsv.idm.client.event.db.asset.link;

import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.ObjectEvent;
import de.gsv.idm.shared.dto.AssetLinkDTO;

public class UpdatedAssetLinkEvent extends
ObjectEvent<AssetLinkDTO, UpdatedAssetLinkEvent> {

public static Type<GeneralEventHandler<UpdatedAssetLinkEvent>> TYPE = new Type<GeneralEventHandler<UpdatedAssetLinkEvent>>();

public UpdatedAssetLinkEvent(AssetLinkDTO object) {
super(object);
}

@Override
public Type<GeneralEventHandler<UpdatedAssetLinkEvent>> getAssociatedType() {
return TYPE;
}
}
