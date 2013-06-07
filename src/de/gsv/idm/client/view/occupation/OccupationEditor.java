package de.gsv.idm.client.view.occupation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.client.editor.ListStoreEditor;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent.HasSelectHandlers;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;

import de.gsv.idm.client.images.ImageResources;
import de.gsv.idm.client.presenter.occupation.OccupationEditorPresenter.OccupationEditorDisplay;
import de.gsv.idm.client.view.general.GeneralEditor;
import de.gsv.idm.client.view.information.widgets.InformationGridList;
import de.gsv.idm.client.view.properties.InformationDTOProperties;
import de.gsv.idm.client.view.validation.EmptyFieldValidator;
import de.gsv.idm.client.view.validation.SecurityAssesmentValidator;
import de.gsv.idm.client.view.widgets.form.SecurityLevelAssesmentBox;
import de.gsv.idm.shared.dto.InformationDTO;
import de.gsv.idm.shared.dto.OccupationDTO;

public class OccupationEditor extends GeneralEditor<OccupationDTO> implements
        OccupationEditorDisplay {

	TextField name = new TextField();
	TextField ident = new TextField();

	CheckBox manuelSecurityAssesment = new CheckBox();
	SecurityLevelAssesmentBox availability;
	TextArea availabilityExplanation = new TextArea();
	SecurityLevelAssesmentBox confidentiality;
	TextArea confidentialityExplanation = new TextArea();
	SecurityLevelAssesmentBox integrity;
	TextArea integrityExplanation = new TextArea();
	TextField calculatedAvailabilityName = new TextField();
	TextField calculatedConfidentialityName = new TextField();
	TextField calculatedIntegrityName = new TextField();
	CheckBox personalData = new CheckBox();

	ListStore<InformationDTO> informationStore;
	ListStoreEditor<InformationDTO> informations;

	TextField assignedSecurityzoneName = new TextField();
	@Ignore
	TextButton editSecurityzoneButton = new TextButton();
	InformationGridList informationsContainer;

	// Function in Presenter auslagern ...
	public OccupationEditor() {
		container.add(new FieldLabel(name, "Name"), getFormData());
		name.addValidator(new EmptyFieldValidator());
		container.add(new FieldLabel(ident, "Ident"), getFormData());

		final InformationDTOProperties props = GWT.create(InformationDTOProperties.class);
		informationStore = new ListStore<InformationDTO>(props.key());
		informations = new ListStoreEditor<InformationDTO>(informationStore);
		informationsContainer = new InformationGridList(informationStore);
		container.add(informationsContainer, getFormData());
		container.add(new FieldLabel(assignedSecurityzoneName, "Zugewiesene Schutzzone"),
		        getHalfFormData());
		assignedSecurityzoneName.setReadOnly(true);
		ImageResources imageBundler = GWT.create(ImageResources.class);
		editSecurityzoneButton.setIcon(imageBundler.edit());
		FieldLabel securityzoneEditButtonLabel = new FieldLabel(editSecurityzoneButton, "");
		securityzoneEditButtonLabel.setLabelSeparator("");
		container.add(securityzoneEditButtonLabel, new VerticalLayoutData(-1, -1, new Margins(5, 0,
		        5, 10)));
		calculatedAvailabilityName.setReadOnly(true);
		container.add(new FieldLabel(calculatedAvailabilityName,
		        "Berechneter Schutzbedarf Verfügbarkeit"), getHalfFormData());
		calculatedConfidentialityName.setReadOnly(true);
		container.add(new FieldLabel(calculatedConfidentialityName,
		        "Berechneter Schutzbedarf Vertraulichkeit"), getHalfFormData());
		calculatedIntegrityName.setReadOnly(true);
		container.add(
		        new FieldLabel(calculatedIntegrityName, "Berechneter Schutzbedarf Integrität"),
		        getHalfFormData());

		HorizontalPanel manualSecurityAssesmentCheckBoxPanel = new HorizontalPanel();
		manualSecurityAssesmentCheckBoxPanel.add(manuelSecurityAssesment);
		container.add(new FieldLabel(manualSecurityAssesmentCheckBoxPanel,
		        "Schutzbedarf manuell festlegen"), new VerticalLayoutData(-1, -1, new Margins(5, 0,
		        5, 10)));

		availability = new SecurityLevelAssesmentBox();
		container.add(new FieldLabel(availability, "Verfügbarkeit"), getHalfFormData());
		availabilityExplanation.setHeight(100);
		availabilityExplanation.addValidator(new SecurityAssesmentValidator(availability));
		container.add(new FieldLabel(availabilityExplanation, "Verfügbarkeits- erläuterung"),
		        getFormData());

		confidentiality = new SecurityLevelAssesmentBox();
		container.add(new FieldLabel(confidentiality, "Vertraulichkeit"), getHalfFormData());
		confidentialityExplanation.setHeight(100);
		confidentialityExplanation.addValidator(new SecurityAssesmentValidator(confidentiality));
		container.add(new FieldLabel(confidentialityExplanation, "Vertraulichkeits- erläuterung"),
		        getFormData());

		integrity = new SecurityLevelAssesmentBox();
		container.add(new FieldLabel(integrity, "Integrität"), getHalfFormData());
		integrityExplanation.setHeight(100);
		integrityExplanation.addValidator(new SecurityAssesmentValidator(integrity));
		container.add(new FieldLabel(integrityExplanation, "Integritäts- erläuterung"),
		        getFormData());

		HorizontalPanel personalDataCheckBoxPanel = new HorizontalPanel();
		personalDataCheckBoxPanel.add(personalData);
		personalData.disable();
		container.add(new FieldLabel(personalDataCheckBoxPanel, "Personenbezug"),
		        new VerticalLayoutData(-1, -1, new Margins(5, 0, 5, 10)));

		addButtons();

	}

	public ListStore<InformationDTO> getInformationStore() {
		return informationStore;
	}

	public HasSelectHandlers getAddInformationButtonClick() {
		return informationsContainer.getAddButtonClick();
	}

	public void setInformationLabel(String text) {
		informationsContainer.setText(text);
	}

	public void setAddInformationButtonText(String text) {
		informationsContainer.setAddButtonText(text);
	}

	@Override
	public HasChangeHandlers getManuelSecurityAssesmentCheckBoxClick() {
		return manuelSecurityAssesment;
	}

	@Override
	public void setSecurityAssesmentFieldsEnabled(Boolean bool) {
		availability.setEnabled(bool);
		availabilityExplanation.setEnabled(bool);
		confidentiality.setEnabled(bool);
		confidentialityExplanation.setEnabled(bool);
		integrity.setEnabled(bool);
		integrityExplanation.setEnabled(bool);
		personalData.setEnabled(bool);
	}

	public void setPersonalDataToolTip(String toolTip) {
		personalData.setToolTip(toolTip);
	}

	@Override
	public void setCalculatedAvailabilityTooltip(String toolTip) {
		calculatedAvailabilityName.setToolTip(toolTip);
	}

	@Override
	public void setCalculatedConfidentialityTip(String toolTip) {
		calculatedConfidentialityName.setToolTip(toolTip);
	}

	@Override
	public void setCalculatedIntegrityTooltip(String toolTip) {
		calculatedIntegrityName.setToolTip(toolTip);
	}

	@Override
	public void setManualAssesmentTooltip(String toolTip) {
		manuelSecurityAssesment.setToolTip(toolTip);
	}

	@Override
	public void setAssignedSecurityzoneTooltip(String toolTip) {
		assignedSecurityzoneName.setToolTip(toolTip);
	}

	@Override
	public HasSelectHandlers getEditSecurityzoneButtonClick() {
		return editSecurityzoneButton;
	}

	@Override
	public void setEditSecurityzoneButtonText(String text) {
		editSecurityzoneButton.setText(text);
	}

	@Override
	public void setEditSecurityzoneButtonEnabled(Boolean enabled) {
		editSecurityzoneButton.setEnabled(enabled);
	}

	@Override
	public void setEnabled(Boolean enabled) {
		name.setEnabled(enabled);
		ident.setEnabled(enabled);
		manuelSecurityAssesment.setEnabled(enabled);
		availability.setEnabled(enabled);
		availabilityExplanation.setEnabled(enabled);
		confidentiality.setEnabled(enabled);
		confidentialityExplanation.setEnabled(enabled);
		integrity.setEnabled(enabled);
		integrityExplanation.setEnabled(enabled);
		calculatedAvailabilityName.setEnabled(enabled);
		calculatedConfidentialityName.setEnabled(enabled);
		calculatedIntegrityName.setEnabled(enabled);
		personalData.setEnabled(enabled);
		assignedSecurityzoneName.setEnabled(enabled);
		editSecurityzoneButton.setEnabled(enabled);
		informationsContainer.setEnabled(enabled);

	}

}
