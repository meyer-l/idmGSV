package de.gsv.idm.shared.comperator;

import java.util.Comparator;

import com.google.gwt.regexp.shared.MatchResult;
import com.google.gwt.regexp.shared.RegExp;

public class GSKComperator implements Comparator<String> {

	public int compare(String d1, String d2) {
		RegExp pattern = RegExp.compile("\\d.\\d{1,3}"); //\d\.\d{1,3}
		MatchResult string1Matcher = pattern.exec(d1.toLowerCase());
		MatchResult string2Matcher = pattern.exec(d2.toLowerCase());
		
		if (string1Matcher != null && string2Matcher != null) {
			String[] string1SortItems = string1Matcher.toString().split("\\.");
			String[] string2SortItems = string2Matcher.toString().split("\\.");
			if (string1SortItems.length > 0 && string2SortItems.length > 0) {
				Integer firstOrderSort1 = Integer.parseInt(string1SortItems[0]);
				Integer firstOrderSort2 = Integer.parseInt(string2SortItems[0]);
				Integer firstSort = firstOrderSort1.compareTo(firstOrderSort2);
				if (firstSort.equals(0) && string1SortItems.length > 1
				        && string2SortItems.length > 1) {
					Integer secondOrderSort1 = Integer.parseInt(string1SortItems[1]);
					Integer secondOrderSort2 = Integer.parseInt(string2SortItems[1]);
					return secondOrderSort1.compareTo(secondOrderSort2);
				} else {
					return firstSort;
				}
			}
		}
		return d1.toLowerCase().compareTo(d2.toLowerCase());

	}

}
