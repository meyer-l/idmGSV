package de.gsv.idm.client.presenter.securitylevelchange.data;

import de.gsv.idm.shared.dto.SecurityLevelDTO;

public class SecurityLevelBundle {

	private SecurityLevelDTO oldSecurityAssesment;
	private SecurityLevelDTO oldAvailability;
	private SecurityLevelDTO oldConfidentiality;
	private SecurityLevelDTO oldIntegrity;

	public SecurityLevelBundle(SecurityLevelDTO oldSecurityAssesment,
	        SecurityLevelDTO oldAvailability, SecurityLevelDTO oldConfidentiality,
	        SecurityLevelDTO oldIntegrity) {
		this.oldSecurityAssesment = oldSecurityAssesment;
		this.oldAvailability = oldAvailability;
		this.oldConfidentiality = oldConfidentiality;
		this.oldIntegrity = oldIntegrity;
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
