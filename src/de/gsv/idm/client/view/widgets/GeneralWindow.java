package de.gsv.idm.client.view.widgets;

import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

import de.gsv.idm.client.presenter.Presenter;


public class GeneralWindow extends Window {

	protected Presenter presenter;
	private ContentPanel buttonPanel;
	protected BorderLayoutContainer mainPanel;
	
	TextButton okButton;

	public GeneralWindow(Presenter presenter) {
		this.presenter = presenter;
		buildView();
	}

	public GeneralWindow(Presenter presenter, String title) {
		this(presenter);
		setHeadingText(title);
	}
	
	private void buildView() {
		setBodyBorder(false);
		setWidth(900);
		setHeight(600);
		okButton = new TextButton("Ok");
		buttonPanel = new ContentPanel();
		buttonPanel.setHeaderVisible(false);
		mainPanel = new BorderLayoutContainer();
		buttonPanel.addButton(okButton);
		buttonPanel.setBodyBorder(false);
		buttonPanel.setHeaderVisible(false);
		buttonPanel.setBorders(false);
		mainPanel.setCenterWidget(presenter.go());
		mainPanel.setSouthWidget(buttonPanel,new BorderLayoutData(35));
		add(mainPanel);
		final GeneralWindow view = this;
		okButton.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
			        view.hide();
			}
		});	
		show();
	}
	
}
