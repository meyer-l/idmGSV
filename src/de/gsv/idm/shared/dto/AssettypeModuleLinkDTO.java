package de.gsv.idm.shared.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class AssettypeModuleLinkDTO extends GeneralDTO<AssettypeModuleLinkDTO> implements
        Serializable {

	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (other instanceof AssettypeModuleLinkDTO)
			return (getId() != null && getId() == ((HasId) other).getId())
			        || getModule().equals(((AssettypeModuleLinkDTO) other).getModule());
		if (other instanceof HasId)
			return (getId() != null && getId() == ((HasId) other).getId());
		return false;
	}

	private ModuleDTO module;
	private Integer id;
	private EmployeeDTO responsiblePerson;

	public AssettypeModuleLinkDTO() {

	}

	public AssettypeModuleLinkDTO(ModuleDTO module) {
		this.module = module;
	}

	public AssettypeModuleLinkDTO(ModuleDTO module, Integer id, EmployeeDTO responsiblePerson) {
		this.module = module;
		this.id = id;
		this.responsiblePerson = responsiblePerson;
	}

	public String getName() {
		return module.getName();
	}

	@Override
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ModuleDTO getModule() {
		return module;
	}

	public void setModule(ModuleDTO module) {
		this.module = module;
	}

	public EmployeeDTO getResponsiblePerson() {
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

	public AssettypeModuleLinkDTO clone() {
		AssettypeModuleLinkDTO result = new AssettypeModuleLinkDTO();
		result.id = this.id;
		if (responsiblePerson != null) {
			result.responsiblePerson = this.responsiblePerson;
		}

		result.module = this.module;

		return result;

	}

	@Override
	public AssettypeModuleLinkDTO update(AssettypeModuleLinkDTO toUpdate) {
		this.module = toUpdate.module;
		this.responsiblePerson = toUpdate.responsiblePerson;

		return this;
	}
}
