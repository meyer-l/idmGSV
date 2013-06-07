package de.gsv.idm.client.services;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.gsv.idm.shared.dto.UserDTO;

@RemoteServiceRelativePath("userService")
public interface UserService extends RemoteService {
	UserDTO login(String user, String password, Boolean rememberMe);
	Boolean logout(UserDTO user);
	UserDTO checkCookieId(String sessionId);
}
