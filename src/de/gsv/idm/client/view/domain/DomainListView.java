package de.gsv.idm.client.view.domain;

import com.google.gwt.event.shared.HandlerManager;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent.HasSelectHandlers;

import de.gsv.idm.client.RpcController.DBController;
import de.gsv.idm.client.event.view.domain.ViewDomainViewEvent;
import de.gsv.idm.client.presenter.domain.DomainListPresenter.DomainDisplay;
import de.gsv.idm.client.view.general.GeneralListView;
import de.gsv.idm.shared.dto.DomainDTO;

public class DomainListView extends GeneralListView<DomainDTO, DomainDisplay> implements
        DomainDisplay {

	private TextButton cancelButton;

	public DomainListView() {
		super(0.8747);
		cancelButton = new TextButton();
		cancelButton.setVisible(false);
		this.addButtons();
	}

	public void setCancelText(String text) {
		cancelButton.setText(text);
	}

	public HasSelectHandlers getCancelClick() {
		return cancelButton;
	}

	public void setCancelVisibility(boolean visibilty) {
		cancelButton.setVisible(visibilty);
	}

	@Override
	public void addButtons() {
		listPanel.addButton(cancelButton);
		super.addButtons();
	}

	protected void onDoubleClick() {
		HandlerManager eventBus = DBController.getInstance().getEventBus();
		DomainDTO domain = list.getSelectionModel().getSelectedItem();
		if (domain != null) {
			eventBus.fireEvent(new ViewDomainViewEvent(domain.getId()));
		}

	}
}
