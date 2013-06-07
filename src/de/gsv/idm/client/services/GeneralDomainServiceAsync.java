package de.gsv.idm.client.services;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GeneralDomainServiceAsync<T> extends GeneralServiceAsync<T> {
	public void getAll(Integer domain_id, String sessionId, AsyncCallback<ArrayList<T>> callback);
}
