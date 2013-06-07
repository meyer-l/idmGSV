package de.gsv.idm.client.view.search;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent.HasSelectHandlers;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;

import de.gsv.idm.client.presenter.search.SearchPresenter.SearchDisplay;
import de.gsv.idm.client.view.general.GeneralView;
import de.gsv.idm.client.view.gsk.widgets.MeasureGridList;
import de.gsv.idm.client.view.informationdomain.assettype.widgets.AssettypeGridList;
import de.gsv.idm.client.view.properties.AssettypeDTOProperties;
import de.gsv.idm.client.view.properties.MeasureDTOProperties;
import de.gsv.idm.client.view.properties.SecurityzoneDTOProperties;
import de.gsv.idm.shared.dto.AssettypeDTO;
import de.gsv.idm.shared.dto.MeasureDTO;
import de.gsv.idm.shared.dto.SecurityzoneDTO;

public class SearchView extends GeneralView implements SearchDisplay {

	private BorderLayoutContainer mainPanel;

	ContentPanel buttonPanel;
	TextButton searchButton;
	TextButton abortButton;
	TextField assetField;
	TextButton chooseStartAsset;
	ListStore<AssettypeDTO> assettypeStore;

	ListStore<SecurityzoneDTO> securityzoneStore;
	ComboBox<SecurityzoneDTO> securityzoneBox;
	SecurityzoneDTO previousSecurityzoneDTO = null;

	ListStore<MeasureDTO> measureStore;
	AssettypeGridList assettypeGridList;
	MeasureGridList measureGridList;

	static final int LABEL_WIDTH = 200;

	public SearchView() {
		buttonPanel = new ContentPanel();
		buttonPanel.setHeaderVisible(false);
		buttonPanel.setBorders(false);
		mainPanel = new BorderLayoutContainer();
		new ScrollPanel(mainPanel);
		searchButton = new TextButton("");
		abortButton = new TextButton("");
		mainPanel.setSouthWidget(buttonPanel, new BorderLayoutData(35));
		buttonPanel.addButton(abortButton);
		buttonPanel.addButton(searchButton);

		VerticalLayoutContainer verticalSearchContainer = new VerticalLayoutContainer();

		assetField = new TextField();
		assetField.setWidth(150);
		assetField.disable();
		FieldLabel assetLabel = new FieldLabel(assetField, "Startpunkt der Suche");
		assetLabel.setLabelWidth(LABEL_WIDTH);
		VerticalLayoutContainer northVerticalContainer = new VerticalLayoutContainer();
		HorizontalLayoutContainer horizontal = new HorizontalLayoutContainer();
		horizontal.add(assetLabel, new HorizontalLayoutData(-1, 1, new Margins(4, 4, 4, 0)));
		chooseStartAsset = new TextButton();
		horizontal.add(chooseStartAsset, new HorizontalLayoutData(-1, 1, new Margins(4)));
		northVerticalContainer.add(horizontal, new VerticalLayoutData(-1, -1, new Margins(5, 0, 5,
		        10)));
		mainPanel.setNorthWidget(northVerticalContainer, new BorderLayoutData(35));
		// Assettypes
		AssettypeDTOProperties assettypeProperties = GWT.create(AssettypeDTOProperties.class);
		assettypeStore = new ListStore<AssettypeDTO>(assettypeProperties.key());
		assettypeGridList = new AssettypeGridList(assettypeStore);
		assettypeGridList.setText("Zu suchende Asset-Grundtypen");
		assettypeGridList.setHeight(130);
		assettypeGridList.setLabelWidth(LABEL_WIDTH);
		verticalSearchContainer.add(assettypeGridList, getFormData());
		// Securityzone
		SecurityzoneDTOProperties securityzoneProperties = GWT
		        .create(SecurityzoneDTOProperties.class);
		securityzoneStore = new ListStore<SecurityzoneDTO>(securityzoneProperties.key());
		securityzoneStore.addSortInfo(new StoreSortInfo<SecurityzoneDTO>(securityzoneProperties
		        .orderNumber(), SortDir.ASC));

		securityzoneBox = new ComboBox<SecurityzoneDTO>(securityzoneStore,
		        securityzoneProperties.label());
		securityzoneBox.setEditable(false);
		securityzoneBox.addBeforeSelectionHandler(new BeforeSelectionHandler<SecurityzoneDTO>() {

			@Override
			public void onBeforeSelection(BeforeSelectionEvent<SecurityzoneDTO> event) {
				previousSecurityzoneDTO = securityzoneBox.getCurrentValue();
			}
		});
		securityzoneBox.setTriggerAction(TriggerAction.ALL);
		FieldLabel securityzoneLabel = new FieldLabel(securityzoneBox,
		        "Zu suchende Schutzzone");
		securityzoneLabel.setLabelWidth(LABEL_WIDTH);
		FieldLabel seperatorLabel = new FieldLabel(null,"");
		seperatorLabel.setLabelSeparator("");
		verticalSearchContainer.add(seperatorLabel, getFormData());
		FieldLabel seperatorLabel2 = new FieldLabel(null,"");
		seperatorLabel2.setLabelSeparator("");
		verticalSearchContainer.add(seperatorLabel2, getFormData());
		FieldLabel seperatorLabel3 = new FieldLabel(null,"");
		seperatorLabel3.setLabelSeparator("");
		verticalSearchContainer.add(seperatorLabel3, getFormData());
		FieldLabel seperatorLabel4 = new FieldLabel(null,"");
		seperatorLabel4.setLabelSeparator("");
		verticalSearchContainer.add(seperatorLabel4, getFormData());
		FieldLabel seperatorLabel5 = new FieldLabel(null,"");
		seperatorLabel5.setLabelSeparator("");
		verticalSearchContainer.add(seperatorLabel5, getFormData());
		verticalSearchContainer.add(securityzoneLabel, getFormData());
		// Measures
		MeasureDTOProperties measureProperties = GWT.create(MeasureDTOProperties.class);
		measureStore = new ListStore<MeasureDTO>(measureProperties.key());
		measureGridList = new MeasureGridList(measureStore, measureProperties);
		measureGridList.setText("und/oder zu suchende Maßnahmen");
		measureGridList.setLabelWidth(LABEL_WIDTH);
		verticalSearchContainer.add(measureGridList, getFormData());
		mainPanel.setCenterWidget(verticalSearchContainer, new BorderLayoutData(120));

		// WestPanel

	}

