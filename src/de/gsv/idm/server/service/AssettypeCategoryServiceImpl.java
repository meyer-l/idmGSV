package de.gsv.idm.server.service;

import java.util.List;

import de.gsv.idm.client.services.AssettypeCategoryService;
import de.gsv.idm.server.general.GeneralServiceImpl;
import de.gsv.idm.shared.dto.AssettypeCategoryDTO;
import de.gsv.idm.shared.model.AssettypeCategory;
import de.gsv.idm.shared.push.event.PushEvent;
import de.gsv.idm.shared.push.event.assettype.category.AssettypeCategoryCreatedPushEvent;
import de.gsv.idm.shared.push.event.assettype.category.AssettypeCategoryDeletedPushEvent;
import de.gsv.idm.shared.push.event.assettype.category.AssettypeCategoryPushEvent;
import de.gsv.idm.shared.push.event.assettype.category.AssettypeCategoryUpdatedPushEvent;
import de.novanic.eventservice.client.event.domain.Domain;
import de.novanic.eventservice.client.event.domain.DomainFactory;

@SuppressWarnings("serial")
public class AssettypeCategoryServiceImpl extends GeneralServiceImpl<AssettypeCategoryDTO, AssettypeCategory> implements AssettypeCategoryService{

	 private static final de.novanic.eventservice.client.event.domain.Domain CONVERSATION_DOMAIN;
	 static {
	        CONVERSATION_DOMAIN = DomainFactory.getDomain(AssettypeCategoryPushEvent.CONVERSATION_DOMAIN);
	    }
	
	@Override
    protected List<AssettypeCategory> findAll() {
	   return AssettypeCategory.findAll();
    }

	@Override
    protected AssettypeCategory findById(Integer id) {
	    return AssettypeCategory.findById(id);
    }

	@Override
    protected AssettypeCategory createObject() {
	    return new AssettypeCategory();
    }

	@Override
    protected Domain getConversationDomain() {
	    return CONVERSATION_DOMAIN;
    }

	@Override
    protected PushEvent<?,?> getUpdatedPushEvent(
            AssettypeCategoryDTO updated) {
	    return new AssettypeCategoryUpdatedPushEvent(updated);
    }

	@Override
    protected PushEvent<?,?> getCreatedPushEvent(
            AssettypeCategoryDTO updated) {
		return new AssettypeCategoryCreatedPushEvent(updated);
    }

	@Override
    protected PushEvent<?,?> getDeletedPushEvent(
            AssettypeCategoryDTO updated) {
		return new AssettypeCategoryDeletedPushEvent(updated);
    }

}
