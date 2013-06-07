package de.gsv.idm.shared.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class SecurityLevelDTO implements Serializable {
	private Integer id;
	private String name;

	public SecurityLevelDTO() {

	}

	public SecurityLevelDTO(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return getName();
	}

	public static SecurityLevelDTO getSecurityLevel(Integer id) {
		if (id == null) {
			return getDefaultSecurityLevel();
		}

		switch (id) {

		case 2:
			return new SecurityLevelDTO(2, "Hoch");
		case 3:
			return new SecurityLevelDTO(3, "Sehr Hoch");
		default:
		case 1:
			return new SecurityLevelDTO(1, "Normal");
		}
	}

	public static SecurityLevelDTO getDefaultSecurityLevel() {
		return getSecurityLevel(1);
	}

	@Override
	public String toString() {
		return getName();
	}
}
