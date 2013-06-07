package de.gsv.idm.client.presenter.securityzone;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

import de.gsv.idm.client.presenter.Presenter;

public class SecurityzoneMeasuresDetailsPresenter implements Presenter {

	public interface SecurityzoneMeasuresDetailsDisplay {
		Widget asWidget();
	}

	private final SecurityzoneMeasuresDetailsDisplay display;

	public SecurityzoneMeasuresDetailsPresenter(SecurityzoneMeasuresDetailsDisplay display) {
		this.display = display;
	}

	@Override
	public IsWidget go() {
		bindView();
		return display.asWidget();
	}

	private void bindView() {

	}

}
