package de.gsv.idm.shared.model;

import org.javalite.activejdbc.Model;

public class AssetsMeasuresSecurityzonesMeasures extends Model {
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (other instanceof AssetsMeasuresSecurityzonesMeasures)
			return (getId() == ((AssetsMeasuresSecurityzonesMeasures) other).getId())
			        || (getInteger("assets_measures_id").equals(
			                ((AssetsMeasuresSecurityzonesMeasures) other)
			                        .getInteger("assets_measures_id")) && getInteger(
			                "securityzones_measures_id").equals(
			                ((AssetsMeasuresSecurityzonesMeasures) other)
			                        .getInteger("securityzones_measures_id")));
		return false;
	}
}
