package de.gsv.idm.shared.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class EmployeeDTO extends GeneralDTO<EmployeeDTO> implements Serializable {

	private Integer id;
	private Integer domainId;
	private String name;
	private String surname;
	private String telefon;

	private List<InformationDTO> informations;
	private List<OccupationDTO> occupations;
	private List<AssetDTO> assetmodels;

	private Boolean slim = false;

	public EmployeeDTO() {

	}

	public EmployeeDTO(int id, String name, String surname) {
		this.id = id;
		this.name = name;
		this.surname = surname;
		informations = new ArrayList<InformationDTO>();
		occupations = new ArrayList<OccupationDTO>();
		assetmodels = new ArrayList<AssetDTO>();
		slim = true;
	}

	public EmployeeDTO(int id, Integer domain_id, String name, String surname, String telefon,
	        List<InformationDTO> informationList, List<OccupationDTO> occupationList,
	        List<AssetDTO> assetmodelList) {
		this.id = id;
		this.domainId = domain_id;
		this.name = name;
		this.surname = surname;
		this.telefon = telefon;
		this.informations = informationList;
		this.occupations = occupationList;
		assetmodels = assetmodelList;
	}

	public EmployeeDTO(Integer domain_id) {
		this.domainId = domain_id;
		informations = new ArrayList<InformationDTO>();
		occupations = new ArrayList<OccupationDTO>();
		assetmodels = new ArrayList<AssetDTO>();
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getTelefon() {
		return telefon;
	}

	public void setTelefon(String telefon) {
		this.telefon = telefon;
	}

	public List<InformationDTO> getInformations() {
		return informations;
	}

	public void setInformations(List<InformationDTO> informations) {
		this.informations = informations;
	}

	public List<OccupationDTO> getOccupations() {
		return occupations;
	}

	public void setOccupations(List<OccupationDTO> occupations) {
		this.occupations = occupations;
	}

	public List<AssetDTO> getAssetmodels() {
		return assetmodels;
	}

	public void setAssetmodels(List<AssetDTO> assetmodels) {
		this.assetmodels = assetmodels;
	}

	public SecurityLevelDTO getCalculatedAvailability() {
		SecurityLevelDTO result = SecurityLevelDTO.getSecurityLevel(1);
		for (InformationDTO information : getInformations()) {
			if (information.getAvailability() != null
			        && information.getAvailability().getId() > result.getId()) {
				result = information.getAvailability();
			}
		}
		for (OccupationDTO occupation : getOccupations()) {
			if (occupation.getCalculatedAvailability() != null
			        && occupation.getCalculatedAvailability().getId() > result.getId()) {
				result = occupation.getCalculatedAvailability();
			}
		}

		return result;
	}

	public String getCalculatedAvailabilityName() {
		return getCalculatedAvailability().getName();
	}

	public SecurityLevelDTO getCalculatedConfidentiality() {
		SecurityLevelDTO result = SecurityLevelDTO.getSecurityLevel(1);
		for (InformationDTO information : getInformations()) {
			if (information.getCalculatedConfidentiality() != null
			        && information.getCalculatedConfidentiality().getId() > result.getId()) {
				result = information.getCalculatedConfidentiality();
			}
		}
		for (OccupationDTO occupation : getOccupations()) {
			if (occupation.getCalculatedConfidentiality() != null
			        && occupation.getCalculatedConfidentiality().getId() > result.getId()) {
				result = occupation.getCalculatedConfidentiality();
			}
		}

		return result;
	}

	public String getCalculatedConfidentialityName() {
		return getCalculatedConfidentiality().getName();
	}

	public SecurityLevelDTO getCalculatedIntegrity() {
		SecurityLevelDTO result = SecurityLevelDTO.getSecurityLevel(1);
		for (InformationDTO information : getInformations()) {
			if (information.getIntegrity() != null
			        && information.getIntegrity().getId() > result.getId()) {
				result = information.getIntegrity();
			}
		}
		for (OccupationDTO occupation : getOccupations()) {
			if (occupation.getCalculatedIntegrity() != null
			        && occupation.getCalculatedIntegrity().getId() > result.getId()) {
				result = occupation.getCalculatedIntegrity();
			}
		}

		return result;
	}

	public String getCalculatedIntegrityName() {
		return getCalculatedIntegrity().getName();
	}

	public SecurityLevelDTO getCalculatedSecurityAssesment() {
		SecurityLevelDTO result = SecurityLevelDTO.getDefaultSecurityLevel();
		if (getCalculatedAvailability() != null
		        && getCalculatedAvailability().getId() > result.getId()) {
			result = getCalculatedAvailability();
		}
		if (getCalculatedConfidentiality() != null
		        && getCalculatedConfidentiality().getId() > result.getId()) {
			result = getCalculatedConfidentiality();
		}
		if (getCalculatedIntegrity() != null && getCalculatedIntegrity().getId() > result.getId()) {
			result = getCalculatedIntegrity();
		}
		return result;
	}

	public String getCalculatedSecurityAssesmentName() {
		return getCalculatedSecurityAssesment().getName();
	}

	public String getAssignedSecurityzoneName() {
		if (getAssignedSecurityzone() != null) {
			return getAssignedSecurityzone().getName();
		} else {
			return "";
		}
	}

	public SecurityzoneDTO getAssignedSecurityzone() {
		SecurityzoneDTO result = null;
		for (OccupationDTO occupation : getOccupations()) {
			SecurityzoneDTO occupationSecurityzone = occupation.getAssignedSecurityzone();
			if (result == null) {
				result = occupationSecurityzone;
			}
			if (occupationSecurityzone != null
			        && result.getOrderNumber() < occupationSecurityzone.getOrderNumber()) {
				result = occupationSecurityzone;
			}
		}

		for (InformationDTO information : getInformations()) {
			SecurityzoneDTO informationSecurityzone = information.getSecurityzone();
			if (result == null) {
				result = informationSecurityzone;
			}
			if (informationSecurityzone != null
			        && result.getOrderNumber() < informationSecurityzone.getOrderNumber()) {
				result = informationSecurityzone;
			}
		}

		return result;
	}

	@Override
	public String getClassName() {
		return "Mitarbeiter";
	}

	public EmployeeDTO clone() {
		EmployeeDTO result = new EmployeeDTO(domainId);
		if (id != null) {
			result.id = this.id;
			result.name = this.name;
			result.surname = this.surname;
			result.telefon = this.telefon;

			List<InformationDTO> clonedInformation = new ArrayList<InformationDTO>();
			for (InformationDTO i : informations) {
				clonedInformation.add(i);
			}
			result.setInformations(clonedInformation);

			List<OccupationDTO> clonedOccupations = new ArrayList<OccupationDTO>();
			for (OccupationDTO i : occupations) {
				clonedOccupations.add(i);
			}
			result.setOccupations(clonedOccupations);

			List<AssetDTO> clonedAssetmodels = new ArrayList<AssetDTO>();
			for (AssetDTO assetmodel : assetmodels) {
				clonedAssetmodels.add(assetmodel);
			}
			result.setAssetmodels(clonedAssetmodels);
		}
		return result;
	}

	public String getLabel() {
		String result = "";
		if (surname != null) {
			result += surname;
		}
		if (name != null) {
			if (!result.isEmpty()) {
				result += ", ";
			}
			result += name;
		}
		if (result.isEmpty()) {
			result += "Keine Name";
		}
		return result;
	}

	@Override
	public String toString() {
		return getLabel();
	}

	@Override
	public Boolean isSlim() {
		return slim;
	}

	@Override
	public EmployeeDTO update(EmployeeDTO toUpdate) {
		domainId = toUpdate.domainId;
		name = toUpdate.name;
		surname = toUpdate.surname;
		telefon = toUpdate.telefon;
		informations = toUpdate.informations;
		occupations = toUpdate.occupations;
		assetmodels = toUpdate.assetmodels;

		this.slim = toUpdate.slim;
		return this;
	}

}
