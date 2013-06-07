package de.gsv.idm.client.view.widgets.window;

import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.widget.core.client.Dialog;

public class FormNotValidDialog extends Dialog {

	public FormNotValidDialog(String title, String text) {
		setHeadingText(title);
		setPredefinedButtons(PredefinedButton.OK);
		setBodyStyleName("pad-text");
		add(new Label(text));
		getBody().addClassName("pad-text");
		setHideOnButtonClick(true);
		setWidth(300);
		show();
	}

}
