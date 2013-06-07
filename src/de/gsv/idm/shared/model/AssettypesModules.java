package de.gsv.idm.shared.model;

import org.javalite.activejdbc.Model;

import de.gsv.idm.shared.dto.AssettypeModuleLinkDTO;
import de.gsv.idm.shared.dto.EmployeeDTO;

public class AssettypesModules extends Model {
	
	public AssettypeModuleLinkDTO createDTO(){
		Module module = Module.findById(getInteger("module_id"));
		if (module != null) {
			EmployeeDTO moduleResponsiplePersonDTO = null;
			Employee moduleResponsiplePersonDB = Employee.findById(
			        getInteger("responsible_person_id"));
			if (moduleResponsiplePersonDB != null) {
				moduleResponsiplePersonDTO = moduleResponsiplePersonDB.createSlimDTO();
			}

			return new AssettypeModuleLinkDTO(module.createSlimDTO(), (Integer) 
			        getId(), moduleResponsiplePersonDTO);
		} else {
			return null;
		}
	}
}
