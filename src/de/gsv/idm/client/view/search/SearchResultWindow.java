package de.gsv.idm.client.view.search;

import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

import de.gsv.idm.client.presenter.search.SearchResultPresenter;

public class SearchResultWindow extends Window {
	private SearchResultPresenter presenter;

	public  SearchResultWindow(SearchResultPresenter presenter) {
		this(presenter, "Suchergebnis");
	}

	public  SearchResultWindow(SearchResultPresenter presenter, String title) {
		
		setHeadingText(title);
		this.presenter = presenter;
		buildView();
	}

	private void buildView() {
		setBodyBorder(false);
		setWidth(380);
		setHeight(500);
		
		
		add(presenter.go());
		final SearchResultWindow window = this;
		presenter.getOkButtonClick().addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				window.hide();
			}
		});
		show();
	}
}
