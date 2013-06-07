package de.gsv.idm.server.service;

import java.util.List;

import de.gsv.idm.client.services.SecurityzoneService;
import de.gsv.idm.server.general.GeneralServiceImpl;
import de.gsv.idm.shared.dto.SecurityzoneDTO;
import de.gsv.idm.shared.model.Securityzone;
import de.gsv.idm.shared.push.event.PushEvent;
import de.gsv.idm.shared.push.event.securityzone.SecurityzoneCreatedPushEvent;
import de.gsv.idm.shared.push.event.securityzone.SecurityzoneDeletedPushEvent;
import de.gsv.idm.shared.push.event.securityzone.SecurityzonePushEvent;
import de.gsv.idm.shared.push.event.securityzone.SecurityzoneUpdatedPushEvent;
import de.novanic.eventservice.client.event.domain.DomainFactory;

@SuppressWarnings("serial")
public class SecurityzoneServiceImpl extends GeneralServiceImpl<SecurityzoneDTO, Securityzone>
        implements SecurityzoneService {

	public static final de.novanic.eventservice.client.event.domain.Domain CONVERSATION_DOMAIN;
	static {
		CONVERSATION_DOMAIN = DomainFactory.getDomain(SecurityzonePushEvent.CONVERSATION_DOMAIN);
	}

	@Override
	protected List<Securityzone> findAll() {
		return Securityzone.findAll();
	}

	@Override
	protected Securityzone findById(Integer id) {
		return Securityzone.findById(id);
	}

	@Override
	protected Securityzone createObject() {
		Securityzone maxOrderNumberSecurityzone = Securityzone
		        .findFirst("order_number=(select max(order_number) from securityzones)");
		Integer maxOrderNumber = 0;
		if(maxOrderNumberSecurityzone != null){
			maxOrderNumber = maxOrderNumberSecurityzone.getInteger("order_number");
		}
		

		Securityzone securityZone = Securityzone.createIt("order_number", maxOrderNumber + 1);

		return securityZone;
	}

	@Override
	protected de.novanic.eventservice.client.event.domain.Domain getConversationDomain() {
		return CONVERSATION_DOMAIN;
	}

	@Override
	protected PushEvent<?, ?> getUpdatedPushEvent(SecurityzoneDTO updated) {
		return new SecurityzoneUpdatedPushEvent(updated);
	}

	@Override
	protected PushEvent<?, ?> getCreatedPushEvent(SecurityzoneDTO created) {
		return new SecurityzoneCreatedPushEvent(created);
	}

	@Override
	protected PushEvent<?, ?> getDeletedPushEvent(SecurityzoneDTO deleted) {
		return new SecurityzoneDeletedPushEvent(deleted);
	}

}
