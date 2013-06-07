package de.gsv.idm.shared.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class OccupationDTO extends GeneralDTO<OccupationDTO> implements Serializable {

	private Integer id;
	private Integer domain_id;
	private String name;
	private String ident;
	private boolean manuelSecurityAssesment = false;;
	private SecurityLevelDTO availability;
	private String availabilityExplanation;
	private SecurityLevelDTO integrity;
	private String integrityExplanation;
	private SecurityLevelDTO confidentiality;
	private String confidentialityExplanation;
	private boolean personalData;

	private List<InformationDTO> informations;
	private Boolean slim = false;

	public OccupationDTO() {

	}

	public OccupationDTO(int id, Integer domain_id, String name, String ident,
	        SecurityLevelDTO availability, String availabilityExplanation,
	        SecurityLevelDTO confidentiality, String confidentialityExplanation,
	        SecurityLevelDTO integrity, String integrityExplanation, boolean privateData,
	        boolean manuelSecurityAssesment, List<InformationDTO> informationList) {
		this.id = id;
		this.domain_id = domain_id;
		this.name = name;
		this.ident = ident;
		this.manuelSecurityAssesment = manuelSecurityAssesment;
		this.integrity = integrity;
		this.integrityExplanation = integrityExplanation;
		this.availability = availability;
		this.availabilityExplanation = availabilityExplanation;
		this.confidentiality = confidentiality;
		this.confidentialityExplanation = confidentialityExplanation;
		informations = informationList;
		this.personalData = privateData;
	}

	public OccupationDTO(Integer domain_id) {
		this.domain_id = domain_id;
		integrity = SecurityLevelDTO.getDefaultSecurityLevel();
		availability = SecurityLevelDTO.getDefaultSecurityLevel();
		confidentiality = SecurityLevelDTO.getDefaultSecurityLevel();
		informations = new ArrayList<InformationDTO>();
	}

	public OccupationDTO(Integer id, String name) {
		this.id = id;
		this.name = name;
		informations = new ArrayList<InformationDTO>();
		slim = true;
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

	public String getCalculatedAvailabilityName() {
		if(getCalculatedAvailability() != null){
			return getCalculatedAvailability().getName();
		} else {
			return SecurityLevelDTO.getDefaultSecurityLevel().getName();
		}		
	}

	public String getCalculatedConfidentialityName() {
		if(getCalculatedConfidentiality() != null){
			return getCalculatedConfidentiality().getName();
		} else {
			return SecurityLevelDTO.getDefaultSecurityLevel().getName();
		}
	}

	public String getCalculatedIntegrityName() {
		if(getCalculatedIntegrity() != null){
			return getCalculatedIntegrity().getName();
		} else {
			return SecurityLevelDTO.getDefaultSecurityLevel().getName();
		}
	}

	public SecurityLevelDTO getCalculatedAvailability() {
		SecurityLevelDTO result = getAvailability();
		for (InformationDTO information : getInformations()) {
			if (information.getAvailability() != null
			        && information.getAvailability().getId() > result.getId()) {
				result = information.getAvailability();
			}
		}

		return result;
	}

	public SecurityLevelDTO getConfidentiality() {
		return confidentiality;
	}

	public SecurityLevelDTO getCalculatedConfidentiality() {
		SecurityLevelDTO result = getConfidentiality();
		for (InformationDTO information : getInformations()) {
			if (information.getCalculatedConfidentiality() != null
			        && information.getCalculatedConfidentiality().getId() > result.getId()) {
				result = information.getCalculatedConfidentiality();
			}
		}
		if(manuelSecurityAssesment && personalData){
			result = SecurityLevelDTO.getSecurityLevel(3);
		}

		return result;
	}

	public void setConfidentiality(SecurityLevelDTO confidentiality) {
		this.confidentiality = confidentiality;
	}

	public SecurityLevelDTO getIntegrity() {
		return integrity;
	}

	public SecurityLevelDTO getCalculatedIntegrity() {
		SecurityLevelDTO result = getIntegrity();
		for (InformationDTO information : getInformations()) {
			if (information.getIntegrity() != null
			        && information.getIntegrity().getId() > result.getId()) {
				result = information.getIntegrity();
			}
		}

		return result;
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getDomainId() {
		return domain_id;
	}

	public void setDomainId(Integer domain_id) {
		this.domain_id = domain_id;
	}

	public boolean isPersonalData() {
		return personalData;
	}

	public void setPersonalData(boolean privateData) {
		this.personalData = privateData;
	}

	public List<InformationDTO> getInformations() {
		return informations;
	}

	public void setInformations(List<InformationDTO> informations) {
		this.informations = informations;
	}

	public boolean isManuelSecurityAssesment() {
		return manuelSecurityAssesment;
	}

	public void setManuelSecurityAssesment(boolean manuelSecurityAssesment) {
		this.manuelSecurityAssesment = manuelSecurityAssesment;
	}

	public SecurityzoneDTO getAssignedSecurityzone() {
		SecurityzoneDTO result = null;
		for (InformationDTO information : getInformations()) {
			SecurityzoneDTO informationSecurityzone = information.getSecurityzone();
			if (result == null) {
				result = informationSecurityzone;
			}
			if (informationSecurityzone != null && !informationSecurityzone.isSlim() && result != null
			        && result.getOrderNumber() < informationSecurityzone.getOrderNumber()) {
				result = informationSecurityzone;
			}
		}
		return result;
	}

	public String getAssignedSecurityzoneName() {
		if(getAssignedSecurityzone() != null){
			return getAssignedSecurityzone().getName();
		} else {
			return "";
		}		
	}
	
	@Override
	public String getLabel() {
		if (ident == null || ident.isEmpty()) {
			return name;
		} else {
			return name + " (" + ident + ")";
		}
	}

	public Boolean isSlim() {
		return slim;
	}

	@Override
	public String getClassName() {
		return "Dienstposten";
	}

	public OccupationDTO clone() {
		OccupationDTO result = new OccupationDTO(domain_id);
		if (this.id != null) {
			result.setId(getId());
			result.setDomainId(getDomainId());
			result.setName(getName());
			result.setIdent(getIdent());
			result.setManuelSecurityAssesment(isManuelSecurityAssesment());
			result.setAvailability(getAvailability());
			result.setAvailabilityExplanation(getAvailabilityExplanation());
			result.setConfidentiality(getConfidentiality());
			result.setConfidentialityExplanation(getConfidentialityExplanation());
			result.setIntegrity(getIntegrity());
			result.setIntegrityExplanation(getIntegrityExplanation());
			List<InformationDTO> clonedInformation = new ArrayList<InformationDTO>();
			for (InformationDTO i : informations) {
				clonedInformation.add(i);
			}
			result.setInformations(clonedInformation);
		}
		return result;
	}

	@Override
	public OccupationDTO update(OccupationDTO toUpdate) {
		this.domain_id = toUpdate.domain_id;
		this.name = toUpdate.name;
		this.ident = toUpdate.ident;
		this.manuelSecurityAssesment = toUpdate.manuelSecurityAssesment;
		this.integrity = toUpdate.integrity;
		this.integrityExplanation = toUpdate.integrityExplanation;
		this.availability = toUpdate.availability;
		this.availabilityExplanation = toUpdate.availabilityExplanation;
		this.confidentiality = toUpdate.confidentiality;
		this.confidentialityExplanation = toUpdate.confidentialityExplanation;
		informations = toUpdate.informations;

		this.slim = toUpdate.slim;
		return this;
	}

}
