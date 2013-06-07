package de.gsv.idm.server.service;

import java.util.List;

import de.gsv.idm.client.services.AssettypeService;
import de.gsv.idm.server.general.GeneralDomainServiceImpl;
import de.gsv.idm.shared.dto.AssettypeDTO;
import de.gsv.idm.shared.model.Assettype;
import de.gsv.idm.shared.push.event.PushEvent;
import de.gsv.idm.shared.push.event.assettype.AssettypeCreatedPushEvent;
import de.gsv.idm.shared.push.event.assettype.AssettypeDeletedPushEvent;
import de.gsv.idm.shared.push.event.assettype.AssettypePushEvent;
import de.gsv.idm.shared.push.event.assettype.AssettypeUpdatedPushEvent;
import de.novanic.eventservice.client.event.domain.DomainFactory;

@SuppressWarnings("serial")
public class AssettypeServiceImpl extends GeneralDomainServiceImpl<AssettypeDTO, Assettype> implements AssettypeService {
	public static final de.novanic.eventservice.client.event.domain.Domain CONVERSATION_DOMAIN;
	static {
		CONVERSATION_DOMAIN = DomainFactory.getDomain(AssettypePushEvent.CONVERSATION_DOMAIN);
	}
	
	@Override
    protected Class<Assettype> getDBClass() {
	    return Assettype.class;
    }
	@Override
    protected List<Assettype> findAll() {
	    return Assettype.findAll();
    }
	@Override
    protected Assettype findById(Integer id) {
	    return Assettype.findById(id);
    }
	@Override
    protected Assettype createObject() {
	    return Assettype.createIt();
    }
	@Override
    protected de.novanic.eventservice.client.event.domain.Domain getConversationDomain() {
	    return CONVERSATION_DOMAIN;
    }
	@Override
    protected PushEvent<?, ?> getUpdatedPushEvent(AssettypeDTO updated) {
	    return new AssettypeUpdatedPushEvent(updated);
    }
	@Override
    protected PushEvent<?, ?> getCreatedPushEvent(AssettypeDTO created) {
	    return new AssettypeCreatedPushEvent(created);
    }
	@Override
    protected PushEvent<?, ?> getDeletedPushEvent(AssettypeDTO deleted) {
	    return new AssettypeDeletedPushEvent(deleted);
    }

}
