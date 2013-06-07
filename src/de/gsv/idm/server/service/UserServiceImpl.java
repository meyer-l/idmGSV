package de.gsv.idm.server.service;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import org.mindrot.jbcrypt.BCrypt;

import de.gsv.idm.client.services.UserService;
import de.gsv.idm.server.general.DBServlet;
import de.gsv.idm.shared.dto.UserDTO;
import de.gsv.idm.shared.model.User;

@SuppressWarnings("serial")
public class UserServiceImpl extends DBServlet implements UserService {

	final static long HOUR = 1000 * 60 * 60;
	final static long SESSION_DURATION = HOUR * 8;
	final static long COOKIE_DURATION = HOUR * 24 * 3;

	private HashMap<String, Integer> loginFailuresMap = new HashMap<String, Integer>();

	@Override
	public UserDTO login(String username, String password, Boolean rememberMe) {

		Integer loginFailureCount = loginFailuresMap.get(username);
		if (loginFailureCount == null) {
			loginFailureCount = 0;
		}

		buildConnection();
		User user = User.findFirst("username = ?", username);
		UserDTO userDTO = null;
		// Create first User if there is no user in the DB
		if (user == null && User.findAll().isEmpty()) {
			user = User.createIt("username", username, "hash",
			        BCrypt.hashpw(password, BCrypt.gensalt()), "session_expires", null);
		}
		if (user != null) {
			String hashFromDB = user.getString("hash");
			boolean valid = BCrypt.checkpw(password, hashFromDB);
			if (valid && user != null) {
				Long uuid = UUID.randomUUID().getMostSignificantBits();
				user.set("session_id", uuid.toString());
				user.set("session_expires", new Date(System.currentTimeMillis() + SESSION_DURATION));
				uuid = UUID.randomUUID().getMostSignificantBits();
				if(rememberMe){
					user.set("cookie_id", uuid.toString());
					user.set("cookie_expires", new Date(System.currentTimeMillis() + COOKIE_DURATION));
				} else {
					user.set("cookie_id", null);
					user.set("cookie_expires", null);
				}				
				user.set("remember_me", rememberMe);
				user.saveIt();
				userDTO = user.createDTO();
				loginFailureCount = 0;
			} else {

				loginFailureCount += 1;
			}
		}

		// Brute-Force-Protection, taken from:
		// http://www.bryanrite.com/preventing-brute-force-attacks-on-your-web-login/
		try {

			if (loginFailureCount >= 30) { // Sleep between 30 seconds and 1 min
				                           // per
				                           // request
				Thread.sleep(Math.max(loginFailureCount * 1000, 60000));
			} else if (loginFailureCount >= 15) {
				// Sleep between ~9 and ~21 seconds
				Thread.sleep(loginFailureCount * 600);
			} else if (loginFailureCount >= 3) {
				// Sleep between 1.5 seconds and 7 seconds.
				Thread.sleep(loginFailureCount * 500);
			}
		} catch (Exception e) {
			System.err.println("Failure while sleeping to protect before brute-force");
		}
		closeConnection();
		loginFailuresMap.put(username, loginFailureCount);
		return userDTO;
	}

	@Override
	public Boolean logout(UserDTO userDTO) {
		buildConnection();
		User userDB = User.findById(userDTO.getId());
		if (userDB != null) {
			userDB.set("session_id", "");
			userDB.set("session_expires", null);
			userDB.saveIt();
		}
		closeConnection();
		return true;
	}

	@Override
	public UserDTO checkCookieId(String cookieId) {
		buildConnection();
		User userDB = User.findByCookieId(cookieId);
		UserDTO userDTO = null;
		if (userDB != null) {
			if (userDB.cookieIdIsValid()) {
				Long uuid = UUID.randomUUID().getMostSignificantBits();
				userDB.set("session_id", uuid.toString());
				userDB.set("session_expires", new Date(System.currentTimeMillis() + SESSION_DURATION));
				userDB.saveIt();
				
				userDTO = userDB.createDTO();
			}
		}
		closeConnection();
		return userDTO;
	}
}
