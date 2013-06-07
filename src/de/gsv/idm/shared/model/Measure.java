package de.gsv.idm.shared.model;

import java.util.ArrayList;
import java.util.List;

import org.javalite.activejdbc.Model;

import de.gsv.idm.server.push.PushUpdateHandler;
import de.gsv.idm.shared.dto.GeneralDTO;
import de.gsv.idm.shared.dto.MeasureDTO;
import de.gsv.idm.shared.dto.ModuleDTO;
import de.gsv.idm.shared.dto.ThreatDTO;

public class Measure extends Model implements HasDTOMethods<MeasureDTO> {

	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (other instanceof Measure) {
			return getId().equals(((Measure) other).getId());
		}

		return false;
	}

	public MeasureDTO createSlimDTO() {
		return new MeasureDTO(getInteger("id"), getString("title"), getString("category"));
	}

	public MeasureDTO createFullDTO() {
		List<MeasureModuleParentChild> moduleAddAssocList = MeasureModuleParentChild.where(
		        "parent_id = ?", getId());
		List<ModuleDTO> moduleDTOAddList = new ArrayList<ModuleDTO>();
		for (MeasureModuleParentChild link : moduleAddAssocList) {
			Module module = Module.findById(link.get("child_id"));
			if (module != null)
				moduleDTOAddList.add(module.createSlimDTO());
		}

		List<MeasureThreatParentChild> threatAddAssocList = MeasureThreatParentChild.where(
		        "parent_id = ?", getId());
		List<ThreatDTO> threatDTOAddList = new ArrayList<ThreatDTO>();
		for (MeasureThreatParentChild link : threatAddAssocList) {
			Threat threat = Threat.findById(link.get("child_id"));
			if (threat != null)
				threatDTOAddList.add(threat.createSlimDTO());
		}

		List<MeasureMeasureParentChild> measureAddAssocList = MeasureMeasureParentChild.where(
		        "parent_id = ?", getId());
		List<MeasureDTO> measureDTOAddList = new ArrayList<MeasureDTO>();
		for (MeasureMeasureParentChild link : measureAddAssocList) {
			Measure threat = Measure.findById(link.get("child_id"));
			if (threat != null)
				measureDTOAddList.add(threat.createSlimDTO());
		}

		return new MeasureDTO(getInteger("id"), getString("title"), getString("category"), getString("version"),
		        getString("initiator"), getString("implementation"), getString("description"),
		        moduleDTOAddList, threatDTOAddList, measureDTOAddList);
	}

	@Override
	public MeasureDTO updateFromDTO(MeasureDTO updateObject, PushUpdateHandler server) {
		set("name", updateObject.getName());
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
