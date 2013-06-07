package de.gsv.idm.shared.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class AssetMeasureLinkDTO extends GeneralDTO<AssetMeasureLinkDTO> implements
        Serializable {

	private AssettypeMeasureLinkDTO assettypeMeasureLink;
	private MeasureDTO measure;
	private Integer id;
	private EmployeeDTO responsiblePerson;
	private String status;
	private String cost;
	private Boolean notRemovable;
	private Boolean passOn;

	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (other instanceof AssetMeasureLinkDTO)
			return (getId() != null && getId() == ((HasId) other).getId())
			        || getMeasure().equals(
			                ((AssetMeasureLinkDTO) other).getMeasure());
		if (other instanceof HasId)
			return (getId() != null && getId() == ((HasId) other).getId());
		return false;
	}

	public AssetMeasureLinkDTO() {

	}

	public AssetMeasureLinkDTO(AssettypeMeasureLinkDTO assetMeasureLink, MeasureDTO measure, Integer id,
	        EmployeeDTO responsiblePerson, String status, String cost, Boolean notRemovable, Boolean passOn) {
		this.assettypeMeasureLink = assetMeasureLink;
		this.measure = measure;
		this.id = id;
		this.responsiblePerson = responsiblePerson;
		this.status = status;
		this.cost = cost;
		this.notRemovable = notRemovable;
		this.passOn = passOn;
	}
	
	public AssetMeasureLinkDTO(MeasureDTO measure) {
		this.measure = measure;
		this.status = MeasureStatusObject.getNotProcessed();
		this.notRemovable = false;
	}

	@Override
	public String getName() {
		return measure.getName();
	}

	public MeasureDTO getMeasure() {
    	return measure;
    }

	public void setMeasure(MeasureDTO measure) {
    	this.measure = measure;
    }

	public AssettypeMeasureLinkDTO getAssettypeMeasureLink() {
		return assettypeMeasureLink;
	}

	public void setAssettypeMeasureLink(AssettypeMeasureLinkDTO assetMeasureLink) {
		this.assettypeMeasureLink = assetMeasureLink;
	}
	
	public String getCategory(){
		String result = "";
		if(assettypeMeasureLink != null){
			result = assettypeMeasureLink.getCategory();
		}
		return result;
	}

	public EmployeeDTO getResponsiblePerson() {
		if (responsiblePerson != null) {
			return responsiblePerson;
		} else if(assettypeMeasureLink != null) {
			return assettypeMeasureLink.getResponsiblePerson();
		} else {
			return null;
		}
	}

	public EmployeeDTO getModelResponsiblePerson() {
		return responsiblePerson;
	}

	public void setResponsiblePerson(EmployeeDTO responsiblePerson) {
		this.responsiblePerson = responsiblePerson;
	}

	public String getStatus() {
		if (status != null) {
			return status;
		} else if(assettypeMeasureLink != null) {
			return assettypeMeasureLink.getStatus();
		} else {
			return null;
		}
	}

	public String getModelStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCost() {
		if (cost != null) {
			return cost;
		} else if (assettypeMeasureLink != null) {
			return assettypeMeasureLink.getCost();
		} else {
			return null;
		}
	}

	public String getModelCost() {
		return cost;
	}

	public void setCost(String cost) {
		this.cost = cost;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getNotRemovable() {
    	return notRemovable;
    }

	public void setNotRemovable(Boolean notRemovable) {
    	this.notRemovable = notRemovable;
    }
	
	public Boolean getPassOn() {
    	return passOn;
    }

	public void setPassOn(Boolean passOn) {
    	this.passOn = passOn;
    }
	
	@Override
	public String getClassName() {
		return "Maßnahmen-Verknüpfung";
	}

	@Override
	public Boolean isSlim() {
		return false;
	}

	@Override
	public AssetMeasureLinkDTO update(AssetMeasureLinkDTO toUpdate) {
		this.assettypeMeasureLink = toUpdate.assettypeMeasureLink;
		this.measure = toUpdate.measure;
		this.responsiblePerson = toUpdate.responsiblePerson;
		this.status = toUpdate.status;
		this.cost = toUpdate.cost;
		this.notRemovable = toUpdate.notRemovable;
		this.passOn = toUpdate.passOn;
		return this;
	}

}
