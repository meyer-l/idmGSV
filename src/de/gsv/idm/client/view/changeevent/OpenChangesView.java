package de.gsv.idm.client.view.changeevent;

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent.HasSelectHandlers;

import de.gsv.idm.client.images.ImageResources;
import de.gsv.idm.client.presenter.changeevent.OpenChangesPresenter.OpenChangesDisplay;
import de.gsv.idm.client.view.properties.ChangeEventDTOProperties;
import de.gsv.idm.shared.dto.ChangeEventDTO;

public class OpenChangesView extends EditableChangesView implements OpenChangesDisplay {

	TextButton uploadTelefonlistButton;

	public OpenChangesView() {
		this((ChangeEventDTOProperties) GWT.create(ChangeEventDTOProperties.class));
	}

	public OpenChangesView(ChangeEventDTOProperties create) {
		this(create, new StoreSortInfo<ChangeEventDTO>(create.date(), SortDir.DESC));
	}

	public OpenChangesView(ChangeEventDTOProperties properties,
	        StoreSortInfo<ChangeEventDTO> sortInfo) {
		super(properties, sortInfo);
	}

	@Override
	protected void addButtons() {
		uploadTelefonlistButton = new TextButton();
		mainPanel.addButton(uploadTelefonlistButton);
		super.addButtons();
		ImageResources imageBundler = GWT.create(ImageResources.class);
		mainPanel.getHeader().setIcon(imageBundler.help());
		mainPanel.getHeader().setToolTip(
		        "Änderungen am Informationsverbund die durch die Verarbeitung"
		                + " von Telefonlisten erkannt wurden.");
	}

	@Override
	public void setUploadTelefonlistButtonText(String text) {
		uploadTelefonlistButton.setText(text);

	}

	@Override
	public HasSelectHandlers getUploadTelefonlistButtonClick() {
		return uploadTelefonlistButton;
	}
}