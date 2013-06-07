package de.gsv.idm.client.view.information;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.client.editor.ListStoreEditor;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent.HasSelectHandlers;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;

import de.gsv.idm.client.images.ImageResources;
import de.gsv.idm.client.presenter.information.InformationEditorPresenter.InformationEditorDisplay;
import de.gsv.idm.client.view.general.GeneralEditor;
import de.gsv.idm.client.view.gsk.widgets.MeasureGridList;
import de.gsv.idm.client.view.properties.MeasureDTOProperties;
import de.gsv.idm.client.view.properties.SecurityzoneDTOProperties;
import de.gsv.idm.client.view.validation.EmptyFieldValidator;
import de.gsv.idm.client.view.validation.SecurityAssesmentValidator;
import de.gsv.idm.client.view.widgets.form.SecurityLevelAssesmentBox;
import de.gsv.idm.shared.dto.InformationDTO;
import de.gsv.idm.shared.dto.MeasureDTO;
import de.gsv.idm.shared.dto.SecurityzoneDTO;

//TODO: MeasureContainer wird falsch aktualisiert wenn noch "alte Maßnahmen" aus der adneren schutzzone bestehen.
public class InformationEditor extends GeneralEditor<InformationDTO> implements
        InformationEditorDisplay {

	TextField name = new TextField();
	TextField ident = new TextField();
	SecurityLevelAssesmentBox availability;
	TextArea availabilityExplanation = new TextArea();
	SecurityLevelAssesmentBox confidentiality;
	TextArea confidentialityExplanation = new TextArea();
	SecurityLevelAssesmentBox integrity;
	TextArea integrityExplanation = new TextArea();
	CheckBox personalData = new CheckBox();

	SecurityzoneDTO previousSecurityzone = null;

	MeasureGridList measuresContainer;
	ListStore<MeasureDTO> measuresStore;
	ListStoreEditor<MeasureDTO> measures;

	ListStore<SecurityzoneDTO> securityzoneStore;
	ComboBox<SecurityzoneDTO> securityzone;
	@Ignore
	TextButton editSecurityzonesButton;

	public InformationEditor() {
		container.add(new FieldLabel(name, "Name"), getFormData());
		name.addValidator(new EmptyFieldValidator());
		container.add(new FieldLabel(ident, "Ident"), getFormData());

		availability = new SecurityLevelAssesmentBox();
		container
		        .add(new FieldLabel(availability, "Schutzbedarf Verfügbarkeit"), getHalfFormData());
		availabilityExplanation.setHeight(100);
		availabilityExplanation.addValidator(new SecurityAssesmentValidator(availability));
		container.add(new FieldLabel(availabilityExplanation, "Verfügbarkeits- erläuterung"),
		        getFormData());

		confidentiality = new SecurityLevelAssesmentBox();
		container.add(new FieldLabel(confidentiality, "Schutzbedarf Vertraulichkeit"),
		        getHalfFormData());
		confidentialityExplanation.setHeight(100);
		confidentialityExplanation.addValidator(new SecurityAssesmentValidator(confidentiality));
		container.add(new FieldLabel(confidentialityExplanation, "Vertraulichkeits- erläuterung"),
		        getFormData());

		integrity = new SecurityLevelAssesmentBox();
		container.add(new FieldLabel(integrity, "Schutzbedarf Integrität"), getHalfFormData());
		integrityExplanation.setHeight(100);
		integrityExplanation.addValidator(new SecurityAssesmentValidator(integrity));
		container.add(new FieldLabel(integrityExplanation, "Integritäts- erläuterung"),
		        getFormData());

		HorizontalPanel personalDataCheckBoxPanel = new HorizontalPanel();
		personalDataCheckBoxPanel.add(personalData);
		container.add(new FieldLabel(personalDataCheckBoxPanel, "Personenbezug"),
		        new VerticalLayoutData(-1, -1, new Margins(5, 0, 5, 10)));

		SecurityzoneDTOProperties securityzoneProps = GWT.create(SecurityzoneDTOProperties.class);
		securityzoneStore = new ListStore<SecurityzoneDTO>(securityzoneProps.key());
		securityzoneStore.addSortInfo(new StoreSortInfo<SecurityzoneDTO>(securityzoneProps
		        .orderNumber(), SortDir.ASC));
		securityzone = new ComboBox<SecurityzoneDTO>(securityzoneStore, securityzoneProps.label());
		securityzone.addBeforeSelectionHandler(new BeforeSelectionHandler<SecurityzoneDTO>() {

			@Override
			public void onBeforeSelection(BeforeSelectionEvent<SecurityzoneDTO> event) {
				previousSecurityzone = securityzone.getCurrentValue();
			}
		});
		securityzone.setTypeAhead(true);
		securityzone.setTriggerAction(TriggerAction.ALL);
		container.add(new FieldLabel(securityzone, "Schutzzone"), getHalfFormData());
		editSecurityzonesButton = new TextButton();
		ImageResources imageBundler = GWT.create(ImageResources.class);
		editSecurityzonesButton.setIcon(imageBundler.edit());
		FieldLabel editSecurityzoneButtonLabel = new FieldLabel(editSecurityzonesButton, "");
		editSecurityzoneButtonLabel.setLabelSeparator("");
		container.add(editSecurityzoneButtonLabel, new VerticalLayoutData(-1, -1, new Margins(5, 0,
		        5, 10)));

		final MeasureDTOProperties props = GWT.create(MeasureDTOProperties.class);
		measuresStore = new ListStore<MeasureDTO>(props.key());
		measures = new ListStoreEditor<MeasureDTO>(measuresStore);
		measuresContainer = new MeasureGridList(measuresStore);
		measuresContainer.setAddButtonVisibility(false);
		container.add(measuresContainer, getFormData());

		addButtons();
	}

	@Override
	public ListStore<SecurityzoneDTO> getSecurityzoneStore() {
		return securityzoneStore;
	}

	public void setMeasuresLabel(String text) {
		measuresContainer.setText(text);
	}

	@Override
	public SecurityzoneDTO getPreviousSecurityzone() {
		return previousSecurityzone;
	}

	@Override
	public void addSecurityzoneComboBoxSelectionHandler(
	        SelectionHandler<SecurityzoneDTO> selectionHandler) {
		securityzone.addSelectionHandler(selectionHandler);
	}

	@Override
	public ListStore<MeasureDTO> getMeasuresStore() {
		return measuresStore;
	}

	public void setPersonalDataTooltip(String toolTip) {
		personalData.setToolTip(toolTip);
	}

	@Override
	public HasSelectHandlers getEditSecurityzonesButtonClick() {
		return editSecurityzonesButton;
	}

	@Override
	public void setEditSecurityzonesButtonText(String text) {
		editSecurityzonesButton.setText(text);

	}

	@Override
	public void setChooseSecurityzoneTooltip(String toolTip) {
		securityzone.setToolTip(toolTip);
	}

	@Override
	public void clearSecurityzoneBox(SecurityzoneDTO object) {
		if (securityzone.getValue().equals(object)) {
			securityzone.clear();
		}
	}

	@Override
	public void setEnabled(Boolean enabled) {
		name.setEnabled(enabled);
		ident.setEnabled(enabled);

		availability.setEnabled(enabled);
		availabilityExplanation.setEnabled(enabled);
		confidentiality.setEnabled(enabled);
		confidentialityExplanation.setEnabled(enabled);
		integrity.setEnabled(enabled);
		integrityExplanation.setEnabled(enabled);
		personalData.setEnabled(enabled);

		measuresContainer.setEnabled(enabled);
		securityzone.setEnabled(enabled);
		editSecurityzonesButton.setEnabled(enabled);
	}

}
