package de.gsv.idm.client.event.db.module;

import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.ObjectEvent;
import de.gsv.idm.shared.dto.ModuleDTO;

public class UpdatedModuleEvent extends ObjectEvent<ModuleDTO,UpdatedModuleEvent> {
	public static Type<GeneralEventHandler<UpdatedModuleEvent>> TYPE = new Type<GeneralEventHandler<UpdatedModuleEvent>>();
	public UpdatedModuleEvent(ModuleDTO dbObject) {
	    super(dbObject);
    }

	@Override
    public Type<GeneralEventHandler<UpdatedModuleEvent>> getAssociatedType() {
	   return TYPE;
    }

}
