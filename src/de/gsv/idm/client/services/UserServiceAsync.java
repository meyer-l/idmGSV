package de.gsv.idm.client.services;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.gsv.idm.shared.dto.UserDTO;

public interface UserServiceAsync {
	void login(String user, String password, Boolean rememberMe, final AsyncCallback<UserDTO> callback);
	void logout(UserDTO user, final AsyncCallback<Boolean> callback);
	void checkCookieId(String sessionId, final AsyncCallback<UserDTO> callback);
}
