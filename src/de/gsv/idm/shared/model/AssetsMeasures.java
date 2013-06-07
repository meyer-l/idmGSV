package de.gsv.idm.shared.model;

import java.util.List;

import org.javalite.activejdbc.Model;

import de.gsv.idm.shared.dto.AssetMeasureLinkDTO;
import de.gsv.idm.shared.dto.AssettypeMeasureLinkDTO;
import de.gsv.idm.shared.dto.EmployeeDTO;
import de.gsv.idm.shared.dto.MeasureDTO;

public class AssetsMeasures extends Model {

	public AssetMeasureLinkDTO createDTO() {
		AssetsMeasuresAssettypesMeasures assettypeMeasuresLink = AssetsMeasuresAssettypesMeasures
		        .findFirst("assets_measures_id = ? AND measure_id = ?", getInteger("id"),
		                getInteger("measure_id"));
		AssettypeMeasureLinkDTO assettypeMeasureDTO = null;
		if (assettypeMeasuresLink != null) {
			AssettypesMeasures assettypeMeasure = AssettypesMeasures.findById(assettypeMeasuresLink
			        .getInteger("assettypes_measures_id"));
			assettypeMeasureDTO = assettypeMeasure.createDTO();
		}
		
		AssetsMeasuresSecurityzonesMeasures securityzoneMeasuresLink = AssetsMeasuresSecurityzonesMeasures
		        .findFirst("assets_measures_id = ?", getInteger("id"));
		Boolean notRemovable = false;
		if(assettypeMeasureDTO != null || securityzoneMeasuresLink != null){
			notRemovable = true;
		}

		MeasureDTO measureDTO = null;
		Measure measureDB = Measure.findById(getInteger("measure_id"));
		if (measureDB != null) {
			measureDTO = measureDB.createSlimDTO();
		}

		EmployeeDTO moduleResponsiplePersonDTO = null;
		Employee moduleResponsiplePersonDB = Employee.findById(getInteger("responsible_person_id"));
		if (moduleResponsiplePersonDB != null) {
			moduleResponsiplePersonDTO = moduleResponsiplePersonDB.createSlimDTO();
		}

		return new AssetMeasureLinkDTO(assettypeMeasureDTO, measureDTO, (Integer) getId(),
		        moduleResponsiplePersonDTO, getString("status"), getString("cost"), notRemovable, getBoolean("pass_on"));

	}

	public void completeDelete() {
		Integer id = getInteger("id");
			List<AssetsMeasuresAssettypesMeasures> assetMeasureAssettypeMeasures = AssetsMeasuresAssettypesMeasures
			        .where("assets_measures_id = ?", id);
			for (AssetsMeasuresAssettypesMeasures assetMeasureAssettypeMeasure : assetMeasureAssettypeMeasures) {
				assetMeasureAssettypeMeasure.deleteCascadeShallow();
			}
			List<AssetsMeasuresSecurityzonesMeasures> assetsMeasuresSecurityzonesMeasures = AssetsMeasuresSecurityzonesMeasures
			        .where("assets_measures_id = ?", id);
			for (AssetsMeasuresSecurityzonesMeasures assetMeasureAssettypeMeasure : assetsMeasuresSecurityzonesMeasures) {
				assetMeasureAssettypeMeasure.deleteCascadeShallow();
			}
		deleteCascadeShallow();
	    
    }
}
