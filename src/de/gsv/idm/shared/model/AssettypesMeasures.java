package de.gsv.idm.shared.model;

import java.util.ArrayList;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Many2Many;

import de.gsv.idm.shared.dto.AssettypeMeasureLinkDTO;
import de.gsv.idm.shared.dto.EmployeeDTO;
import de.gsv.idm.shared.dto.ModuleDTO;
import de.gsv.idm.shared.dto.SecurityzoneDTO;

@Many2Many(other = Module.class, join = "assettypes_measures_modules", sourceFKName = "assettypes_measures_id", targetFKName = "module_id")
public class AssettypesMeasures extends Model {

	public AssettypeMeasureLinkDTO createDTO() {
		Measure measure = Measure.findById(getInteger("measure_id"));
		if (measure != null) {
			EmployeeDTO moduleResponsiplePersonDTO = null;
			Employee moduleResponsiplePersonDB = Employee
			        .findById(getInteger("responsible_person_id"));
			if (moduleResponsiplePersonDB != null) {
				moduleResponsiplePersonDTO = moduleResponsiplePersonDB.createSlimDTO();
			}

			ArrayList<Module> currentLinkedModules = new ArrayList<Module>(getAll(Module.class));
			ArrayList<ModuleDTO> linkedModules = new ArrayList<ModuleDTO>();
			for (Module moduleDB : currentLinkedModules) {
				linkedModules.add(moduleDB.createSlimDTO());
			}

			ArrayList<Securityzone> currentLinkedSecurityzones = new ArrayList<Securityzone>(
			        getAll(Securityzone.class));
			ArrayList<SecurityzoneDTO> linkedSecurityzones = new ArrayList<SecurityzoneDTO>();
			for (Securityzone securityzoneDB : currentLinkedSecurityzones) {
				linkedSecurityzones.add(securityzoneDB.createSlimDTO());
			}

			return new AssettypeMeasureLinkDTO(measure.createSlimDTO(), (Integer) getId(),
			        moduleResponsiplePersonDTO, getString("status"), getString("cost"),
			        getBoolean("pass_on"), getBoolean("manual_add"), linkedModules,
			        linkedSecurityzones);
		} else {
			return null;
		}
	}

}
