package de.gsv.idm.shared.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class ModuleDTO extends GeneralDTO<ModuleDTO> implements Serializable {

	private Integer id;
	private String name;
	private String category;
	private String version;
	private String moduleDescription;
	private String threatDescription;
	private String measureDescription;

	private List<ThreatDTO> threats;
	private List<MeasureLinkDTO> measures;
	private List<ModuleDTO> modulesAdd;
	private List<ThreatDTO> threatsAdd;
	private List<MeasureDTO> measuresAdd;

	private Boolean slim = false;

	public ModuleDTO() {

	}
	
	public ModuleDTO(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public ModuleDTO(Integer id, String name, String category, List<MeasureLinkDTO> measures) {
		this.id = id;
		this.name = name;
		this.category = category;
		slim = true;
		this.threats = new ArrayList<ThreatDTO>();
		this.measures = measures;
		this.modulesAdd = new ArrayList<ModuleDTO>();
		this.threatsAdd = new ArrayList<ThreatDTO>();
		this.measuresAdd = new ArrayList<MeasureDTO>();
	}

	public ModuleDTO(Integer id, String name, String category, String version, String moduleDescription,
	        String threatDescription, String measureDescription, List<ThreatDTO> threats,
	        List<MeasureLinkDTO> measures, List<ModuleDTO> modulesAdd, List<ThreatDTO> threatsAdd,
	        List<MeasureDTO> measuresAdd) {
		this.id = id;
		this.name = name;
		this.category = category;
		this.version = version;
		this.moduleDescription = moduleDescription;
		this.threatDescription = threatDescription;
		this.measureDescription = measureDescription;
		this.threats = threats;
		this.measures = measures;
		this.modulesAdd = modulesAdd;
		this.threatsAdd = threatsAdd;
		this.measuresAdd = measuresAdd;
	}

	public ModuleDTO(Integer id) {
		this.id = id;
		slim = true;
	}

	public List<ModuleDTO> getModulesAdd() {
		return modulesAdd;
	}

	public void setModulesAdd(List<ModuleDTO> modulesAdd) {
		this.modulesAdd = modulesAdd;
	}

	public List<ThreatDTO> getThreatsAdd() {
		return threatsAdd;
	}

	public void setThreatsAdd(List<ThreatDTO> threatsAdd) {
		this.threatsAdd = threatsAdd;
	}

	public List<MeasureDTO> getMeasuresAdd() {
		return measuresAdd;
	}

	public void setMeasuresAdd(List<MeasureDTO> measuresAdd) {
		this.measuresAdd = measuresAdd;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getModuleDescription() {
		return moduleDescription;
	}

	public void setModuleDescription(String moduleDescription) {
		this.moduleDescription = moduleDescription;
	}

	public String getThreatDescription() {
		return threatDescription;
	}

	public void setThreatDescription(String threatDescription) {
		this.threatDescription = threatDescription;
	}

	public String getMeasureDescription() {
		return measureDescription;
	}

	public void setMeasureDescription(String measureDescription) {
		this.measureDescription = measureDescription;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setCategory(String text){
		category = text;
	}
	
	public String getCategory(){
		return category;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<ThreatDTO> getThreats() {
		return threats;
	}

	public void setThreats(List<ThreatDTO> threats) {
		this.threats = threats;
	}

	public List<MeasureLinkDTO> getMeasures() {
		return measures;
	}

	public void setMeasures(List<MeasureLinkDTO> measures) {
		this.measures = measures;
	}

	public String getClassName() {
		return "Gefährdung";
	}

	public Boolean isSlim() {
		return slim;
	}

	public ModuleDTO clone() {
		ModuleDTO result = new ModuleDTO();
		if (id != null) {
			result.id = this.id;
			result.name = this.name;
			result.category = this.category;
			result.version = this.version;
			result.moduleDescription = this.moduleDescription;
			result.threatDescription = this.threatDescription;
			result.measureDescription = this.measureDescription;
			result.slim = this.slim;
			
			ArrayList<MeasureLinkDTO> clonedMeasures = new ArrayList<MeasureLinkDTO>();
			for(MeasureLinkDTO measureLink : measures){
				clonedMeasures.add(measureLink);
			}
			result.setMeasures(clonedMeasures);
			
			ArrayList<ThreatDTO> clonedThreat = new ArrayList<ThreatDTO>();
			for(ThreatDTO threat : threats){
				clonedThreat.add(threat);
			}
			result.setThreats(clonedThreat);
			
			ArrayList<ModuleDTO> clonedModuleAdd = new ArrayList<ModuleDTO>();
			for(ModuleDTO module : modulesAdd){
				clonedModuleAdd.add(module);
			}
			result.setModulesAdd(clonedModuleAdd);
			
			ArrayList<ThreatDTO> clonedThreatAdd = new ArrayList<ThreatDTO>();
			for(ThreatDTO threat : threatsAdd){
				clonedThreatAdd.add(threat);
			}
			result.setThreatsAdd(clonedThreatAdd);
			
			ArrayList<MeasureDTO> clonedMeasuresAdd = new ArrayList<MeasureDTO>();
			for(MeasureDTO measure : measuresAdd){
				clonedMeasuresAdd.add(measure);
			}
			result.setMeasuresAdd(clonedMeasuresAdd);
		}
		return result;
	}

	@Override
	public ModuleDTO update(ModuleDTO toUpdate) {
		this.name = toUpdate.name;
		this.category = toUpdate.category;
		this.version = toUpdate.version;
		this.moduleDescription = toUpdate.moduleDescription;
		this.threatDescription = toUpdate.threatDescription;
		this.measureDescription = toUpdate.measureDescription;
		this.threats = toUpdate.threats;
		this.measures = toUpdate.measures;
		this.modulesAdd = toUpdate.modulesAdd;
		this.threatsAdd = toUpdate.threatsAdd;
		this.measuresAdd = toUpdate.measuresAdd;

		this.slim = toUpdate.slim;
		return this;
	}

	public MeasureLinkDTO getMeasureLink(MeasureDTO measure) {
	    for(MeasureLinkDTO link : measures ){
	    	if(link.getMeasure().equals(measure)){
	    		return link;
	    	}
	    }
	    return null;
    }

}
