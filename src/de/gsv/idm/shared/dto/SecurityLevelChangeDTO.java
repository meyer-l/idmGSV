package de.gsv.idm.shared.dto;

import java.io.Serializable;

import de.gsv.idm.client.presenter.securitylevelchange.data.SecurityLevelBundle;

@SuppressWarnings("serial")
public class SecurityLevelChangeDTO extends GeneralDTO<SecurityLevelChangeDTO> implements
        Serializable {

	private Integer id;
	private Integer domainId;
	private AssetDTO asset;
	private Boolean reviewed;
	private SecurityLevelDTO oldSecurityAssesment;
	private SecurityLevelDTO oldAvailability;
	private SecurityLevelDTO oldConfidentiality;
	private SecurityLevelDTO oldIntegrity;

	public SecurityLevelChangeDTO() {

	}

	public SecurityLevelChangeDTO(Integer id, Integer domainId, AssetDTO asset,
	        SecurityLevelDTO oldSecurityAssesment, SecurityLevelDTO oldAvailability,
	        SecurityLevelDTO oldConfidentiality, SecurityLevelDTO oldIntegrity) {
		this.id = id;
		this.domainId = domainId;
		this.asset = asset;
		reviewed = false;
		this.oldSecurityAssesment = oldSecurityAssesment;
		this.oldAvailability = oldAvailability;
		this.oldConfidentiality = oldConfidentiality;
		this.oldIntegrity = oldIntegrity;

	}

	public SecurityLevelChangeDTO(AssetDTO asset, SecurityLevelBundle oldSecurityLevels) {
		this.asset = asset;
		this.domainId = asset.getDomainId();
		reviewed = false;
		if (oldSecurityLevels != null) {
			this.oldSecurityAssesment = oldSecurityLevels.getOldSecurityAssesment();
			this.oldAvailability = oldSecurityLevels.getOldAvailability();
			this.oldConfidentiality = oldSecurityLevels.getOldConfidentiality();
			this.oldIntegrity = oldSecurityLevels.getOldIntegrity();
		}
	}

	@Override
	public String getName() {
		return asset.getName();
	}

	@Override
	public Integer getId() {
		return id;
	}

	public AssetDTO getAsset() {
		return asset;
	}

	public void setAsset(AssetDTO asset) {
		this.asset = asset;
	}

	public Boolean getReviewed() {
		return reviewed;
	}

	public void setReviewed(Boolean reviewed) {
		this.reviewed = reviewed;
	}

	public SecurityLevelChangeDTO getInstance() {
		return this;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public SecurityLevelDTO getSecurityAssesment() {
		return asset.getCalculatedSecurityAssesment();
	}

	public String getSecurityAssesmentName() {
		String result = asset.getCalculatedSecurityAssesment().getName();
		if (oldSecurityAssesment != null) {
			result += " (Vorher: " + oldSecurityAssesment + ")";
		}
		return result;
	}

	public SecurityLevelDTO getAvailability() {
		return asset.getCalculatedAvailability();
	}

	public String getAvailabilityName() {
		String result = asset.getCalculatedAvailability().getName();
		if (oldAvailability != null) {
			result += " (Vorher: " + oldAvailability + ")";
		}
		return result;
	}

	public SecurityLevelDTO getConfidentiality() {
		return asset.getCalculatedConfidentiality();
	}

	public String getConfidentialityName() {
		String result = asset.getCalculatedConfidentiality().getName();
		if (oldConfidentiality != null) {
			result += " (Vorher: " + oldConfidentiality + ")";
		}
		return result;
	}

	public SecurityLevelDTO getIntegrity() {
		return asset.getCalculatedIntegrity();
	}

	public String getIntegrityName() {
		String result = asset.getCalculatedIntegrity().getName();
		if (oldIntegrity != null) {
			result += " (Vorher: " + oldIntegrity + ")";
		}
		return result;
	}

	@Override
	public String getClassName() {
		return "Schutzbedarf-Änderung";
	}

	@Override
	public Boolean isSlim() {
		return false;
	}

	@Override
	public SecurityLevelChangeDTO update(SecurityLevelChangeDTO toUpdate) {
		asset = toUpdate.asset;
		domainId = toUpdate.domainId;
		reviewed = toUpdate.reviewed;

		oldSecurityAssesment = toUpdate.oldSecurityAssesment;
		oldAvailability = toUpdate.oldAvailability;
		oldConfidentiality = toUpdate.oldConfidentiality;
		oldIntegrity = toUpdate.oldIntegrity;
		return this;
	}

	public Integer getDomainId() {
		return domainId;
	}

	public SecurityLevelDTO getOldSecurityAssesment() {
		return oldSecurityAssesment;
	}

	public void setOldSecurityAssesment(SecurityLevelDTO oldSecurityAssesment) {
		this.oldSecurityAssesment = oldSecurityAssesment;
	}

	public SecurityLevelDTO getOldAvailability() {
		return oldAvailability;
	}

	public void setOldAvailability(SecurityLevelDTO oldAvailability) {
		this.oldAvailability = oldAvailability;
	}

	public SecurityLevelDTO getOldConfidentiality() {
		return oldConfidentiality;
	}

	public void setOldConfidentiality(SecurityLevelDTO oldConfidentiality) {
		this.oldConfidentiality = oldConfidentiality;
	}

	public SecurityLevelDTO getOldIntegrity() {
		return oldIntegrity;
	}

	public void setOldIntegrity(SecurityLevelDTO oldIntegrity) {
		this.oldIntegrity = oldIntegrity;
	}

}
