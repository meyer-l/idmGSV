package de.gsv.idm.client.event.db.module;

import de.gsv.idm.client.event.GeneralEventHandler;
import de.gsv.idm.client.event.db.ObjectEvent;
import de.gsv.idm.shared.dto.ModuleDTO;

public class DeletedModuleEvent extends ObjectEvent<ModuleDTO,DeletedModuleEvent> {
	public static Type<GeneralEventHandler<DeletedModuleEvent>> TYPE = new Type<GeneralEventHandler<DeletedModuleEvent>>();
	public DeletedModuleEvent(ModuleDTO dbObject) {
	    super(dbObject);
    }

	@Override
    public Type<GeneralEventHandler<DeletedModuleEvent>> getAssociatedType() {
	   return TYPE;
    }

}
