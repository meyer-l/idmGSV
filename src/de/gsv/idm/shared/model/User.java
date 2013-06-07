package de.gsv.idm.shared.model;

import java.util.Date;

import org.javalite.activejdbc.Model;

import de.gsv.idm.shared.dto.UserDTO;

public class User extends Model {

	public UserDTO createDTO() {

		return new UserDTO(getInteger("id"), getString("username"), getString("session_id"),
		        getString("cookie_id"), getBoolean("remember_me"));
	}

	public boolean sessionIdIsValid() {
		if (new Date(System.currentTimeMillis()).before(getTimestamp("session_expires"))) {
			return true;
		} else {
			set("session_id", "");
			set("session_expires", null);
			saveIt();
			return false;
		}
	}

	public static Boolean sessionIdIsValid(String sessionId) {
		User user = findBySessionId(sessionId);
		if (user != null) {
			return user.sessionIdIsValid();
		} else {
			return false;
		}
	}

	public static User findBySessionId(String sessionId) {
		return User.findFirst("session_id = ?", sessionId);
	}
	
	public static User findByCookieId(String cookieId) {
		return User.findFirst("cookie_id = ?", cookieId);
	}

	public boolean cookieIdIsValid() {
		if (new Date(System.currentTimeMillis()).before(getTimestamp("cookie_expires"))) {
			return true;
		} else {
			set("cookie_id", "");
			set("cookie_expires", null);
			saveIt();
			return false;
		}
    }
}
