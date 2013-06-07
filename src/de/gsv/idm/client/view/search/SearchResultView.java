package de.gsv.idm.client.view.search;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent.HasSelectHandlers;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.GroupingView;

import de.gsv.idm.client.presenter.search.SearchResult;
import de.gsv.idm.client.presenter.search.SearchResultPresenter.SearchResultDisplay;
import de.gsv.idm.client.view.general.GeneralGridView;
import de.gsv.idm.client.view.properties.SearchResultProperties;

public class SearchResultView extends GeneralGridView<SearchResult> implements SearchResultDisplay {

	TextButton okButton;

	// XXX: should be sorted, best hits first
	public SearchResultView() {
		this((SearchResultProperties) GWT.create(SearchResultProperties.class));
	}

	public SearchResultView(SearchResultProperties properties) {
		super(properties, new StoreSortInfo<SearchResult>(properties.implementedMeasuresSize(),
		        SortDir.DESC));

		okButton = new TextButton();
		mainPanel.addButton(okButton);

		final GroupingView<SearchResult> view = new GroupingView<SearchResult>();
		view.setShowGroupedColumn(false);
		view.setForceFit(true);
		grid.setView(view);
		view.groupBy(grid.getColumnModel().getColumn(0));
	}

	@Override
	protected ColumnModel<SearchResult> getColumnModel() {
		SearchResultProperties properties = GWT.create(SearchResultProperties.class);

		List<ColumnConfig<SearchResult, ?>> cfgs = new ArrayList<ColumnConfig<SearchResult, ?>>();

		ColumnConfig<SearchResult, String> implementedMeasuresLabel = new ColumnConfig<SearchResult, String>(
		        properties.implementedMeasuresLabel());
		implementedMeasuresLabel.setHeader("label");
		cfgs.add(implementedMeasuresLabel);

		ColumnConfig<SearchResult, String> name = new ColumnConfig<SearchResult, String>(
		        properties.name());
		name.setHeader("Name");
		cfgs.add(name);

		return new ColumnModel<SearchResult>(cfgs);
	}

	@Override
	public void setOkButtonText(String text) {
		okButton.setText(text);
	}

	@Override
	public HasSelectHandlers getOkButtonClick() {
		return okButton;
	}

}
