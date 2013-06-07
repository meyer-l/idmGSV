package de.gsv.idm.shared.model;

import org.javalite.activejdbc.Model;

public class SecurityzonesMeasures extends Model {
	public boolean equals(Object other) {
		if (this == other)
			return true;
		if (other == null)
			return false;
		if (other instanceof SecurityzonesMeasures)
			return (getId() == ((SecurityzonesMeasures) other).getId())
			        || (getInteger("securityzone_id").equals(
			                ((SecurityzonesMeasures) other).getInteger("securityzone_id")) && getInteger(
			                "measure_id").equals(
			                ((SecurityzonesMeasures) other).getInteger("measure_id")));
		return false;
	}
}
