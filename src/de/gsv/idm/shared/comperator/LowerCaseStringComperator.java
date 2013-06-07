package de.gsv.idm.shared.comperator;

import java.util.Comparator;

public class LowerCaseStringComperator implements Comparator<String> {
	
	public int compare(String d1, String d2) {
		if(d1 != null && d2 != null){
			return d1.toLowerCase().compareTo(d2.toLowerCase());
		} else if (d1 != null){
			return -1;
		} else {
			return 1;
		}
		
		
	}
}
