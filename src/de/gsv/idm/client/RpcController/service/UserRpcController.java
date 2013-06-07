package de.gsv.idm.client.RpcController.service;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import de.gsv.idm.client.services.UserService;
import de.gsv.idm.client.services.UserServiceAsync;
import de.gsv.idm.shared.dto.UserDTO;

public class UserRpcController implements UserServiceAsync {
	
	private final UserServiceAsync userService;
	
	public UserRpcController(){
		this.userService = GWT.create(UserService.class);
	}

	@Override
	public void login(String username, String password, Boolean rememberMe, final AsyncCallback<UserDTO> callback) {
		userService.login(username, password, rememberMe, new AsyncCallback<UserDTO>() {

			@Override
            public void onFailure(Throwable caught) {
				callback.onFailure(caught);
            }

			@Override
            public void onSuccess(UserDTO result) {
	           callback.onSuccess(result);
            }
		});
	}

	@Override
	public void logout(UserDTO user, final AsyncCallback<Boolean> callback) {
		userService.logout(user, new AsyncCallback<Boolean>() {

			@Override
            public void onFailure(Throwable caught) {
				callback.onFailure(caught);
            }

			@Override
            public void onSuccess(Boolean result) {
	           callback.onSuccess(result);
            }
		});
	}

	@Override
	public void checkCookieId(String sessionId, final AsyncCallback<UserDTO> callback) {
		userService.checkCookieId(sessionId, new AsyncCallback<UserDTO>() {

			@Override
            public void onFailure(Throwable caught) {
				callback.onFailure(caught);
            }

			@Override
            public void onSuccess(UserDTO result) {
	           callback.onSuccess(result);
            }
		});
	}

}
