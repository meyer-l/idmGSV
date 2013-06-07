package de.gsv.idm.client.view.widgets.window;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.PasswordField;
import com.sencha.gxt.widget.core.client.form.TextField;

import de.gsv.idm.client.IdmGsv;

public class LoginWindow extends Window {

	TextField username = new TextField();
	PasswordField password = new PasswordField();
	CheckBox rememberMe = new CheckBox();
	TextButton okButton;

	public LoginWindow(String title, Boolean cookieFound) {
		setHeadingText(title);
		setWidth(350);
		setHeight(165);
		FramedPanel mainPanel = new FramedPanel();
		mainPanel.setHeaderVisible(false);
		mainPanel.setBorders(false);
		VerticalLayoutContainer verticalLayout = new VerticalLayoutContainer();
		verticalLayout.add(new FieldLabel(username, "Benutzer"), new VerticalLayoutData(0.98, -1,
		        new Margins(5, 0, 5, 10)));
		verticalLayout.add(new FieldLabel(password, "Passwort"), new VerticalLayoutData(0.98, -1,
		        new Margins(5, 0, 5, 10)));

		username.addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					if (password.getValue() ==  null || password.getValue().isEmpty()) {
						password.focus();
					} else {
						login();
					}
				}
			}
		});
		username.focus();
		password.addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					if (username.getValue() ==  null || username.getValue().isEmpty()) {
						username.focus();
					} else {
						login();
					}
				}
			}
		});

		HorizontalPanel rememberMePanel = new HorizontalPanel();
		rememberMe.setValue(cookieFound);
		rememberMePanel.add(rememberMe);
		verticalLayout.add(new FieldLabel(rememberMePanel, "Eingeloggt bleiben?"),
		        new VerticalLayoutData(-1, -1, new Margins(5, 0, 5, 10)));
		mainPanel.add(verticalLayout);
		okButton = new TextButton("Ok");
		okButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				login();

			}

		});

		TextButton abortButton = new TextButton("Abbrechen");
		abortButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				username.clear();
				password.clear();
			}

		});
		mainPanel.addButton(abortButton);
		mainPanel.addButton(okButton);
		add(mainPanel);
		show();
	}

	private void login() {
		IdmGsv.handleLogin(username.getValue(), password.getCurrentValue(), rememberMe.getValue());
		hide();
	}
}
