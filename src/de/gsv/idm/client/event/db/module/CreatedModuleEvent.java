package de.gsv.idm.client.event.db.module;

import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.ObjectEvent;
import de.gsv.idm.shared.dto.ModuleDTO;

public class CreatedModuleEvent extends ObjectEvent<ModuleDTO,CreatedModuleEvent> {
	public static Type<GeneralEventHandler<CreatedModuleEvent>> TYPE = new Type<GeneralEventHandler<CreatedModuleEvent>>();
	public CreatedModuleEvent(ModuleDTO dbObject) {
	    super(dbObject);
    }

	@Override
    public Type<GeneralEventHandler<CreatedModuleEvent>> getAssociatedType() {
	   return TYPE;
    }
}
