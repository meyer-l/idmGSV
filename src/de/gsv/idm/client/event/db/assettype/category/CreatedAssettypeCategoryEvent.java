package de.gsv.idm.client.event.db.assettype.category;

import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.ObjectEvent;
import de.gsv.idm.shared.dto.AssettypeCategoryDTO;

public class CreatedAssettypeCategoryEvent extends
        ObjectEvent<AssettypeCategoryDTO, CreatedAssettypeCategoryEvent> {

	public static Type<GeneralEventHandler<CreatedAssettypeCategoryEvent>> TYPE = new Type<GeneralEventHandler<CreatedAssettypeCategoryEvent>>();

	public CreatedAssettypeCategoryEvent(AssettypeCategoryDTO dbObject) {
		super(dbObject);
	}

	@Override
	public Type<GeneralEventHandler<CreatedAssettypeCategoryEvent>> getAssociatedType() {
		return TYPE;
	}

}
