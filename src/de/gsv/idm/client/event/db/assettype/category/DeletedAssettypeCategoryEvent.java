package de.gsv.idm.client.event.db.assettype.category;

import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.ObjectEvent;
import de.gsv.idm.shared.dto.AssettypeCategoryDTO;

public class DeletedAssettypeCategoryEvent extends
        ObjectEvent<AssettypeCategoryDTO, DeletedAssettypeCategoryEvent> {

	public static Type<GeneralEventHandler<DeletedAssettypeCategoryEvent>> TYPE = new Type<GeneralEventHandler<DeletedAssettypeCategoryEvent>>();

	public DeletedAssettypeCategoryEvent(AssettypeCategoryDTO dbObject) {
		super(dbObject);
	}

	@Override
	public Type<GeneralEventHandler<DeletedAssettypeCategoryEvent>> getAssociatedType() {
		return TYPE;
	}
}
