package de.gsv.idm.server.service;

import java.util.List;

import de.gsv.idm.client.services.ThreatService;
import de.gsv.idm.server.general.GeneralServiceImpl;
import de.gsv.idm.shared.dto.ThreatDTO;
import de.gsv.idm.shared.model.Threat;
import de.gsv.idm.shared.push.event.PushEvent;
import de.gsv.idm.shared.push.event.threat.ThreatCreatedPushEvent;
import de.gsv.idm.shared.push.event.threat.ThreatDeletedPushEvent;
import de.gsv.idm.shared.push.event.threat.ThreatPushEvent;
import de.gsv.idm.shared.push.event.threat.ThreatUpdatedPushEvent;
import de.novanic.eventservice.client.event.domain.Domain;
import de.novanic.eventservice.client.event.domain.DomainFactory;

@SuppressWarnings("serial")
public class ThreatServiceImpl extends GeneralServiceImpl<ThreatDTO, Threat> implements
        ThreatService {
	private static final de.novanic.eventservice.client.event.domain.Domain CONVERSATION_DOMAIN;
	static {
		CONVERSATION_DOMAIN = DomainFactory.getDomain(ThreatPushEvent.CONVERSATION_DOMAIN);
	}

	@Override
	protected List<Threat> findAll() {
		return Threat.findAll();
	}

	@Override
	protected Threat findById(Integer id) {
		return Threat.findById(id);
	}

	@Override
	protected Threat createObject() {
		return Threat.createIt();
	}

	@Override
	protected Domain getConversationDomain() {
		return CONVERSATION_DOMAIN;
	}

	@Override
	protected PushEvent<?, ?> getUpdatedPushEvent(ThreatDTO updated) {
		return new ThreatUpdatedPushEvent(updated);
	}

	@Override
	protected PushEvent<?, ?> getCreatedPushEvent(ThreatDTO updated) {
		return new ThreatCreatedPushEvent(updated);
	}

	@Override
	protected PushEvent<?, ?> getDeletedPushEvent(ThreatDTO updated) {
		return new ThreatDeletedPushEvent(updated);
	}

}