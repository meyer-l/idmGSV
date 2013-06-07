package de.gsv.idm.shared.comperator;

import java.util.Comparator;

import de.gsv.idm.shared.dto.HasName;

public class GeneralComperator<T extends HasName> implements Comparator<T> {
	
	public int compare(T d1, T d2) {
		return d1.getName().toLowerCase().compareTo(d2.getName().toLowerCase());
	}

}
