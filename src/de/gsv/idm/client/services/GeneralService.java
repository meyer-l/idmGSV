package de.gsv.idm.client.services;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;

import de.gsv.idm.shared.dto.GeneralDTO;

public interface GeneralService<T> extends RemoteService {
	ArrayList<T> getAll(String sessionId);
	T getObject(int id, String sessionId);
	List<GeneralDTO<?>> getAssociatedObjects(int id, String sessionId);
	T update(T toUpdate, String sessionId);
	T create(T toCreate, String sessionId);
	T delete(T toDelete, String sessionId);
	Integer getAllCount(String sessionId);
}
