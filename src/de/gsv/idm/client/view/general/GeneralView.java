package de.gsv.idm.client.view.general;

import com.google.gwt.user.client.Window;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;

public class GeneralView {

	protected static final int CLIENT_WIDTH = Window.getClientWidth();
	protected static final int CLIENT_HEIGHT = Window.getClientHeight();

	protected VerticalLayoutData getFormData() {
		return new VerticalLayoutData(0.98, -1, new Margins(5, 0, 5, 10));
	}

	protected VerticalLayoutData getHalfFormData() {
		return new VerticalLayoutData(0.5, -1, new Margins(5, 0, 5, 10));
	}
}
