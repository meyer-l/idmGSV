package de.gsv.idm.shared.push.event.module;

import de.gsv.idm.client.push.CUDListener;
import de.gsv.idm.shared.dto.ModuleDTO;

@SuppressWarnings("serial")
public class ModuleCreatedPushEvent extends ModulePushEvent {
	
	public ModuleCreatedPushEvent() {
	}
	
	public ModuleCreatedPushEvent(ModuleDTO object){
		super(object);
	}
	public void call(CUDListener<ModulePushEvent> listener) {
		listener.onCreated(this);
	}

}
