package de.gsv.idm.shared.push.event.module;

import de.gsv.idm.client.push.CUDListener;
import de.gsv.idm.shared.dto.ModuleDTO;

@SuppressWarnings("serial")
public class ModuleUpdatedPushEvent extends ModulePushEvent {
	
	public ModuleUpdatedPushEvent() {
	}
	
	public ModuleUpdatedPushEvent(ModuleDTO object){
		super(object);
	}
	public void call(CUDListener<ModulePushEvent> listener) {
		listener.onUpdated(this);
	}

}
