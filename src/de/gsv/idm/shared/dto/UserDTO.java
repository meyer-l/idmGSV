package de.gsv.idm.shared.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class UserDTO implements Serializable {
	private Integer id;
	private String sessionId;
	private String cookieId;
	private String username;
	private Boolean rememberMe;
	
	public UserDTO(){
		
	}
	
	public UserDTO(Integer id, String username, String sessionId, String cookieId, Boolean rememberMe) {
		this.id = id;
		this.username = username;
		this.sessionId = sessionId;
		this.cookieId = cookieId;
		this.rememberMe = rememberMe;
	}

	public String getSessionId() {
    	return sessionId;
    }

	public void setSessionId(String sessionId) {
    	this.sessionId = sessionId;
    }
	
	public String getCookieId() {
    	return cookieId;
    }

	public void setCookieId(String cookieId) {
    	this.cookieId = cookieId;
    }

	public Boolean getRememberMe() {
    	return rememberMe;
    }

	public void setRememberMe(Boolean rememberMe) {
    	this.rememberMe = rememberMe;
    }

	public Integer getId() {
    	return id;
    }

	public void setId(Integer id) {
    	this.id = id;
    }

	public String getUsername() {
    	return username;
    }

	public void setUsername(String username) {
    	this.username = username;
    }
	
}
