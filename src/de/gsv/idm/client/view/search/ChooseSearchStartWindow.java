package de.gsv.idm.client.view.search;

import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

import de.gsv.idm.client.presenter.search.ChooseSearchStartPresenter;



public class ChooseSearchStartWindow extends Window {

	private ChooseSearchStartPresenter presenter;

	public  ChooseSearchStartWindow(ChooseSearchStartPresenter presenter) {
		this(presenter, "Startpunkt für Suche auswählen");
	}

	public  ChooseSearchStartWindow(ChooseSearchStartPresenter presenter, String title) {
		
		setHeadingText(title);
		this.presenter = presenter;
		buildView();
	}

	private void buildView() {
		setBodyBorder(false);
		setWidth(380);
		setHeight(500);
		
		
		add(presenter.go());
		final ChooseSearchStartWindow window = this;
		presenter.getOkButtonClick().addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				window.hide();
			}
		});
		presenter.getAbortButtonClick().addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				window.hide();
			}
		});
		show();
	}
	
}
