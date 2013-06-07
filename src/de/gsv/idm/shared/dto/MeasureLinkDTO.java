package de.gsv.idm.shared.dto;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MeasureLinkDTO extends GeneralDTO<MeasureLinkDTO> implements Serializable {
	
	private MeasureDTO measure;	
	private String category;
	
	public MeasureLinkDTO(){
		
	}
	
	public MeasureLinkDTO(MeasureDTO measure, String category){
		this.measure = measure;
		this.category = category;
	}
	
	public MeasureDTO getMeasure() {
    	return measure;
    }
	public void setMeasure(MeasureDTO measure) {
    	this.measure = measure;
    }
	public String getCategory() {
    	return category;
    }
	public void setCategory(String category) {
    	this.category = category;
    }
	
	public String getName(){
		return measure.getName();
	}
	
	public Integer getId(){
		return measure.getId();
	}

	@Override
    public String getClassName() {
	    return "MeasureLink";
    }

	@Override
    public Boolean isSlim() {
	    return measure.isSlim();
    }

	@Override
    public MeasureLinkDTO update(MeasureLinkDTO toUpdate) {
		this.measure = toUpdate.measure;
		this.category = toUpdate.category;
	    return this;
    }

}
