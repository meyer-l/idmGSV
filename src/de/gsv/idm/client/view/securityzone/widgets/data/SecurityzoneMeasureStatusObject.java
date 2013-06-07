package de.gsv.idm.client.view.securityzone.widgets.data;

import de.gsv.idm.shared.dto.MeasureDTO;

public class SecurityzoneMeasureStatusObject {

	private MeasureDTO measure;
	private String status;
	private Boolean inherited;

	public SecurityzoneMeasureStatusObject(MeasureDTO measure, String status, Boolean inherited) {
		this.measure = measure;
		this.status = status;
		this.inherited = inherited;
	}

	public String getName() {
		return measure.getName();
	}

	public Integer getId(){
		return measure.getId();
	}

	public String getStatus() {
		return status;
	}

	public Boolean getInherited() {
		return inherited;
	}


}
