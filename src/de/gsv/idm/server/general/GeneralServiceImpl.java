package de.gsv.idm.server.general;

import java.util.ArrayList;
import java.util.List;

import de.gsv.idm.client.services.GeneralService;
import de.gsv.idm.server.push.PushUpdateHandler;
import de.gsv.idm.shared.dto.GeneralDTO;
import de.gsv.idm.shared.model.HasDTOMethods;
import de.gsv.idm.shared.model.User;
import de.gsv.idm.shared.push.event.PushEvent;

@SuppressWarnings("serial")
public abstract class GeneralServiceImpl<T extends GeneralDTO<T>, V extends HasDTOMethods<T>>
        extends DBServlet implements GeneralService<T> {

	protected PushUpdateHandler pushUpdateHandler;

	public GeneralServiceImpl() {
		pushUpdateHandler = PushUpdateHandler.getInstance();
	}

	@Override
	public ArrayList<T> getAll(String sessionId) {
		ArrayList<T> objectsDTO = new ArrayList<T>();

		buildConnection();
		if (User.sessionIdIsValid(sessionId)) {
			List<V> objectsDB = findAll();
			for (V objectDB : objectsDB) {
				T objectDTO = objectDB.createFullDTO();
				objectsDTO.add(objectDTO);
			}
		}
		closeConnection();

		return objectsDTO;
	}

	@Override
	public T getObject(int id, String sessionId) {
		buildConnection();
		V objectDB = findById(id);
		T objectDTO;
		if (objectDB != null && User.sessionIdIsValid(sessionId)) {
			objectDTO = objectDB.createFullDTO();
		} else {
			objectDTO = null;
		}

		closeConnection();

		return objectDTO;
	}

	@Override
	public List<GeneralDTO<?>> getAssociatedObjects(int id, String sessionId) {
		buildConnection();
		V objectDB = findById(id);
		List<GeneralDTO<?>> result = new ArrayList<GeneralDTO<?>>();
		if (id > 0 && objectDB != null && objectDB != null && User.sessionIdIsValid(sessionId)) {
			result.addAll(objectDB.getAffectedObjects());
		}

		closeConnection();
		return result;
	}

	@Override
	public T update(T toUpdate, String sessionId) {
		buildConnection();
		V update = findById(toUpdate.getId());
		if (update != null && User.sessionIdIsValid(sessionId)) {
			toUpdate = update.updateFromDTO(toUpdate, pushUpdateHandler);
			pushUpdateHandler.addEvent(getConversationDomain(),
			        getUpdatedPushEvent(update.createSlimDTO()));
		} else {
			toUpdate = null;
		}
		closeConnection();
		return toUpdate;
	}

	@Override
	public T create(T toCreate, String sessionId) {
		buildConnection();
		if (User.sessionIdIsValid(sessionId)) {
			V newObject = createObject();
			toCreate = newObject.updateFromDTO(toCreate, pushUpdateHandler);
			pushUpdateHandler.addEvent(getConversationDomain(),
			        getCreatedPushEvent(newObject.createSlimDTO()));
		}
		closeConnection();
		return toCreate;
	}

	@Override
	public T delete(T toDelete, String sessionId) {
		buildConnection();
		if (User.sessionIdIsValid(sessionId)) {
			V deleteDB = findById(toDelete.getId());
			if (deleteDB != null) {
				toDelete = deleteDB.createSlimDTO();
				findById(toDelete.getId()).completeDelete(pushUpdateHandler);
				pushUpdateHandler.addEvent(getConversationDomain(), getDeletedPushEvent(toDelete));
			}
			closeConnection();
		}
		return toDelete;
	}

	@Override
	public Integer getAllCount(String sessionId) {
		buildConnection();
		if (User.sessionIdIsValid(sessionId)) {
			List<V> objects = findAll();
			// No close because its messes up, seems to be a timing issue with
			// the open and close call.
			return objects.size();
		} else {
			return 0;
		}
	}

	protected abstract List<V> findAll();

	protected abstract V findById(Integer id);

	protected abstract V createObject();

	protected abstract de.novanic.eventservice.client.event.domain.Domain getConversationDomain();

	protected abstract PushEvent<?, ?> getUpdatedPushEvent(T updated);

	protected abstract PushEvent<?, ?> getCreatedPushEvent(T created);

	protected abstract PushEvent<?, ?> getDeletedPushEvent(T deleted);

}
