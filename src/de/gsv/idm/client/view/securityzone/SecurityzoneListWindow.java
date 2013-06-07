package de.gsv.idm.client.view.securityzone;

import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

import de.gsv.idm.client.presenter.securityzone.SecurityzoneListPresenter;

public class SecurityzoneListWindow extends Window {

	SecurityzoneListPresenter secruityzoneListPresenter;

	public SecurityzoneListWindow() {
		setWidth(com.google.gwt.user.client.Window.getClientWidth());
		setHeight(600);
		setWidth(1300);
		secruityzoneListPresenter = new SecurityzoneListPresenter(new SecurityzoneListView());
		ContentPanel mainPanel = new ContentPanel();
		mainPanel.setHeaderVisible(false);
		TextButton okButton = new TextButton("Ok");
		mainPanel.add(secruityzoneListPresenter.go());
		mainPanel.addButton(okButton);
		final SecurityzoneListWindow window = this;
		okButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				window.hide();
			}
		});
		add(mainPanel);
		setHeadingText("Schutzzonen bearbeiten");
		show();
	}
}
