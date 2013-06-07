package de.gsv.idm.shared.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class AssettypeCategoryDTO extends GeneralDTO<AssettypeCategoryDTO> implements Serializable{

	private String name;
	private String iconName;
	private Integer id;
	
	public AssettypeCategoryDTO(){
		
	}
	
	public AssettypeCategoryDTO(Integer id, String iconName){
		this.id = id;
		this.iconName = iconName;
	}

	public AssettypeCategoryDTO(Integer id, String name, String iconName){
		this.name = name;
		this.iconName = iconName;
		this.id = id;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
    	this.name = name;
    }

	@Override
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
    	this.id = id;
    }
	
	public String getIconName() {
	    return iconName;
    }

	public void setIconName(String iconName) {
	    this.iconName = iconName;
    }

	@Override
	public String getTreeKey() {
		return "assetCategory" + id;
	}

	@Override
	public String getClassName() {
		return "Asset-Kategorie";
	}

	@Override
	public Boolean isSlim() {
		return false;
	}

	@Override
	public AssettypeCategoryDTO update(AssettypeCategoryDTO toUpdate) {
		this.name = toUpdate.name;
		this.iconName = toUpdate.iconName;
		return this;
	}
	
	public AssettypeCategoryDTO clone(){
		AssettypeCategoryDTO clone = new AssettypeCategoryDTO();
		if(id != null) {
			clone.id = this.id;
			clone.name = this.name;
			clone.iconName = this.iconName;
		}
		return clone;
	}

}
