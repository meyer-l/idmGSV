package de.gsv.idm.server.service;

import java.util.List;

import de.gsv.idm.client.services.InformationService;
import de.gsv.idm.server.general.GeneralDomainServiceImpl;
import de.gsv.idm.shared.dto.InformationDTO;
import de.gsv.idm.shared.model.Information;
import de.gsv.idm.shared.push.event.PushEvent;
import de.gsv.idm.shared.push.event.information.InformationCreatedPushEvent;
import de.gsv.idm.shared.push.event.information.InformationDeletedPushEvent;
import de.gsv.idm.shared.push.event.information.InformationPushEvent;
import de.gsv.idm.shared.push.event.information.InformationUpdatedPushEvent;
import de.novanic.eventservice.client.event.domain.DomainFactory;

@SuppressWarnings("serial")
public class InformationServiceImpl extends GeneralDomainServiceImpl<InformationDTO, Information>
        implements InformationService {
	public static final de.novanic.eventservice.client.event.domain.Domain CONVERSATION_DOMAIN;
	static {
		CONVERSATION_DOMAIN = DomainFactory.getDomain(InformationPushEvent.CONVERSATION_DOMAIN);
	}

	@Override
	protected Class<Information> getDBClass() {
		return Information.class;
	}

	@Override
	protected List<Information> findAll() {
		return Information.findAll();
	}

	@Override
	protected Information findById(Integer id) {
		return Information.findById(id);
	}

	@Override
	protected Information createObject() {
		return Information.createIt();
	}

	@Override
	protected de.novanic.eventservice.client.event.domain.Domain getConversationDomain() {
		return CONVERSATION_DOMAIN;
	}

	@Override
	protected PushEvent<?, ?> getUpdatedPushEvent(InformationDTO updated) {
		return new InformationUpdatedPushEvent(updated);
	}

	@Override
	protected PushEvent<?, ?> getCreatedPushEvent(InformationDTO created) {
		return new InformationCreatedPushEvent(created);
	}

	@Override
	protected PushEvent<?, ?> getDeletedPushEvent(InformationDTO deleted) {
		return new InformationDeletedPushEvent(deleted);
	}

}
