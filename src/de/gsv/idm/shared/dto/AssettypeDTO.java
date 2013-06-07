package de.gsv.idm.shared.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class AssettypeDTO extends GeneralDTO<AssettypeDTO> implements Serializable {

	private String name;
	private AssettypeCategoryDTO category;
	private String iconName;
	private Integer id;
	private Integer domainId;
	private boolean itAsset;
	private String location;
	private String description;
	private String architecture;
	private String status;
	private boolean propagateSecurityAssesment;
	private boolean manuelSecurityAssesment;
	private SecurityLevelDTO availability;
	private String availabilityExplanation;
	private SecurityLevelDTO integrity;
	private String integrityExplanation;
	private SecurityLevelDTO confidentiality;
	private String confidentialityExplanation;
	private EmployeeDTO responsiblePerson;

	private List<AssettypeModuleLinkDTO> modules;
	private List<AssettypeMeasureLinkDTO> measures;
	private List<AssetDTO> linkedAssets;
	
	private Boolean personalData;

	private Boolean slim = false;

	public AssettypeDTO() {

	}

	public AssettypeDTO(Integer domainId) {
		this.domainId = domainId;
		propagateSecurityAssesment = true;
		manuelSecurityAssesment = false;
		availability = SecurityLevelDTO.getDefaultSecurityLevel();
		confidentiality = SecurityLevelDTO.getDefaultSecurityLevel();
		integrity = SecurityLevelDTO.getDefaultSecurityLevel();
		modules = new ArrayList<AssettypeModuleLinkDTO>();
		measures = new ArrayList<AssettypeMeasureLinkDTO>();
		linkedAssets = new ArrayList<AssetDTO>();
	}

	public AssettypeDTO(Integer id, Integer domainId, String name, AssettypeCategoryDTO category,
	        String iconName, Boolean propagateSecurityAssesment, Boolean manuelSecurityAssesment,
	        SecurityLevelDTO availability, SecurityLevelDTO confidentiality,
	        SecurityLevelDTO integrity, Boolean privateData) {
		this.name = name;
		this.category = category;
		this.iconName = iconName;
		this.id = id;
		this.domainId = domainId;
		this.propagateSecurityAssesment = propagateSecurityAssesment;
		this.manuelSecurityAssesment = manuelSecurityAssesment;
		this.slim = true;
		this.availability = availability;
		this.confidentiality = confidentiality;
		this.integrity = integrity;
		this.personalData = privateData;
		modules = new ArrayList<AssettypeModuleLinkDTO>();
		measures = new ArrayList<AssettypeMeasureLinkDTO>();
		linkedAssets = new ArrayList<AssetDTO>();		
	}

	public AssettypeDTO(Integer id, Integer domainId, String name, AssettypeCategoryDTO category,
	        String iconName, String location, String description, Boolean itAsset,
	        String architecture, String status, Boolean propagateSecurityAssesment,
	        Boolean manuelSecurityAssesment, SecurityLevelDTO availability,
	        String availabilityExplanation, SecurityLevelDTO confidentiality,
	        String confidentialityExplanation, SecurityLevelDTO integrity,
	        String integrityExplanation, List<AssettypeModuleLinkDTO> modules,
	        List<AssettypeMeasureLinkDTO> measures, EmployeeDTO responsiblePerson,
	        List<AssetDTO> linkedModels, Boolean privateData) {
		this.name = name;
		this.category = category;
		this.iconName = iconName;
		this.id = id;
		this.domainId = domainId;
		this.location = location;
		this.description = description;
		this.itAsset = itAsset;
		this.architecture = architecture;
		this.status = status;
		this.propagateSecurityAssesment = propagateSecurityAssesment;
		this.manuelSecurityAssesment = manuelSecurityAssesment;
		this.integrity = integrity;
		this.integrityExplanation = integrityExplanation;
		this.availability = availability;
		this.availabilityExplanation = availabilityExplanation;
		this.confidentiality = confidentiality;
		this.confidentialityExplanation = confidentialityExplanation;
		this.modules = modules;
		this.measures = measures;
		this.responsiblePerson = responsiblePerson;
		this.linkedAssets = linkedModels;
		this.personalData = privateData;
	}
	
	public AssettypeDTO(Integer id, String name){
		this.id = id;
		this.name = name;
	}

	public Integer getDomainId() {
		return domainId;
	}

	public void setDomainId(Integer domainId) {
		this.domainId = domainId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIconName() {
		return iconName;
	}

	public void setIconName(String iconName) {
		this.iconName = iconName;
	}

	public String getTreeIconName() {
		if (iconName != null && !iconName.equals("")) {
			return iconName;
		} else if (category != null) {
			return category.getIconName();
		} else {
			return "";
		}
	}

	@Override
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String getTreeKey() {
		return "assettype" + id;
	}

	public AssettypeCategoryDTO getCategory() {
		return category;
	}

	public void setCategory(AssettypeCategoryDTO category) {
		this.category = category;
	}

	public boolean isItAsset() {
		return itAsset;
	}

	public void setItAsset(boolean itAsset) {
		this.itAsset = itAsset;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getArchitecture() {
		return architecture;
	}

	public void setArchitecture(String architecture) {
		this.architecture = architecture;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isPropagateSecurityAssesment() {
		return propagateSecurityAssesment;
	}

	public void setPropagateSecurityAssesment(boolean propagateSecurityAssesment) {
		this.propagateSecurityAssesment = propagateSecurityAssesment;
	}

	public boolean isManuelSecurityAssesment() {
		return manuelSecurityAssesment;
	}

	public void setManuelSecurityAssesment(boolean manuelSecurityAssesment) {
		this.manuelSecurityAssesment = manuelSecurityAssesment;
	}

	public SecurityLevelDTO getAvailability() {
		return availability;
	}

	public void setAvailability(SecurityLevelDTO availability) {
		this.availability = availability;
	}

	public SecurityLevelDTO getConfidentiality() {
		return confidentiality;
	}
	
	public SecurityLevelDTO getCalculatedConfidentiality() {
		if(personalData){
			return SecurityLevelDTO.getSecurityLevel(3);
		} else {
			return confidentiality;
		}
		
	}

	public void setConfidentiality(SecurityLevelDTO confidentiality) {
		this.confidentiality = confidentiality;
	}

	public SecurityLevelDTO getIntegrity() {
		return integrity;
	}

	public void setIntegrity(SecurityLevelDTO integrity) {
		this.integrity = integrity;
	}

	public String getAvailabilityExplanation() {
		return availabilityExplanation;
	}

	public void setAvailabilityExplanation(String availabilityExplanation) {
		this.availabilityExplanation = availabilityExplanation;
	}

	public String getIntegrityExplanation() {
		return integrityExplanation;
	}

	public void setIntegrityExplanation(String integrityExplanation) {
		this.integrityExplanation = integrityExplanation;
	}

	public String getConfidentialityExplanation() {
		return confidentialityExplanation;
	}

	public void setConfidentialityExplanation(String confidentialityExplanation) {
		this.confidentialityExplanation = confidentialityExplanation;
	}

	public List<AssettypeModuleLinkDTO> getModules() {
		return modules;
	}

	public void setModules(List<AssettypeModuleLinkDTO> modules) {
		this.modules = modules;
	}

	public EmployeeDTO getResponsiblePerson() {
		return responsiblePerson;
	}

	public void setResponsiblePerson(EmployeeDTO responsiplePerson) {
		this.responsiblePerson = responsiplePerson;
	}

	public List<AssettypeMeasureLinkDTO> getMeasures() {
		return measures;
	}

	public void setMeasures(List<AssettypeMeasureLinkDTO> measures) {
		this.measures = measures;
	}

	public List<AssetDTO> getLinkedAssets() {
		return linkedAssets;
	}

	public void setLinkedAssets(List<AssetDTO> linkedModels) {
		this.linkedAssets = linkedModels;
	}
	
	public Boolean isPersonalData() {
		return personalData;
	}

	public void setPersonalData(Boolean privateData) {
		this.personalData = privateData;
	}

	@Override
	public String getClassName() {
		return getStaticClassName();
	}

	public static String getStaticClassName() {
		return "Grundtyp";
	}

	public String getLabel() {
		return name + " (" + linkedAssets.size() + ")";
	}

	public String getTreeToListLabel() {
		return getName();
	}

	@Override
	public Boolean isSlim() {
		return slim;
	}

	public AssettypeDTO update(AssettypeDTO toUpdate) {
		this.domainId = toUpdate.domainId;
		this.name = toUpdate.name;
		this.category = toUpdate.category;
		this.iconName = toUpdate.iconName;
		this.location = toUpdate.location;
		this.description = toUpdate.description;
		this.itAsset = toUpdate.itAsset;
		this.architecture = toUpdate.architecture;
		this.status = toUpdate.status;
		this.propagateSecurityAssesment = toUpdate.propagateSecurityAssesment;
		this.manuelSecurityAssesment = toUpdate.manuelSecurityAssesment;
		this.integrity = toUpdate.integrity;
		this.integrityExplanation = toUpdate.integrityExplanation;
		this.availability = toUpdate.availability;
		this.availabilityExplanation = toUpdate.availabilityExplanation;
		this.confidentiality = toUpdate.confidentiality;
		this.confidentialityExplanation = toUpdate.confidentialityExplanation;
		this.responsiblePerson = toUpdate.responsiblePerson;
		this.modules = toUpdate.modules;
		this.measures = toUpdate.measures;
		this.linkedAssets = toUpdate.linkedAssets;
		this.personalData = toUpdate.personalData;
		
		this.slim = toUpdate.slim;
		return this;
	}

	public AssettypeDTO clone() {
		AssettypeDTO result = new AssettypeDTO(domainId);
		if (id != null) {
			result.id = this.id;
			result.domainId = this.domainId;
			result.name = this.name;
			result.category = this.category;
			result.iconName = this.iconName;
			result.location = this.location;
			result.description = this.description;
			result.itAsset = this.itAsset;
			result.architecture = this.architecture;
			result.status = this.status;
			result.propagateSecurityAssesment = this.propagateSecurityAssesment;
			result.manuelSecurityAssesment = this.manuelSecurityAssesment;
			result.setAvailability(getAvailability());
			result.setAvailabilityExplanation(getAvailabilityExplanation());
			result.setConfidentiality(getConfidentiality());
			result.setConfidentialityExplanation(getConfidentialityExplanation());
			result.setIntegrity(getIntegrity());
			result.setIntegrityExplanation(getIntegrityExplanation());
			if (responsiblePerson != null) {
				result.responsiblePerson = responsiblePerson.clone();
			}
			List<AssettypeModuleLinkDTO> clonedModules = new ArrayList<AssettypeModuleLinkDTO>();
			for (AssettypeModuleLinkDTO module : this.modules) {
				clonedModules.add(module);
			}
			result.modules = clonedModules;
			List<AssettypeMeasureLinkDTO> clonedMeasures = new ArrayList<AssettypeMeasureLinkDTO>();
			for (AssettypeMeasureLinkDTO measure : this.measures) {
				clonedMeasures.add(measure);
			}
			result.measures = clonedMeasures;

			List<AssetDTO> clonedLinkedModels = new ArrayList<AssetDTO>();
			for (AssetDTO model : this.linkedAssets) {
				clonedLinkedModels.add(model);
			}
			result.linkedAssets = clonedLinkedModels;
			result.setPersonalData(isPersonalData());
		}

		return result;
	}

	@Override
	public String toString() {
		return getName();
	}
}
