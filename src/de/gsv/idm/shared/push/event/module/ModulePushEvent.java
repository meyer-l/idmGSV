package de.gsv.idm.shared.push.event.module;

import de.gsv.idm.shared.dto.ModuleDTO;
import de.gsv.idm.shared.push.event.PushEvent;

@SuppressWarnings("serial")
public abstract class ModulePushEvent extends PushEvent<ModulePushEvent, ModuleDTO> {
	public static final String CONVERSATION_DOMAIN = "module_pushDomain";

	ModulePushEvent() {

	}

	ModulePushEvent(ModuleDTO module) {
		super(module);
	}

	public String getConservationDomain() {
		return CONVERSATION_DOMAIN;
	}

}
