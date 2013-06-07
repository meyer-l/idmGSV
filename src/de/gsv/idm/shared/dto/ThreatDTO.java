package de.gsv.idm.shared.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class ThreatDTO extends GeneralDTO<ThreatDTO> implements Serializable {

	private Integer id;
	private String name;
	private String category;
	private String version;
	private String description;
	private List<ModuleDTO> modules;
	private List<ThreatDTO> threats;
	private List<MeasureDTO> measures;
	
	private Boolean slim = false;
	
	

	

	public ThreatDTO() {

	}

	public ThreatDTO(Integer id, String name, String category, String version, String description,
	        List<ModuleDTO> modules, List<ThreatDTO> threats, List<MeasureDTO> measures) {
		this.id = id;
		this.name = name;
		this.category = category;
		this.version = version;
		this.description = description;
		this.modules = modules;
		this.threats = threats;
		this.measures = measures;
	}

	public ThreatDTO(Integer id, String name, String category) {
		this.id = id;
		this.name = name;
		this.category = category;
		slim = true;
		modules = new ArrayList<ModuleDTO>();
		threats = new ArrayList<ThreatDTO>();
		measures = new ArrayList<MeasureDTO>();
	}
	
	public ThreatDTO(Integer id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public List<ModuleDTO> getModules() {
    	return modules;
    }

	public void setModules(List<ModuleDTO> modules) {
    	this.modules = modules;
    }

	public List<ThreatDTO> getThreats() {
    	return threats;
    }

	public void setThreats(List<ThreatDTO> threats) {
    	this.threats = threats;
    }

	public List<MeasureDTO> getMeasures() {
    	return measures;
    }

	public void setMeasures(List<MeasureDTO> measures) {
    	this.measures = measures;
    }
	
	public void setCategory(String text){
		category = text;
	}
	
	public String getCategory(){
		return category;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getClassName() {
		return "Gefährdung";
	}
	
	public Boolean isSlim() {
		return slim;
	}

	public ThreatDTO clone() {
		ThreatDTO result = new ThreatDTO();
		if (id != null) {
			result.id = this.id;
			result.name = this.name;
			result.category = this.category;
			result.version = this.version;
			result.description = this.description;
			
			ArrayList<ModuleDTO> clonedModuleAdd = new ArrayList<ModuleDTO>();
			for(ModuleDTO module : modules){
				clonedModuleAdd.add(module);
			}
			result.setModules(clonedModuleAdd);
			
			ArrayList<ThreatDTO> clonedThreatAdd = new ArrayList<ThreatDTO>();
			for(ThreatDTO threat : threats){
				clonedThreatAdd.add(threat);
			}
			result.setThreats(clonedThreatAdd);
			
			ArrayList<MeasureDTO> clonedMeasuresAdd = new ArrayList<MeasureDTO>();
			for(MeasureDTO measure : measures){
				clonedMeasuresAdd.add(measure);
			}
			result.setMeasures(clonedMeasuresAdd);
		}
		return result;
	}

	@Override
    public ThreatDTO update(ThreatDTO toUpdate) {
		this.name = toUpdate.name;
		this.category = toUpdate.category;
		this.version = toUpdate.version;
		this.description = toUpdate.description;
		this.modules = toUpdate.modules;
		this.threats = toUpdate.threats;
		this.measures = toUpdate.measures;
		
		this.slim = toUpdate.slim;
	    return this;
    }

}
