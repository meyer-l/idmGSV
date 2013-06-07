package de.gsv.idm.client.view.properties;

import com.sencha.gxt.core.client.ValueProvider;

import de.gsv.idm.client.presenter.search.SearchResult;

public interface SearchResultProperties extends HasKeyAndName<SearchResult> {

	ValueProvider<SearchResult, String> implementedMeasuresLabel();
	ValueProvider<SearchResult, Integer> implementedMeasuresSize();
}
