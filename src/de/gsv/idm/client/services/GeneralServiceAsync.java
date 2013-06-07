package de.gsv.idm.client.services;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.gsv.idm.shared.dto.GeneralDTO;

public interface GeneralServiceAsync<T> {
	public void getAll(String sessionId, final AsyncCallback<ArrayList<T>> callback);

	public void getAllCount(String sessionId, final AsyncCallback<Integer> callback);

	public void getObject(final int id, String sessionId, final AsyncCallback<T> callback);

	void getAssociatedObjects(int id, String sessionId,
	        final AsyncCallback<List<GeneralDTO<?>>> callback);

	public void update(T toUpdate, String sessionId, AsyncCallback<T> callback);

	public void create(T toCreate, String sessionId, AsyncCallback<T> callback);

	public void delete(T toDelete, String sessionId, AsyncCallback<T> callback);
}
