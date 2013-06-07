package de.gsv.idm.client.event.db.assettype.category;

import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.ObjectEvent;
import de.gsv.idm.shared.dto.AssettypeCategoryDTO;

public class UpdatedAssettypeCategoryEvent extends
        ObjectEvent<AssettypeCategoryDTO, UpdatedAssettypeCategoryEvent> {

	public static Type<GeneralEventHandler<UpdatedAssettypeCategoryEvent>> TYPE = new Type<GeneralEventHandler<UpdatedAssettypeCategoryEvent>>();

	public UpdatedAssettypeCategoryEvent(AssettypeCategoryDTO dbObject) {
		super(dbObject);
	}

	@Override
	public Type<GeneralEventHandler<UpdatedAssettypeCategoryEvent>> getAssociatedType() {
		return TYPE;
	}
}
