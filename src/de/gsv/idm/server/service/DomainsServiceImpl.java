package de.gsv.idm.server.service;

import java.util.List;

import de.gsv.idm.client.services.DomainsService;
import de.gsv.idm.server.general.GeneralServiceImpl;
import de.gsv.idm.shared.dto.DomainDTO;
import de.gsv.idm.shared.model.Domain;
import de.gsv.idm.shared.push.event.PushEvent;
import de.gsv.idm.shared.push.event.domain.DomainCreatedPushEvent;
import de.gsv.idm.shared.push.event.domain.DomainDeletedPushEvent;
import de.gsv.idm.shared.push.event.domain.DomainPushEvent;
import de.gsv.idm.shared.push.event.domain.DomainUpdatedPushEvent;
import de.novanic.eventservice.client.event.domain.DomainFactory;

@SuppressWarnings("serial")
public class DomainsServiceImpl extends GeneralServiceImpl< DomainDTO, Domain> implements
		DomainsService {
	 private static final de.novanic.eventservice.client.event.domain.Domain CONVERSATION_DOMAIN;
	 static {
	        CONVERSATION_DOMAIN = DomainFactory.getDomain(DomainPushEvent.CONVERSATION_DOMAIN);
	    }
	 
	@Override
    protected List<Domain> findAll() {
	    return Domain.findAll();
    }

	@Override
    protected Domain findById(Integer id) {
	    return Domain.findById(id);
    }

	@Override
    protected Domain createObject() {
		return Domain.createIt();
    }

	@Override
    protected de.novanic.eventservice.client.event.domain.Domain getConversationDomain() {
	   return CONVERSATION_DOMAIN;
    }

	@Override
    protected PushEvent<?, ?> getUpdatedPushEvent(DomainDTO updated) {
	    return new DomainUpdatedPushEvent(updated);
    }

	@Override
    protected PushEvent<?, ?> getCreatedPushEvent(DomainDTO updated) {
		return new DomainCreatedPushEvent(updated);
    }

	@Override
    protected PushEvent<?, ?> getDeletedPushEvent(DomainDTO updated) {
		return new DomainDeletedPushEvent(updated);
    }
}