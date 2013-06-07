package de.gsv.idm.server.service;

import java.util.List;

import de.gsv.idm.client.services.ModuleService;
import de.gsv.idm.server.general.GeneralServiceImpl;
import de.gsv.idm.shared.dto.ModuleDTO;
import de.gsv.idm.shared.model.Module;
import de.gsv.idm.shared.push.event.PushEvent;
import de.gsv.idm.shared.push.event.module.ModuleCreatedPushEvent;
import de.gsv.idm.shared.push.event.module.ModuleDeletedPushEvent;
import de.gsv.idm.shared.push.event.module.ModulePushEvent;
import de.gsv.idm.shared.push.event.module.ModuleUpdatedPushEvent;
import de.novanic.eventservice.client.event.domain.Domain;
import de.novanic.eventservice.client.event.domain.DomainFactory;

@SuppressWarnings("serial")
public class ModuleServiceImpl extends GeneralServiceImpl<ModuleDTO, Module> implements
        ModuleService {
	private static final de.novanic.eventservice.client.event.domain.Domain CONVERSATION_DOMAIN;
	static {
		CONVERSATION_DOMAIN = DomainFactory.getDomain(ModulePushEvent.CONVERSATION_DOMAIN);
	}

	@Override
	protected List<Module> findAll() {
		return Module.findAll();
	}

	@Override
	protected Module findById(Integer id) {
		return Module.findById(id);
	}

	@Override
	protected Module createObject() {
		return Module.createIt();
	}

	@Override
	protected Domain getConversationDomain() {
		return CONVERSATION_DOMAIN;
	}

	@Override
	protected PushEvent<?, ?> getUpdatedPushEvent(ModuleDTO updated) {
		return new ModuleUpdatedPushEvent(updated);
	}

	@Override
	protected PushEvent<?, ?> getCreatedPushEvent(ModuleDTO created) {
		return new ModuleCreatedPushEvent(created);
	}

	@Override
	protected PushEvent<?, ?> getDeletedPushEvent(ModuleDTO deleted) {
		return new ModuleDeletedPushEvent(deleted);
	}

}
