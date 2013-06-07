package de.gsv.idm.shared.model;

import org.javalite.activejdbc.Model;

import de.gsv.idm.shared.dto.AssetModuleLinkDTO;
import de.gsv.idm.shared.dto.EmployeeDTO;

public class AssetsAssettypesModules extends Model {
	public AssetModuleLinkDTO createDTO(){
		AssettypesModules assetsModules = AssettypesModules.findById(getInteger("assettypes_modules_id"));
		if (assetsModules != null) {
			EmployeeDTO moduleResponsiplePersonDTO = null;
			Employee moduleResponsiplePersonDB = Employee.findById(
			        getInteger("responsible_person_id"));
			if (moduleResponsiplePersonDB != null) {
				moduleResponsiplePersonDTO = moduleResponsiplePersonDB.createSlimDTO();
			}

			return new AssetModuleLinkDTO(assetsModules.createDTO(), (Integer) 
			        getId(), moduleResponsiplePersonDTO);
		} else {
			return null;
		}
	}
}
