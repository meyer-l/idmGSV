package de.gsv.idm.shared.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class AssetModuleLinkDTO extends GeneralDTO<AssetModuleLinkDTO> implements Serializable {

	private AssettypeModuleLinkDTO assettypeModuleLink;
	private Integer id;
	private EmployeeDTO responsiblePerson;

	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (other instanceof AssetModuleLinkDTO)
			return (getId() != null && getId() == ((HasId) other).getId())
			        || getAssettypeModuleLink().equals(
			                ((AssetModuleLinkDTO) other).getAssettypeModuleLink());
		if (other instanceof HasId)
			return (getId() != null && getId() == ((HasId) other).getId());
		return false;
	}
	
	public AssetModuleLinkDTO() {

	}

	public AssetModuleLinkDTO(AssettypeModuleLinkDTO assetModuleLink, Integer id,
	        EmployeeDTO responsiblePerson) {
		this.assettypeModuleLink = assetModuleLink;
		this.id = id;
		this.responsiblePerson = responsiblePerson;
	}
	
	public AssetModuleLinkDTO(AssettypeModuleLinkDTO assetModuleLink,
	        EmployeeDTO responsiblePerson) {
		this.assettypeModuleLink = assetModuleLink;
		this.responsiblePerson = responsiblePerson;
	}

	@Override
	public String getName() {
		return assettypeModuleLink.getName();
	}

	public AssettypeModuleLinkDTO getAssettypeModuleLink() {
		return assettypeModuleLink;
	}

	public void setAssettypeModuleLink(AssettypeModuleLinkDTO assettypeModuleLink) {
		this.assettypeModuleLink = assettypeModuleLink;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public EmployeeDTO getResponsiblePerson() {
		if (responsiblePerson != null) {
			return responsiblePerson;
		} else {
			return assettypeModuleLink.getResponsiblePerson();
		}
	}

	public EmployeeDTO getModelResponsiblePerson() {
		return responsiblePerson;
	}

	public void setResponsiblePerson(EmployeeDTO responsiblePerson) {
		this.responsiblePerson = responsiblePerson;
	}

	@Override
	public String getClassName() {
		return "Baustein-Verknüpfung";
	}

	@Override
	public Boolean isSlim() {
		return false;
	}

	@Override
	public AssetModuleLinkDTO update(AssetModuleLinkDTO toUpdate) {
		this.assettypeModuleLink = toUpdate.assettypeModuleLink;
		this.responsiblePerson = toUpdate.responsiblePerson;

		return this;
	}

}
