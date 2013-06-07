package de.gsv.idm.shared.model;

import java.util.ArrayList;
import java.util.List;

import org.javalite.activejdbc.Model;

import de.gsv.idm.server.push.PushUpdateHandler;
import de.gsv.idm.shared.dto.AssetDTO;
import de.gsv.idm.shared.dto.GeneralDTO;
import de.gsv.idm.shared.dto.SecurityLevelChangeDTO;
import de.gsv.idm.shared.dto.SecurityLevelDTO;

public class SecurityLevelChange extends Model implements HasDTOMethods<SecurityLevelChangeDTO> {

	// Has a reviewed db-field, but that is not used

	@Override
	public SecurityLevelChangeDTO createFullDTO() {
		Asset assetDB = Asset.findById(getInteger("asset_id"));
		AssetDTO assetDTO = null;
		if (assetDB != null) {
			assetDTO = assetDB.createSlimDTO();
		} else {
			delete();
			return null;
		}

		SecurityLevelDTO oldSecurityAssesment = SecurityLevelDTO
		        .getSecurityLevel(getInteger("old_security_assesment"));
		SecurityLevelDTO oldAvailability = SecurityLevelDTO
		        .getSecurityLevel(getInteger("old_availability"));
		SecurityLevelDTO oldoConfidentiality = SecurityLevelDTO
		        .getSecurityLevel(getInteger("old_confidentiality"));
		SecurityLevelDTO oldIntegrity = SecurityLevelDTO
		        .getSecurityLevel(getInteger("old_integrity"));

		return new SecurityLevelChangeDTO(getInteger("id"), getInteger("domain_id"), assetDTO,
		        oldSecurityAssesment, oldAvailability, oldoConfidentiality, oldIntegrity);
	}

	@Override
	public SecurityLevelChangeDTO createSlimDTO() {
		return createFullDTO();
	}

	@Override
	public SecurityLevelChangeDTO updateFromDTO(SecurityLevelChangeDTO toUpdate,
	        PushUpdateHandler serverImpl) {
		if (toUpdate.getAsset() != null) {
			set("asset_id", toUpdate.getAsset().getId());
		}
		set("domain_id", toUpdate.getDomainId());
		
		if (toUpdate.getOldSecurityAssesment() != null) {
			set("old_security_assesment", toUpdate.getOldSecurityAssesment().getId());
		} else {
			set("old_security_assesment", null);
		}
		if (toUpdate.getOldAvailability()!= null) {
			set("old_availability", toUpdate.getOldAvailability().getId());
		} else {
			set("old_availability", null);
		}
		if (toUpdate.getOldConfidentiality() != null) {
			set("old_confidentiality", toUpdate.getOldConfidentiality().getId());
		} else {
			set("old_confidentiality", null);
		}
		if (toUpdate.getOldIntegrity() != null) {
			set("old_integrity", toUpdate.getOldIntegrity().getId());
		} else {
			set("old_integrity", null);
		}
		saveIt();

		return createFullDTO();
	}

	@Override
	public void completeDelete(PushUpdateHandler pushUpdate) {
		deleteCascadeShallow();
	}

	@Override
	public List<GeneralDTO<?>> getAffectedObjects() {
		List<GeneralDTO<?>> result = new ArrayList<GeneralDTO<?>>();
		result.add(createFullDTO());

		return result;
	}

}
