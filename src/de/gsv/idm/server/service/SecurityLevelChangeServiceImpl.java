package de.gsv.idm.server.service;

import java.util.List;

import de.gsv.idm.client.services.SecurityLevelChangeService;
import de.gsv.idm.server.general.GeneralDomainServiceImpl;
import de.gsv.idm.shared.dto.SecurityLevelChangeDTO;
import de.gsv.idm.shared.model.SecurityLevelChange;
import de.gsv.idm.shared.model.User;
import de.gsv.idm.shared.push.event.PushEvent;
import de.gsv.idm.shared.push.event.securitylevelchange.SecurityLevelChangeCreatedPushEvent;
import de.gsv.idm.shared.push.event.securitylevelchange.SecurityLevelChangeDeletedPushEvent;
import de.gsv.idm.shared.push.event.securitylevelchange.SecurityLevelChangePushEvent;
import de.gsv.idm.shared.push.event.securitylevelchange.SecurityLevelChangeUpdatedPushEvent;
import de.novanic.eventservice.client.event.domain.Domain;
import de.novanic.eventservice.client.event.domain.DomainFactory;

@SuppressWarnings("serial")
public class SecurityLevelChangeServiceImpl extends
        GeneralDomainServiceImpl<SecurityLevelChangeDTO, SecurityLevelChange> implements
        SecurityLevelChangeService {

	public static final de.novanic.eventservice.client.event.domain.Domain CONVERSATION_DOMAIN;
	static {
		CONVERSATION_DOMAIN = DomainFactory
		        .getDomain(SecurityLevelChangePushEvent.CONVERSATION_DOMAIN);
	}

	@Override
	public SecurityLevelChangeDTO create(SecurityLevelChangeDTO toCreate, String sessionId) {
		buildConnection();
		SecurityLevelChange securityLevelChangeDB = SecurityLevelChange.findFirst("asset_id = ?",
		        toCreate.getAsset().getId());
		if (securityLevelChangeDB == null && User.sessionIdIsValid(sessionId)) {
			SecurityLevelChange newObject = createObject();
			toCreate = newObject.updateFromDTO(toCreate, pushUpdateHandler);
			addEvent(getConversationDomain(), getCreatedPushEvent(newObject.createSlimDTO()));
		} else {
			toCreate = securityLevelChangeDB.updateFromDTO(toCreate, pushUpdateHandler);
			addEvent(getConversationDomain(),
			        getUpdatedPushEvent(securityLevelChangeDB.createSlimDTO()));
		}
		closeConnection();

		return toCreate;
	}

	@Override
	public Boolean saveEditableList(Integer domain_id,
	        List<SecurityLevelChangeDTO> changeEventsToSave, String sessionId) {
		buildConnection();
		if (User.sessionIdIsValid(sessionId)) {
			for (SecurityLevelChangeDTO securityLevelDTO : changeEventsToSave) {
				if (securityLevelDTO.getReviewed()) {
					SecurityLevelChange securityLevelChangeDB = SecurityLevelChange
					        .findById(securityLevelDTO.getId());
					if (securityLevelChangeDB != null) {
						SecurityLevelChangeDTO dto = securityLevelChangeDB.createSlimDTO();
						securityLevelChangeDB.completeDelete(pushUpdateHandler);
						pushUpdateHandler.addEvent(CONVERSATION_DOMAIN, new SecurityLevelChangeDeletedPushEvent(dto));
					}
				}

			}
		}
		closeConnection();
		return true;
	}

	@Override
	protected Class<SecurityLevelChange> getDBClass() {
		return SecurityLevelChange.class;
	}

	@Override
	protected List<SecurityLevelChange> findAll() {
		return SecurityLevelChange.findAll();
	}

	@Override
	protected SecurityLevelChange findById(Integer id) {
		return SecurityLevelChange.findById(id);
	}

	@Override
	protected SecurityLevelChange createObject() {
		return SecurityLevelChange.createIt();
	}

	@Override
	protected Domain getConversationDomain() {
		return CONVERSATION_DOMAIN;
	}

	@Override
	protected PushEvent<?, ?> getUpdatedPushEvent(SecurityLevelChangeDTO updated) {
		return new SecurityLevelChangeUpdatedPushEvent(updated);
	}

	@Override
	protected PushEvent<?, ?> getCreatedPushEvent(SecurityLevelChangeDTO created) {
		return new SecurityLevelChangeCreatedPushEvent(created);
	}

	@Override
	protected PushEvent<?, ?> getDeletedPushEvent(SecurityLevelChangeDTO deleted) {
		return new SecurityLevelChangeDeletedPushEvent(deleted);
	}

}
