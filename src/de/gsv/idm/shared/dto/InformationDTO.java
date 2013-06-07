package de.gsv.idm.shared.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class InformationDTO extends GeneralDTO<InformationDTO> implements Serializable {

	private String name;
	private String ident;
	private SecurityLevelDTO availability;
	private String availabilityExplanation;
	private SecurityLevelDTO integrity;
	private String integrityExplanation;
	private SecurityLevelDTO confidentiality;
	private String confidentialityExplanation;
	private Integer id;
	private Integer domainId;
	private boolean personalData;

	private SecurityzoneDTO securityzone;

	private Boolean slim = false;

	public InformationDTO() {

	}

	public InformationDTO(int id, String name, SecurityzoneDTO securityzone) {
		this.id = id;
		this.name = name;
		this.securityzone = securityzone;

		slim = true;
	}

	public InformationDTO(int id, Integer domain_id, String name, String ident,
	        SecurityLevelDTO availability, String availabilityExplanation,
	        SecurityLevelDTO confidentiality, String confidentialityExplanation,
	        SecurityLevelDTO integrity, String integrityExplanation, boolean privateData,
	        SecurityzoneDTO securityzone) {
		this.id = id;
		this.domainId = domain_id;
		this.name = name;
		this.ident = ident;
		this.integrity = integrity;
		this.integrityExplanation = integrityExplanation;
		this.availability = availability;
		this.availabilityExplanation = availabilityExplanation;
		this.confidentiality = confidentiality;
		this.confidentialityExplanation = confidentialityExplanation;
		this.personalData = privateData;
		this.securityzone = securityzone;
	}

	public InformationDTO(Integer domain_id) {
		this.domainId = domain_id;
		integrity = SecurityLevelDTO.getDefaultSecurityLevel();
		availability = SecurityLevelDTO.getDefaultSecurityLevel();
		confidentiality = SecurityLevelDTO.getDefaultSecurityLevel();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIdent() {
		return ident;
	}

	public void setIdent(String ident) {
		this.ident = ident;
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

	public String getAvailabilityName() {
		if(availability == null){
			return "";
		} else {
			return availability.getName();
		}
		
	}

	public String getConfidentialityName() {
		if(confidentiality == null){
			return "";
		} else {
			return confidentiality.getName();
		}
	}

	public String getIntegrityName() {
		if(integrity == null){
			return "";
		} else {
			return integrity.getName();
		}
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDomainId() {
		return domainId;
	}

	public void setDomainId(Integer domain_id) {
		this.domainId = domain_id;
	}

	public boolean isPersonalData() {
		return personalData;
	}

	public void setPersonalData(boolean privateData) {
		this.personalData = privateData;
	}

	public SecurityzoneDTO getSecurityzone() {
		return securityzone;
	}
	
	public String getSecurityzoneName() {
		if(securityzone != null){
			return securityzone.getName();
		} else {
			return "";
		}		
	}

	public void setSecurityzone(SecurityzoneDTO securityzone) {
		this.securityzone = securityzone;
	}

	public List<MeasureDTO> getMeasures() {
		if (securityzone != null) {
			return securityzone.getAllMeasures();
		} else {
			return new ArrayList<MeasureDTO>();
		}

	}

	@Override
	public String getClassName() {
		return "Information";
	}

	public InformationDTO clone() {
		InformationDTO result = new InformationDTO(domainId);
		if (this.id != null) {
			result.setId(getId());
			result.setDomainId(getDomainId());
			result.setName(getName());
			result.setIdent(getIdent());
			result.setAvailability(getAvailability());
			result.setAvailabilityExplanation(getAvailabilityExplanation());
			result.setConfidentiality(getConfidentiality());
			result.setConfidentialityExplanation(getConfidentialityExplanation());
			result.setIntegrity(getIntegrity());
			result.setIntegrityExplanation(getIntegrityExplanation());
			result.setPersonalData(isPersonalData());
			result.setSecurityzone(getSecurityzone());
			
			result.slim = isSlim();
		}
		return result;
	}

	@Override
	public Boolean isSlim() {
		return slim;
	}

	@Override
	public InformationDTO update(InformationDTO toUpdate) {
		this.domainId = toUpdate.domainId;
		this.name = toUpdate.name;
		this.ident = toUpdate.ident;
		this.integrity = toUpdate.integrity;
		this.integrityExplanation = toUpdate.integrityExplanation;
		this.availability = toUpdate.availability;
		this.availabilityExplanation = toUpdate.availabilityExplanation;
		this.confidentiality = toUpdate.confidentiality;
		this.confidentialityExplanation = toUpdate.confidentialityExplanation;
		this.personalData = toUpdate.personalData;
		this.securityzone = toUpdate.securityzone;

		this.slim = toUpdate.slim;

		return this;
	}
}
