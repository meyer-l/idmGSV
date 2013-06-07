package de.gsv.idm.client.view.securityzone;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.client.editor.ListStoreEditor;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.CenterLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent.HasSelectHandlers;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;

import de.gsv.idm.client.images.ImageResources;
import de.gsv.idm.client.presenter.securityzone.SecurityzoneEditorPresenter.SecurityzoneEditorDisplay;
import de.gsv.idm.client.view.general.GeneralEditor;
import de.gsv.idm.client.view.gsk.widgets.MeasureGridList;
import de.gsv.idm.client.view.properties.MeasureDTOProperties;
import de.gsv.idm.client.view.validation.EmptyFieldValidator;
import de.gsv.idm.client.view.widgets.form.GridList;
import de.gsv.idm.shared.dto.MeasureDTO;
import de.gsv.idm.shared.dto.SecurityzoneDTO;

public class SecurityzoneEditor extends GeneralEditor<SecurityzoneDTO> implements
        SecurityzoneEditorDisplay {
	TextField name = new TextField();
	TextField orderNumberString = new TextField();
	ListStore<MeasureDTO> measureStore;
	ListStoreEditor<MeasureDTO> measures;
	GridList<MeasureDTO> measuresContainer;
	ListStore<MeasureDTO> inheritedMeasureStore;
	ListStoreEditor<MeasureDTO> inheritedMeasures;
	GridList<MeasureDTO> inheritedMeasuresContainer;
	TextField childName;
	@Ignore
	FieldLabel childNameLabel;

	private ImageResources imageBundler = GWT.create(ImageResources.class);
	BorderLayoutContainer borderLayoutContainer;
	@Ignore
	TextButton upButton;
	@Ignore
	TextButton downButton;
	@Ignore
	FieldLabel orderNumberLabel;

	public SecurityzoneEditor() {
		borderLayoutContainer = new BorderLayoutContainer();
		container.add(new FieldLabel(name, "Name"), getFormData());
		name.addValidator(new EmptyFieldValidator());
		HorizontalLayoutContainer orderNumberPanel = new HorizontalLayoutContainer();
		orderNumberPanel.add(orderNumberString, new HorizontalLayoutData(402, 100, new Margins(0,
		        4, 4, 0)));
		Image image = new Image(imageBundler.help());
		image.setTitle("Die Ordnung einer Schutzzone kann durch die mittig "
		        + "angeordnenten Buttons geändert werden.");
		orderNumberPanel.add(image, new HorizontalLayoutData(100, 100, new Margins(0, 4, 4, 4)));
		orderNumberLabel = new FieldLabel(orderNumberPanel, "Ordnungsnummer");
		container.add(orderNumberLabel, getFormData());
		FieldLabel seperatorLabel = new FieldLabel(null, "");
		seperatorLabel.setLabelSeparator("");
		container.add(seperatorLabel, getHalfFormData());
		orderNumberString.setReadOnly(true);
		ImageResources imageBundler = GWT.create(ImageResources.class);
		final MeasureDTOProperties measureProps = GWT.create(MeasureDTOProperties.class);
		measureStore = new ListStore<MeasureDTO>(measureProps.key());
		measures = new ListStoreEditor<MeasureDTO>(measureStore);
		measuresContainer = new MeasureGridList(measureStore);
		measuresContainer.setText("Verlinkte Maßnahmen");
		container.add(measuresContainer, getFormData());

		inheritedMeasureStore = new ListStore<MeasureDTO>(measureProps.key());
		inheritedMeasures = new ListStoreEditor<MeasureDTO>(inheritedMeasureStore);
		inheritedMeasuresContainer = new MeasureGridList(inheritedMeasureStore);
		inheritedMeasuresContainer.setText("Geerbte Maßnahmen");
		inheritedMeasuresContainer.setAddButtonVisibility(false);
		childName = new TextField();
		childName.disable();
		HorizontalLayoutContainer childNamePanel = new HorizontalLayoutContainer();
		childNamePanel.add(childName, new HorizontalLayoutData(402, 100, new Margins(0, 4, 4, 0)));
		Image childToolTip = new Image(imageBundler.help());
		childToolTip.setTitle("Eine Schutzzone erbt die Maßnahmen der untergeordneten"
		        + " Schutzzone. Die Ordnung einer Schutzzone kann durch die mittig "
		        + "angeordnenten Buttons geändert werden. ");
		childNamePanel.add(childToolTip,
		        new HorizontalLayoutData(100, 100, new Margins(0, 4, 4, 4)));
		childNameLabel = new FieldLabel(childNamePanel, "Untergeordnete Schutzzone");
		container.add(childNameLabel, getFormData());
		container.add(inheritedMeasuresContainer, getFormData());

		borderLayoutContainer.setEastWidget(buttonPanel, new BorderLayoutData(0.70 * CLIENT_WIDTH));
		addButtons();

		FlowPanel upPanel = new FlowPanel();
		upButton = new TextButton();
		upButton.setIcon(imageBundler.arrowUp());
		upPanel.add(upButton);

		FlowPanel downPanel = new FlowPanel();
		downButton = new TextButton();
		downButton.setIcon(imageBundler.arrowDown());
		downPanel.add(downButton);

		FlowPanel orderPanel = new FlowPanel();
		orderPanel.add(upPanel);
		orderPanel.add(downPanel);

		CenterLayoutContainer orderCenterPanel = new CenterLayoutContainer();
		orderCenterPanel.add(orderPanel);
		borderLayoutContainer.setCenterWidget(orderCenterPanel);

	}

	public Widget asWidget() {
		return borderLayoutContainer;
	}

	public void setAddMeasuresButtonText(String text) {
		measuresContainer.setAddButtonText(text);
	}

	@Override
	public HasSelectHandlers getAddMeasuresButtonClick() {
		return measuresContainer.getAddButtonClick();
	}

	public ListStore<MeasureDTO> getMeasuresStore() {
		return measureStore;
	}

	@Override
	public HasSelectHandlers getUpButtonClick() {
		return upButton;
	}

	@Override
	public void setUpButtonText(String text) {
		upButton.setText(text);
	}

	@Override
	public HasSelectHandlers getDownButtonClick() {
		return downButton;
	}

	@Override
	public void setDownButtonText(String text) {
		downButton.setText(text);
	}

	@Override
	public ListStore<MeasureDTO> getInheritedMeasuresStore() {
		return inheritedMeasureStore;
	}

	@Override
	public void setUpButtonToolTip(String toolTip) {
		upButton.setToolTip(toolTip);
	}

	@Override
	public void setDownButtonToolTip(String toolTip) {
		downButton.setToolTip(toolTip);
	}

	@Override
	public void setOrderNumberVisible(Boolean enabled) {
		orderNumberLabel.setVisible(enabled);
		inheritedMeasuresContainer.setVisible(enabled);
		childNameLabel.setVisible(enabled);
	}

	@Override
	public void setEnabled(Boolean enabled) {
		name.setEnabled(enabled);
		orderNumberString.setEnabled(enabled);
		measuresContainer.setEnabled(enabled);
		inheritedMeasuresContainer.setEnabled(enabled);
	}
}