	@Override
	public Widget asWidget() {

		return mainPanel;
	}

	@Override
	public HasSelectHandlers getOkButtonClick() {
		return abortButton;
	}

	@Override
	public void setAbortButtonText(String text) {
		abortButton.setText(text);
	}

	@Override
	public HasSelectHandlers getSearchButtonClick() {
		return searchButton;
	}

	@Override
	public void setSearchButtonText(String text) {
		searchButton.setText(text);
	}

	@Override
	public HasSelectHandlers getAddAssettypeButtonClick() {
		return assettypeGridList.getAddButtonClick();
	}

	@Override
	public ListStore<AssettypeDTO> getAssettypeStore() {
		return assettypeStore;
	}

	@Override
	public void setSecurityzoneStore(List<SecurityzoneDTO> list) {
		securityzoneStore.replaceAll(list);
	}

	@Override
	public HasSelectHandlers getAddMeasuresButtonClick() {
		return measureGridList.getAddButtonClick();
	}

	@Override
	public ListStore<MeasureDTO> getMeasureStore() {
		return measureStore;
	}

	@Override
	public void setAddAssettypeButtonText(String string) {
		assettypeGridList.setAddButtonText(string);
	}

	@Override
	public void setAddMeasureButtonText(String string) {
		measureGridList.setAddButtonText(string);
	}

	@Override
	public void setChooseStartAssetButtonText(String string) {
		chooseStartAsset.setText(string);
	}

	@Override
	public HasSelectHandlers getChooseStartAssetButtonClick() {
		return chooseStartAsset;
	}

	@Override
	public void setChooseStartAssetFieldValue(String string) {
		assetField.setValue(string);
	}

	@Override
	public SecurityzoneDTO getSelectedSecurityzone() {
		return securityzoneBox.getCurrentValue();
	}

	@Override
	public void addSecurityzoneComboBoxSelectionHandler(
	        SelectionHandler<SecurityzoneDTO> changeHandler) {
		securityzoneBox.addSelectionHandler(changeHandler);
	}

	@Override
	public SecurityzoneDTO getPreviousSecurityzone() {
		return previousSecurityzoneDTO;
	}

}
