package de.gsv.idm.client.push.listener;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.event.db.module.CreatedModuleEvent;
import de.gsv.idm.client.event.db.module.DeletedModuleEvent;
import de.gsv.idm.client.event.db.module.UpdatedModuleEvent;
import de.gsv.idm.client.push.CUDListenerAdapter;
import de.gsv.idm.shared.dto.ModuleDTO;
import de.gsv.idm.shared.push.event.module.ModulePushEvent;

public class ModuleListenerAdapter extends CUDListenerAdapter<ModulePushEvent, ModuleDTO> {

	private static ModuleListenerAdapter instance;

	static public ModuleListenerAdapter getInstance() {
		if (instance == null) {
			instance = new ModuleListenerAdapter();
		}
		return instance;
	}
	
	public ModuleListenerAdapter() {
		super(DBController.getInstance().getModuleController());
	}

	@Override
	public void fireCreatedEvent(ModulePushEvent createdEvent) {
		eventBus.fireEvent(new CreatedModuleEvent(createdEvent.getObject()));
	}

	@Override
	public void fireDeletedEvent(ModulePushEvent deletedEvent) {
		eventBus.fireEvent(new DeletedModuleEvent(deletedEvent.getObject()));
	}

	@Override
	public void fireUpdatedEvent(ModuleDTO updatedEvent) {
		eventBus.fireEvent(new UpdatedModuleEvent(updatedEvent));

	}

}