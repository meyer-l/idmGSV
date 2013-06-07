package de.gsv.idm.shared.model;

import java.util.ArrayList;
import java.util.List;

import org.javalite.activejdbc.Model;

public class DBHelper {
	
	public static <V extends Model, T extends Model> void manageAssociations(V manageObject, List<T> currentAssociations,
	       List<T> newAssociations) {
		ArrayList<T> clonedAssociaitons = new ArrayList<T>(currentAssociations);
		currentAssociations.removeAll(newAssociations);
		newAssociations.removeAll(clonedAssociaitons);

		for (T oldAssociation : currentAssociations) {
			manageObject.remove(oldAssociation);
		}

		for (T newAssociation : newAssociations) {
			manageObject.add(newAssociation);
		}
	}
}
