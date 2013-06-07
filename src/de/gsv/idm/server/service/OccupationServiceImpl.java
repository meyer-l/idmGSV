package de.gsv.idm.server.service;

import java.util.List;

import de.gsv.idm.client.services.OccupationService;
import de.gsv.idm.server.general.GeneralDomainServiceImpl;
import de.gsv.idm.shared.dto.OccupationDTO;
import de.gsv.idm.shared.model.Occupation;
import de.gsv.idm.shared.push.event.PushEvent;
import de.gsv.idm.shared.push.event.occupation.OccupationCreatedPushEvent;
import de.gsv.idm.shared.push.event.occupation.OccupationDeletedPushEvent;
import de.gsv.idm.shared.push.event.occupation.OccupationPushEvent;
import de.gsv.idm.shared.push.event.occupation.OccupationUpdatedPushEvent;
import de.novanic.eventservice.client.event.domain.DomainFactory;

@SuppressWarnings("serial")
public class OccupationServiceImpl extends GeneralDomainServiceImpl<OccupationDTO, Occupation>
        implements OccupationService {

	public static final de.novanic.eventservice.client.event.domain.Domain CONVERSATION_DOMAIN;
	static {
		CONVERSATION_DOMAIN = DomainFactory.getDomain(OccupationPushEvent.CONVERSATION_DOMAIN);
	}

	@Override
	protected Class<Occupation> getDBClass() {
		return Occupation.class;
	}

	@Override
	protected List<Occupation> findAll() {
		return Occupation.findAll();
	}

	@Override
	protected Occupation findById(Integer id) {
		return Occupation.findById(id);
	}

	@Override
	protected Occupation createObject() {
		return Occupation.createIt();
	}

	@Override
	protected de.novanic.eventservice.client.event.domain.Domain getConversationDomain() {
		return CONVERSATION_DOMAIN;
	}

	@Override
	protected PushEvent<?, ?> getUpdatedPushEvent(OccupationDTO updated) {
		return new OccupationUpdatedPushEvent(updated);
	}

	@Override
	protected PushEvent<?, ?> getCreatedPushEvent(OccupationDTO created) {
		return new OccupationCreatedPushEvent(created);
	}

	@Override
	protected PushEvent<?, ?> getDeletedPushEvent(OccupationDTO deleted) {
		return new OccupationDeletedPushEvent(deleted);
	}

}
