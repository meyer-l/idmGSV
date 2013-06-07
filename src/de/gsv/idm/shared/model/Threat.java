package de.gsv.idm.shared.model;

import java.util.ArrayList;
import java.util.List;

import org.javalite.activejdbc.Model;

import de.gsv.idm.server.push.PushUpdateHandler;
import de.gsv.idm.shared.dto.GeneralDTO;
import de.gsv.idm.shared.dto.MeasureDTO;
import de.gsv.idm.shared.dto.ModuleDTO;
import de.gsv.idm.shared.dto.ThreatDTO;

public class Threat extends Model implements HasDTOMethods<ThreatDTO> {
	public ThreatDTO createSlimDTO() {
		return new ThreatDTO(getInteger("id"), getString("title"), getString("category"));
	}

	public ThreatDTO createFullDTO() {
		List<ThreatModuleParentChild> moduleAddAssocList = ThreatModuleParentChild.where(
		        "parent_id = ?", getId());
		List<ModuleDTO> moduleDTOAddList = new ArrayList<ModuleDTO>();
		for (ThreatModuleParentChild link : moduleAddAssocList) {
			Module module = Module.findById(link.get("child_id"));
			if (module != null)
				moduleDTOAddList.add(module.createSlimDTO());
		}
		List<ThreatThreatParentChild> threatAddAssocList = ThreatThreatParentChild.where(
		        "parent_id = ?", getId());
		List<ThreatDTO> threatDTOAddList = new ArrayList<ThreatDTO>();
		for (ThreatThreatParentChild link : threatAddAssocList) {
			Threat threat = Threat.findById(link.get("child_id"));
			if (threat != null)
				threatDTOAddList.add(threat.createSlimDTO());
		}

		List<ThreatMeasureParentChild> measureAddAssocList = ThreatMeasureParentChild.where(
		        "parent_id = ?", getId());
		List<MeasureDTO> measureDTOAddList = new ArrayList<MeasureDTO>();
		for (ThreatMeasureParentChild link : measureAddAssocList) {
			Measure threat = Measure.findById(link.get("child_id"));
			if (threat != null)
				measureDTOAddList.add(threat.createSlimDTO());
		}

		return new ThreatDTO(getInteger("id"), getString("title"), getString("category"), getString("version"),
		        getString("description"), moduleDTOAddList, threatDTOAddList, measureDTOAddList);
	}

	@Override
	public ThreatDTO updateFromDTO(ThreatDTO updateObject, PushUpdateHandler server) {
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
