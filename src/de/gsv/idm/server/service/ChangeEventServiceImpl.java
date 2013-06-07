package de.gsv.idm.server.service;

import java.util.ArrayList;
import java.util.List;

import de.gsv.idm.client.services.ChangeEventService;
import de.gsv.idm.server.general.GeneralDomainServiceImpl;
import de.gsv.idm.shared.dto.ChangeAppliedDTO;
import de.gsv.idm.shared.dto.ChangeEventDTO;
import de.gsv.idm.shared.model.ChangeEvent;
import de.gsv.idm.shared.model.User;
import de.gsv.idm.shared.push.event.PushEvent;
import de.gsv.idm.shared.push.event.change.event.ChangeEventCreatedPushEvent;
import de.gsv.idm.shared.push.event.change.event.ChangeEventDeletedPushEvent;
import de.gsv.idm.shared.push.event.change.event.ChangeEventPushEvent;
import de.gsv.idm.shared.push.event.change.event.ChangeEventUpdatedPushEvent;
import de.novanic.eventservice.client.event.domain.DomainFactory;

@SuppressWarnings("serial")
public class ChangeEventServiceImpl extends GeneralDomainServiceImpl<ChangeEventDTO, ChangeEvent>
        implements ChangeEventService {

	public static final de.novanic.eventservice.client.event.domain.Domain CONVERSATION_DOMAIN;
	static {
		CONVERSATION_DOMAIN = DomainFactory.getDomain(ChangeEventPushEvent.CONVERSATION_DOMAIN);
	}

	@Override
	protected Class<ChangeEvent> getDBClass() {
		return ChangeEvent.class;
	}

	@Override
	protected List<ChangeEvent> findAll() {
		return ChangeEvent.findAll();
	}

	@Override
	protected ChangeEvent findById(Integer id) {
		return ChangeEvent.findById(id);
	}

	@Override
	protected ChangeEvent createObject() {
		return ChangeEvent.createIt();
	}

	@Override
	public List<ChangeEventDTO> getAllNotProcessed(Integer domain_id, String sessionId) {
		buildConnection();
		ArrayList<ChangeEventDTO> changeEventsDTO = new ArrayList<ChangeEventDTO>();
		if (User.sessionIdIsValid(sessionId)) {
			List<ChangeEvent> changeEventsDB = ChangeEvent.where(
			        "domain_id = ? and process_type = ?", domain_id,
			        ChangeEventDTO.ProcessType.NotProcessed.toString());
			for (ChangeEvent changeEventDB : changeEventsDB) {
				ChangeEventDTO changeEventDTO = changeEventDB.createFullDTO();
				if (changeEventDTO != null) {
					changeEventsDTO.add(changeEventDTO);
				}
			}
		}
		closeConnection();
		return changeEventsDTO;
	}

	@Override
	public List<ChangeEventDTO> getAllManual(Integer domain_id, String sessionId) {
		buildConnection();
		ArrayList<ChangeEventDTO> changeEventsDTO = new ArrayList<ChangeEventDTO>();
		if (User.sessionIdIsValid(sessionId)) {
			List<ChangeEvent> changeEventsDB = ChangeEvent.where(
			        "domain_id = ? and process_type = ?", domain_id,
			        ChangeEventDTO.ProcessType.Manual.toString());
			for (ChangeEvent changeEventDB : changeEventsDB) {
				ChangeEventDTO changeEventDTO = changeEventDB.createFullDTO();
				if (changeEventDTO != null) {
					changeEventsDTO.add(changeEventDTO);
				}
			}
		}
		closeConnection();
		return changeEventsDTO;
	}

	@Override
	public List<ChangeEventDTO> getAllNotNeeded(Integer domain_id, String sessionId) {
		buildConnection();
		ArrayList<ChangeEventDTO> changeEventsDTO = new ArrayList<ChangeEventDTO>();
		if (User.sessionIdIsValid(sessionId)) {
			List<ChangeEvent> changeEventsDB = ChangeEvent.where(
			        "domain_id = ? and process_type = ?", domain_id,
			        ChangeEventDTO.ProcessType.NotNeeded.toString());

			for (ChangeEvent changeEventDB : changeEventsDB) {
				ChangeEventDTO changeEventDTO = changeEventDB.createFullDTO();
				if (changeEventDTO != null) {
					changeEventsDTO.add(changeEventDTO);
				}
			}
		}
		closeConnection();
		return changeEventsDTO;
	}

	@Override
	public List<ChangeEventDTO> getAllApplied(Integer domain_id, String sessionId) {
		buildConnection();
		ArrayList<ChangeEventDTO> changeEventsDTO = new ArrayList<ChangeEventDTO>();
		if (User.sessionIdIsValid(sessionId)) {
			List<ChangeEvent> changeEventsDB = ChangeEvent.where(
			        "domain_id = ? and applied = true", domain_id);
			for (ChangeEvent changeEventDB : changeEventsDB) {
				ChangeEventDTO changeEventDTO = changeEventDB.createFullDTO();
				if (changeEventDTO != null) {
					changeEventsDTO.add(changeEventDTO);
				}
			}
		}
		closeConnection();
		return changeEventsDTO;
	}

	@Override
	public List<ChangeAppliedDTO> saveEditableList(Integer domain_id,
	        List<ChangeEventDTO> changeEventsToSave, String sessionId) {
		ArrayList<ChangeAppliedDTO> changedItems = new ArrayList<ChangeAppliedDTO>();
		ArrayList<ChangeEvent> changedItemsToApply = new ArrayList<ChangeEvent>();
		ArrayList<ChangeEventDTO> updatedEvents = new ArrayList<ChangeEventDTO>();
		buildConnection();
		if (User.sessionIdIsValid(sessionId)) {
			for (ChangeEventDTO changeEventDTO : changeEventsToSave) {
				ChangeEvent changeEventDB = ChangeEvent.findById(changeEventDTO.getId());
				if (changeEventDB != null) {
					Boolean oldApplied = changeEventDB.getBoolean("applied");
					ChangeEventDTO newChangeEvent = changeEventDB.updateFromDTO(changeEventDTO,
							pushUpdateHandler);
					if ((oldApplied == null || oldApplied == false) && newChangeEvent.isAutomatic()) {
						changedItemsToApply.add(changeEventDB);
					} else {
						updatedEvents.add(newChangeEvent);
					}

				}
			}
			for (ChangeEvent changeEventDB : changedItemsToApply) {
				List<ChangeAppliedDTO> temp = changeEventDB.applyChange(pushUpdateHandler);
				temp.removeAll(changedItems);
				changedItems.addAll(temp);
				updatedEvents.add(changeEventDB.createSlimDTO());
			}
			for (ChangeEventDTO event : updatedEvents) {
				pushUpdateHandler.addPushEvent(CONVERSATION_DOMAIN, new ChangeEventUpdatedPushEvent(event));
			}
		}
		closeConnection();

		return changedItems;
	}

	@Override
	protected de.novanic.eventservice.client.event.domain.Domain getConversationDomain() {
		return CONVERSATION_DOMAIN;
	}

	@Override
	protected PushEvent<?, ?> getUpdatedPushEvent(ChangeEventDTO updated) {
		return new ChangeEventUpdatedPushEvent(updated);
	}

	@Override
	protected PushEvent<?, ?> getCreatedPushEvent(ChangeEventDTO created) {
		return new ChangeEventCreatedPushEvent(created);
	}

	@Override
	protected PushEvent<?, ?> getDeletedPushEvent(ChangeEventDTO deleted) {
		return new ChangeEventDeletedPushEvent(deleted);
	}

}
