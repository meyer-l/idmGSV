package de.gsv.idm.shared.model;

import java.util.ArrayList;
import java.util.List;

import org.javalite.activejdbc.Model;

import de.gsv.idm.server.push.PushUpdateHandler;
import de.gsv.idm.shared.dto.GeneralDTO;
import de.gsv.idm.shared.dto.MeasureDTO;
import de.gsv.idm.shared.dto.MeasureLinkDTO;
import de.gsv.idm.shared.dto.ModuleDTO;
import de.gsv.idm.shared.dto.ThreatDTO;

public class Module extends Model implements HasDTOMethods<ModuleDTO> {

	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (other instanceof Module) {
			return getId().equals(((Module) other).getId());			        
		}

		return false;
	}
	
	public ModuleDTO createAssocDTO(){
		return new ModuleDTO(getInteger("id"));
	}

	public ModuleDTO createSlimDTO() {

		List<ModuleMeasureParentChild> measureAssocList = ModuleMeasureParentChild.where(
		        "parent_id = ? AND additional = 0", getId());
		List<MeasureLinkDTO> measureDTOList = new ArrayList<MeasureLinkDTO>();
		for (ModuleMeasureParentChild link : measureAssocList) {
			Measure measure = Measure.findById(link.get("child_id"));
			if (measure != null)
				measureDTOList.add(new MeasureLinkDTO(measure.createSlimDTO(), link
				        .getString("category")));
		}
		return new ModuleDTO(getInteger("id"), getString("title"), getString("category"), measureDTOList);
	}

	public ModuleDTO createFullDTO() {

		List<ModuleModuleParentChild> moduleAssocList = ModuleModuleParentChild.where(
		        "parent_id = ?", getId());
		List<ModuleDTO> moduleDTOAddList = new ArrayList<ModuleDTO>();
		for (ModuleModuleParentChild link : moduleAssocList) {
			Module module = Module.findById(link.get("child_id"));
			if (module != null)
				moduleDTOAddList.add(module.createAssocDTO());
		}

		List<ModuleThreatParentChild> threatAssocList = ModuleThreatParentChild.where(
		        "parent_id = ? AND additional = 0", getId());
		List<ThreatDTO> threatDTOList = new ArrayList<ThreatDTO>();
		for (ModuleThreatParentChild link : threatAssocList) {
			Threat threat = Threat.findById(link.get("child_id"));
			if (threat != null)
				threatDTOList.add(threat.createSlimDTO());
		}
		List<ModuleThreatParentChild> threatAddAssocList = ModuleThreatParentChild.where(
		        "parent_id = ? AND additional = 1", getId());
		List<ThreatDTO> threatDTOAddList = new ArrayList<ThreatDTO>();
		for (ModuleThreatParentChild link : threatAddAssocList) {
			Threat threat = Threat.findById(link.get("child_id"));
			if (threat != null)
				threatDTOAddList.add(threat.createSlimDTO());
		}

		List<ModuleMeasureParentChild> measureAssocList = ModuleMeasureParentChild.where(
		        "parent_id = ? AND additional = 0", getId());
		List<MeasureLinkDTO> measureDTOList = new ArrayList<MeasureLinkDTO>();
		for (ModuleMeasureParentChild link : measureAssocList) {
			Measure measure = Measure.findById(link.get("child_id"));
			if (measure != null)
				measureDTOList.add(new MeasureLinkDTO(measure.createSlimDTO(), link
				        .getString("category")));
		}
		List<ModuleMeasureParentChild> measureAddAssocList = ModuleMeasureParentChild.where(
		        "parent_id = ? AND additional = 1", getId());
		List<MeasureDTO> measureDTOAddList = new ArrayList<MeasureDTO>();
		for (ModuleMeasureParentChild link : measureAddAssocList) {
			Measure threat = Measure.findById(link.get("child_id"));
			if (threat != null)
				measureDTOAddList.add(threat.createSlimDTO());
		}
		return new ModuleDTO(getInteger("id"), getString("title"), getString("category"), getString("version"),
		        getString("module_description"), getString("threat_description"),
		        getString("measure_description"), threatDTOList, measureDTOList, moduleDTOAddList,
		        threatDTOAddList, measureDTOAddList);
	}

	@Override
    public ModuleDTO updateFromDTO(ModuleDTO updateObject,PushUpdateHandler server) {
		set("name",updateObject.getName());
		
		saveIt();
		return createFullDTO();
    }

	@Override
    public void completeDelete(PushUpdateHandler server) {
	    deleteCascadeShallow();	    
    }

	@Override
    public List<GeneralDTO<?>> getAffectedObjects() {
		List<GeneralDTO<?>> result = new ArrayList<GeneralDTO<?>>();
		result.add(createFullDTO());
		
		return result;
    }
}
