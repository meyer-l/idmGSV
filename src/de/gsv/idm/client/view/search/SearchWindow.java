package de.gsv.idm.client.view.search;

import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

import de.gsv.idm.client.presenter.search.SearchPresenter;

public class SearchWindow extends Window {

	private SearchPresenter presenter;

	public SearchWindow(SearchPresenter presenter) {
		this.presenter = presenter;
		buildView();
	}

	public SearchWindow(SearchPresenter presenter, String title) {
		this(presenter);
		setHeadingText(title);
	}

	private void buildView() {
		setBodyBorder(false);
		setWidth(800);
		setHeight(605);
		
		
		add(presenter.go());
		final SearchWindow window = this;

		presenter.getOkButtonClick().addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				window.hide();
			}
		});
		show();
	}

}
