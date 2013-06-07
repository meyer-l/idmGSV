package de.gsv.idm.shared.model;

import java.util.ArrayList;
import java.util.List;

import org.javalite.activejdbc.Model;

import de.gsv.idm.shared.dto.AssetDTO;
import de.gsv.idm.shared.dto.ChangeItemDTO;
import de.gsv.idm.shared.dto.EmployeeDTO;
import de.gsv.idm.shared.dto.OccupationDTO;

public class ChangeItem extends Model {
	public ChangeItemDTO createDTO() {
		EmployeeDTO employeeDTO = null;
		Integer employeeId = getInteger("employee_id");
		if (employeeId != null) {
			Employee employeeDB = Employee.findById(employeeId);
			if (employeeDB != null) {
				employeeDTO = employeeDB.createSlimDTO();
			}
		}

		List<AssetDTO> oldAssets = new ArrayList<AssetDTO>();
		for (String assetId : getString("old_assets_ids").split(";")) {
			Asset assetDB = Asset.findById(assetId);
			if (assetDB != null) {
				oldAssets.add(assetDB.createSlimDTO());
			}
		}
		
		List<String> missingAssetsNames = new ArrayList<String>();
		List<AssetDTO> newAssets = new ArrayList<AssetDTO>();
		for (String assetId : getString("new_assets_ids").split(";")) {
			if(!assetId.equals("")){
				Asset assetDB = Asset.findById(assetId);
				if (assetDB != null) {
					newAssets.add(assetDB.createSlimDTO());
				} else {
					deleteCascadeShallow();
					return null;
				}
			}
			
		}

		
		for (String assetName : getString("missing_assets_names").split(";")) {
			if (!assetName.equals("")) {
				missingAssetsNames.add(assetName);
			}
		}

		List<OccupationDTO> oldOccupations = new ArrayList<OccupationDTO>();
		for (String occupationId : getString("old_occupations_ids").split(";")) {
			Occupation occupationDB = Occupation.findById(occupationId);
			if (occupationDB != null) {
				oldOccupations.add(occupationDB.createSlimDTO());
			}
		}

		List<OccupationDTO> newOccupations = new ArrayList<OccupationDTO>();
		for (String occupationId : getString("new_occupations_ids").split(";")) {
			Occupation occupationDB = Occupation.findById(occupationId);
			if(!occupationId.equals("")){
				if (occupationDB != null) {
					newOccupations.add(occupationDB.createSlimDTO());
				}  else {
					deleteCascadeShallow();
					return null;
				}
			}
			
		}

		List<String> missingOccupationsNames = new ArrayList<String>();
		for (String occupationName : getString("missing_occupations_names").split(";")) {
			if (!occupationName.equals("")) {
				missingOccupationsNames.add(occupationName);
			}

		}

		return new ChangeItemDTO(getInteger("id"), getInteger("domain_id"),
		        getTimestamp("created_at"), employeeDTO, getString("parsed_employee_name"),
		        oldAssets, newAssets, missingAssetsNames, oldOccupations, newOccupations,
		        missingOccupationsNames, getString("old_telefon"), getString("new_telefon"));
	}
}
