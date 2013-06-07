package de.gsv.idm.shared.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ChangeTypeDTO implements Serializable {
	
	Integer id;
	String name;

	public ChangeTypeDTO() {

	}

	public ChangeTypeDTO(Integer id, String name) {
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

	public static ChangeTypeDTO getChangeType(Integer id) {
		if (id == null) {
			return null;
		}

		switch (id) {

		case 2:
			return new ChangeTypeDTO(2, "Dienstposten-Verknüpfungen");
		case 1:
			return new ChangeTypeDTO(1, "Asset-Verknüpfungen");
		default:
		case 0:
			return new ChangeTypeDTO(0, "Mitarbeiterdetails");
		}
	}

	@Override
	public String toString() {
		return getName();
	}
	
	public Boolean isEmployeeChange(){
		return id.equals(0);
	}

}
