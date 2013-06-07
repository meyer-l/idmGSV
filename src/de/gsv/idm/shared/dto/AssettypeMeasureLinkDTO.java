package de.gsv.idm.shared.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class AssettypeMeasureLinkDTO extends GeneralDTO<AssettypeMeasureLinkDTO> implements
        Serializable {
	private MeasureDTO measure;
	private Integer id;
	private EmployeeDTO responsiblePerson;
	private String status;
	private String cost;
	private Boolean passOn;
	private Boolean manualAdd;

	private List<ModuleDTO> linkedModules;
	private List<SecurityzoneDTO> linkedSecurityzones;

	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (other instanceof AssettypeMeasureLinkDTO)
			return (getId() != null && getId() == ((HasId) other).getId())
			        || getMeasure().equals(((AssettypeMeasureLinkDTO) other).getMeasure());
		if (other instanceof HasId)
			return (getId() != null && getId() == ((HasId) other).getId());
		return false;
	}

	public AssettypeMeasureLinkDTO() {

	}

	public AssettypeMeasureLinkDTO(MeasureDTO measure, Boolean manualAdd) {
		this.measure = measure;
		this.manualAdd = manualAdd;
		this.passOn = false;
		this.status = MeasureStatusObject.getNotProcessed();
		this.linkedModules = new ArrayList<ModuleDTO>();
		this.linkedSecurityzones = new ArrayList<SecurityzoneDTO>();
	}

	public AssettypeMeasureLinkDTO(MeasureDTO measure, Integer id, EmployeeDTO responsiblePerson,
	        String status, String cost, Boolean passOn, Boolean manualAdd,
	        List<ModuleDTO> linkedModules, List<SecurityzoneDTO> linkedSecurityzones) {
		this.measure = measure;
		this.id = id;
		this.responsiblePerson = responsiblePerson;
		this.status = status;
		this.cost = cost;
		this.passOn = passOn;
		this.manualAdd = manualAdd;

		this.linkedModules = linkedModules;
		this.linkedSecurityzones = linkedSecurityzones;
	}

	public String getName() {
		return measure.getName();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public MeasureDTO getMeasure() {
		return measure;
	}

	public void setMeasure(MeasureDTO measure) {
		this.measure = measure;
	}

	public EmployeeDTO getResponsiblePerson() {
		return responsiblePerson;
	}

	public void setResponsiblePerson(EmployeeDTO responsiblePerson) {
		this.responsiblePerson = responsiblePerson;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public Boolean isPassOn() {
		return passOn;
	}

	public void setPassOn(Boolean passOn) {
		this.passOn = passOn;
	}

	public Boolean isManualAdd() {
		return manualAdd;
	}

	public void setManualAdd(Boolean manualAdd) {
		this.manualAdd = manualAdd;
	}

	public List<ModuleDTO> getLinkedModules() {
		return linkedModules;
	}

	public void setLinkedModules(List<ModuleDTO> linkedModules) {
		this.linkedModules = linkedModules;
	}

	public List<SecurityzoneDTO> getLinkedSecurityzones() {
		return linkedSecurityzones;
	}

	public void setLinkedSecurityzones(List<SecurityzoneDTO> linkedSecurityzones) {
		this.linkedSecurityzones = linkedSecurityzones;
	}
	
	public String getCategory(){
		List<MeasureLinkDTO> moduleMeasureLinks= new ArrayList<MeasureLinkDTO>();
		String result = "";
		for(ModuleDTO module : linkedModules){
			MeasureLinkDTO link = module.getMeasureLink(measure);
			if(link != null){
				moduleMeasureLinks.add(link);
			}			
		}
		for(MeasureLinkDTO link : moduleMeasureLinks){
			if(result.equals("(A)")){
				break;
			} else if(link.getCategory().equals("(A)") || link.getCategory().equals("(B)") ) {
				result = link.getCategory();
			} else if(link.getCategory().equals("(C)") && !result.equals("(B)")){
				result = link.getCategory();
			} else if(link.getCategory().equals("(Z)") && (!result.equals("(B)")) || !result.equals("(C)")){
				result = link.getCategory();
			} else if(link.getCategory().equals("(W)") && (!result.equals("(B)")) || !result.equals("(C)") || !result.equals("(Z)")){
				result = link.getCategory();
			}
		}
		return result;
	}

	public String getClassName() {
		return "Maßnahmen-Verknüpfung";
	}

	public Boolean isSlim() {
		return false;
	}

	public AssettypeMeasureLinkDTO clone() {
		AssettypeMeasureLinkDTO result = new AssettypeMeasureLinkDTO();
			result.id = this.id;
			if (responsiblePerson != null) {
				result.responsiblePerson = this.responsiblePerson;
			}

			result.passOn = this.passOn;
			result.manualAdd = this.manualAdd;
			result.measure = this.measure;
			result.status = this.status;
			result.cost = this.cost;
			result.linkedModules = this.linkedModules;
			result.linkedSecurityzones = this.linkedSecurityzones;

		return result;

	}

	@Override
	public AssettypeMeasureLinkDTO update(AssettypeMeasureLinkDTO toUpdate) {
		this.measure = toUpdate.measure;
		this.responsiblePerson = toUpdate.responsiblePerson;
		this.status = toUpdate.status;
		this.cost = toUpdate.cost;
		this.passOn = toUpdate.passOn;
		this.manualAdd = toUpdate.manualAdd;

		this.linkedModules = toUpdate.linkedModules;
		this.linkedSecurityzones = toUpdate.linkedSecurityzones;

		return this;
	}

	

}
